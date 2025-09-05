package com.example.project.features.domain.repository

import com.example.project.features.domain.FeatureFlag
import com.example.project.features.domain.Features

/**
 * Repository interface for managing feature flags in the application.
 *
 * This repository provides a clean abstraction layer for feature flag operations, following the
 * repository pattern. It allows the application to work with feature flags without being coupled to
 * specific provider implementations or data sources.
 *
 * The repository supports both single and bulk feature flag retrieval, as well as user context
 * management for targeted feature flag evaluation.
 *
 * @example
 *
 * ```kotlin
 * class MyViewModel(private val featuresRepository: FeaturesRepository) {
 *     suspend fun checkFeatures() {
 *         // Get a single feature flag
 *         val isNewFeatureEnabled = featuresRepository.get(NewUserOnboarding)
 *
 *         // Get multiple feature flags at once
 *         val features = featuresRepository.get(NewUserOnboarding, DarkModeEnabled)
 *
 *         // Set user context for targeted evaluation
 *         featuresRepository.setUserData("user123", "user@example.com", "US")
 *     }
 * }
 * ```
 */
internal interface FeaturesRepository {

    /**
     * Retrieves the value of a single feature flag.
     *
     * @param flag The feature flag to retrieve
     * @return The boolean value of the feature flag
     * @example
     *
     * ```kotlin
     * val isEnabled = repository.get(NewUserOnboarding)
     * if (isEnabled) {
     *     showOnboarding()
     * }
     * ```
     */
    suspend fun get(flag: FeatureFlag): Boolean

    /**
     * Retrieves the values of multiple feature flags.
     *
     * This method allows you to fetch multiple feature flags in a single call, which can be more
     * efficient than making multiple individual calls.
     *
     * @param flags Variable number of feature flags to retrieve
     * @return A map of feature flags to their boolean values
     * @example
     *
     * ```kotlin
     * val features = repository.get(NewUserOnboarding, DarkModeEnabled, PremiumFeatures)
     * val onboardingEnabled = features[NewUserOnboarding] ?: false
     * val darkModeEnabled = features[DarkModeEnabled] ?: false
     * ```
     */
    suspend fun get(vararg flags: FeatureFlag): Map<FeatureFlag, Boolean>

    /**
     * Retrieves the values of a list of feature flags.
     *
     * This method is similar to the vararg version but accepts a list of feature flags. It returns
     * a [Features] collection with string keys for easier integration with other parts of the
     * application.
     *
     * @param flags List of feature flags to retrieve
     * @return A [Features] collection with string keys and boolean values
     * @example
     *
     * ```kotlin
     * val featureList = listOf(NewUserOnboarding, DarkModeEnabled, PremiumFeatures)
     * val features = repository.get(featureList)
     *
     * // Access using string keys
     * val onboardingEnabled = features["new_user_onboarding"] ?: false
     * ```
     */
    suspend fun get(flags: List<FeatureFlag>): Features

    /**
     * Sets user data for targeted feature flag evaluation.
     *
     * This method allows you to provide user context information that can be used by the feature
     * flag provider for user segmentation and targeted feature flag evaluation.
     *
     * @param userId The unique identifier for the user
     * @param email The user's email address (optional)
     * @param country The user's country code (optional)
     * @example
     *
     * ```kotlin
     * // Set user context when user logs in
     * repository.setUserData(
     *     userId = "user123",
     *     email = "user@example.com",
     *     country = "US"
     * )
     *
     * // Now feature flags can be evaluated based on user context
     * val isPremiumFeatureEnabled = repository.get(PremiumFeatures)
     * ```
     */
    suspend fun setUserData(userId: String, email: String? = null, country: String? = null)
}
