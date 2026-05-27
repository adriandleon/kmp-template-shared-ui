package com.example.project.features.domain

/**
 * Type alias for a collection of feature flags represented as key-value pairs.
 *
 * This type alias provides a convenient way to work with feature flag collections where the key is
 * the feature flag identifier and the value is its boolean state.
 */
internal typealias Features = Map<String, Boolean>

/**
 * Retrieves the value of a specific feature flag from a features collection.
 *
 * This extension function provides a type-safe way to get feature flag values from a [Features]
 * collection. If the feature flag key is not found in the collection, it returns the default value
 * defined in the [FeatureFlag] object.
 *
 * @param feature The feature flag to retrieve
 * @return The feature flag value or its default value if not found
 * @example
 *
 * ```kotlin
 * val features: Features = mapOf(
 *     "new_user_onboarding" to true,
 *     "dark_mode_enabled" to false
 * )
 *
 * val onboardingEnabled = features.get(NewUserOnboarding) // true
 * val darkModeEnabled = features.get(DarkModeEnabled) // false
 * val unknownFeature = features.get(UnknownFeature) // returns UnknownFeature.default
 * ```
 */
internal fun Features.get(feature: FeatureFlag): Boolean =
    getOrElse(feature.key) { feature.default }

/**
 * Converts a map of [FeatureFlag] objects to their boolean values into a [Features] collection.
 *
 * This extension function transforms a map where keys are [FeatureFlag] objects into a [Features]
 * collection where keys are the string identifiers of the feature flags.
 *
 * @return A [Features] collection with string keys and boolean values
 * @example
 *
 * ```kotlin
 * val featureMap = mapOf(
 *     NewUserOnboarding to true,
 *     DarkModeEnabled to false
 * )
 *
 * val features = featureMap.asFeatures()
 * // Result: mapOf("new_user_onboarding" to true, "dark_mode_enabled" to false)
 * ```
 */
internal fun Map<FeatureFlag, Boolean>.asFeatures(): Features = mapKeys { it.key.key }
