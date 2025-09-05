package com.example.project.features.domain

/**
 * Represents a feature flag definition with its key and default value.
 *
 * Feature flags allow you to enable, disable, or change the behavior of features in your
 * application without modifying the source code. This interface provides a type-safe way to define
 * feature flags with their unique identifiers and default values.
 *
 * @property key The unique identifier for the feature flag
 * @property default The default value to use when the flag is not found or unavailable
 * @example
 *
 * ```kotlin
 * object NewUserOnboarding : FeatureFlag {
 *     override val key = "new_user_onboarding"
 *     override val default = false
 * }
 *
 * object DarkModeEnabled : FeatureFlag {
 *     override val key = "dark_mode_enabled"
 *     override val default = true
 * }
 * ```
 */
internal interface FeatureFlag {
    /** The unique identifier for the feature flag */
    val key: String

    /** The default value to use when the flag is not found or unavailable */
    val default: Boolean
}
