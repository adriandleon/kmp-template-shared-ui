package com.example.project.features

import com.configcat.ConfigCatClient
import com.configcat.lazyLoad
import com.configcat.log.LogLevel
import com.configcat.log.Logger
import com.example.project.BuildKonfig
import com.example.project.common.util.DispatcherProvider
import com.example.project.features.data.datasource.FeaturesDataSource
import com.example.project.features.data.provider.RemoteConfigProvider
import com.example.project.features.domain.provider.FeatureFlagProvider
import com.example.project.features.domain.repository.FeaturesRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes

/**
 * Koin module for feature flag dependency injection.
 *
 * This module provides all the necessary dependencies for the feature flag system, including
 * provider implementations, data sources, and configuration. It supports multiple feature flag
 * providers (ConfigCat and Firebase Remote Config) and allows for easy switching between them.
 *
 * The module is designed to be flexible and allows you to:
 * - Switch between different feature flag providers
 * - Use different providers for different environments (debug/release)
 * - Configure provider-specific settings
 *
 * @example
 *
 * ```kotlin
 * // In your Koin module setup
 * startKoin {
 *     modules(featureFlagModule)
 * }
 *
 * // Usage in your classes
 * class MyViewModel(private val featuresRepository: FeaturesRepository) {
 *     suspend fun checkFeature() {
 *         val isEnabled = featuresRepository.get(NewUserOnboarding)
 *     }
 * }
 * ```
 */
internal val featureFlagModule = module {
    // Firebase Remote Config instance
    singleOf(::firebaseRemoteConfig) { bind<FirebaseRemoteConfig>() }

    // Features repository implementation
    factoryOf(::FeaturesDataSource) { bind<FeaturesRepository>() }

    // ConfigCat client instance
    singleOf(::configCatClient)

    /**
     * Feature flag provider selection.
     *
     * **Important**: Use only one provider at a time. The provider selection determines which
     * feature flag service your application will use.
     *
     * **Provider Options:**
     * 1. **ConfigCat Provider**:
     * ```kotlin
     * single<FeatureFlagProvider> { ConfigCatProvider(get<ConfigCatClient>()) }
     * ```
     * 2. **Firebase Remote Config Provider** (Current):
     * ```kotlin
     * single<FeatureFlagProvider> { RemoteConfigProvider(get<FirebaseRemoteConfig>()) }
     * ```
     * 3. **Environment-based Provider Selection**:
     * ```kotlin
     * single<FeatureFlagProvider> {
     *   when {
     *     BuildKonfig.DEBUG -> RemoteConfigProvider(get<FirebaseRemoteConfig>())
     *     else -> ConfigCatProvider(get<ConfigCatClient>())
     *   }
     * }
     * ```
     *
     * **Current Configuration**: Using Firebase Remote Config as the provider. To switch providers,
     * uncomment the desired configuration above and comment out the current one.
     */
    single<FeatureFlagProvider> { RemoteConfigProvider(get<FirebaseRemoteConfig>()) }
}

/**
 * Creates and configures a ConfigCat client instance.
 *
 * This function sets up the ConfigCat client with appropriate configuration based on the build
 * environment. In debug mode, it enables logging and uses a shorter cache refresh interval for
 * faster development iteration.
 *
 * @param kermitLogger The Kermit logger instance for ConfigCat logging
 * @return Configured ConfigCat client instance
 */
private fun configCatClient(kermitLogger: Logger): ConfigCatClient =
    ConfigCatClient(sdkKey = BuildKonfig.CONFIGCAT_KEY) {
        logLevel = if (BuildKonfig.DEBUG) LogLevel.INFO else LogLevel.OFF
        pollingMode = lazyLoad { cacheRefreshInterval = 5.minutes }
        logger = kermitLogger
    }

/**
 * Creates and configures a Firebase Remote Config instance.
 *
 * This function sets up Firebase Remote Config with appropriate settings based on the build
 * environment. It automatically fetches and activates the latest configuration values in the
 * background.
 *
 * @param dispatcher The coroutine dispatcher provider for background operations
 * @return Configured Firebase Remote Config instance
 */
private fun firebaseRemoteConfig(dispatcher: DispatcherProvider): FirebaseRemoteConfig {
    val config = Firebase.remoteConfig
    CoroutineScope(dispatcher.default).launch {
        config.settings { minimumFetchInterval = if (BuildKonfig.DEBUG) 5.minutes else 1.minutes }
        config.fetchAndActivate()
    }
    return config
}
