package com.example.project.features.data.datasource

import com.example.project.features.domain.FeatureFlag
import com.example.project.features.domain.Features
import com.example.project.features.domain.asFeatures
import com.example.project.features.domain.provider.FeatureFlagProvider
import com.example.project.features.domain.repository.FeaturesRepository

/**
 * Data source implementation for feature flags that acts as a bridge between the domain layer and
 * the feature flag provider implementations.
 *
 * This class implements the [FeaturesRepository] interface and delegates feature flag operations to
 * the configured [FeatureFlagProvider]. It handles the conversion between domain objects and
 * provider-specific calls, ensuring that the domain layer remains decoupled from specific provider
 * implementations.
 *
 * The data source supports both single and bulk feature flag retrieval, as well as user context
 * management for targeted evaluation.
 *
 * @param provider The feature flag provider to use for retrieving flag values
 * @example
 *
 * ```kotlin
 * // Inject via Koin
 * val dataSource = FeaturesDataSource(configCatProvider)
 *
 * // Use in repository pattern
 * val isEnabled = dataSource.get(NewUserOnboarding)
 * val features = dataSource.get(NewUserOnboarding, DarkModeEnabled)
 * ```
 */
internal class FeaturesDataSource(private val provider: FeatureFlagProvider) : FeaturesRepository {

    /**
     * Retrieves the value of a single feature flag from the provider.
     *
     * This method delegates to the configured provider, using the feature flag's key and default
     * value for the provider call.
     *
     * @param flag The feature flag to retrieve
     * @return The boolean value of the feature flag
     */
    override suspend fun get(flag: FeatureFlag): Boolean {
        return provider.getBoolean(flag.key, defaultValue = flag.default)
    }

    /**
     * Retrieves the values of multiple feature flags from the provider.
     *
     * This method fetches multiple feature flags by making individual calls to the provider for
     * each flag. The results are collected into a map for easy access.
     *
     * @param flags Variable number of feature flags to retrieve
     * @return A map of feature flags to their boolean values
     */
    override suspend fun get(vararg flags: FeatureFlag): Map<FeatureFlag, Boolean> {
        val result: MutableMap<FeatureFlag, Boolean> = mutableMapOf()
        flags.forEach { flag -> result[flag] = get(flag) }
        return result.toMap()
    }

    /**
     * Retrieves the values of a list of feature flags from the provider.
     *
     * This method fetches multiple feature flags and returns them as a [Features] collection with
     * string keys for easier integration with other parts of the application.
     *
     * @param flags List of feature flags to retrieve
     * @return A [Features] collection with string keys and boolean values
     */
    override suspend fun get(flags: List<FeatureFlag>): Features {
        val result: MutableMap<FeatureFlag, Boolean> = mutableMapOf()
        flags.forEach { flag -> result[flag] = get(flag) }
        return result.toMap().asFeatures()
    }

    /**
     * Sets user data for targeted feature flag evaluation.
     *
     * This method delegates user context information to the provider, allowing for user-specific
     * feature flag evaluation and targeting.
     *
     * @param userId The unique identifier for the user
     * @param email The user's email address (optional)
     * @param country The user's country code (optional)
     */
    override suspend fun setUserData(userId: String, email: String?, country: String?) {
        provider.setUserProperties(userId, email, country)
    }
}
