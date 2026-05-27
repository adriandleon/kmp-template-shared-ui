package com.example.project.analytics

import com.example.project.analytics.data.AnalyticsImpl
import com.example.project.analytics.data.provider.FirebaseAnalyticsProvider
import com.example.project.analytics.domain.Analytics
import com.example.project.analytics.domain.AnalyticsProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin dependency injection module for analytics components.
 *
 * This module provides the necessary dependencies for the analytics system, including the main
 * [Analytics] implementation and configured providers.
 *
 * The module is designed to be:
 * - **Configurable**: Easy to add or remove providers
 * - **Testable**: Supports test implementations
 * - **Extensible**: Easy to add new provider types
 * - **Production-ready**: Includes Firebase Analytics integration
 *
 * @see Analytics for the main analytics interface
 * @see AnalyticsProvider for provider implementations
 * @see AnalyticsImpl for the main implementation
 * @see FirebaseAnalyticsProvider for Firebase integration
 * @example
 *
 * ```kotlin
 * // In your Koin setup
 * startKoin {
 *     modules(
 *         analyticsModule,
 *         // other modules...
 *     )
 * }
 *
 * // Usage in your classes
 * class MyRepository(
 *     private val analytics: Analytics
 * ) {
 *     fun doSomething() {
 *         analytics.track(CommonAnalyticsEvent.ButtonClick("DoSomething"))
 *     }
 * }
 * ```
 */
internal val analyticsModule = module {

    /**
     * Provides a list of analytics providers.
     *
     * This factory creates the list of providers that will be used by the analytics system.
     * Currently configured with Firebase Analytics, but can be easily extended with additional
     * providers:
     * - Firebase Analytics for Google Analytics integration
     * - Custom backend providers for your own analytics
     * - Test providers for unit testing
     * - Multiple providers for redundancy and A/B testing
     *
     * @return List of configured analytics providers
     */
    factory<List<AnalyticsProvider>>(named("analyticsProviders")) {
        listOf(
            FirebaseAnalyticsProvider()
            // Add more providers here as needed:
            // CustomBackendProvider(),
            // TestAnalyticsProvider() // for testing
        )
    }

    /**
     * Provides the main analytics implementation.
     *
     * This single instance will be used throughout the application for tracking analytics events.
     * It coordinates with all configured providers to ensure events are delivered to all services.
     *
     * @return The main analytics implementation
     */
    single<Analytics> { AnalyticsImpl(get(named("analyticsProviders"))) }
}
