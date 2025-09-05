package com.example.project.features.domain.provider

/**
 * Interface for feature flag providers that abstracts the implementation details of different
 * feature flag services (e.g., ConfigCat, Firebase RemoteConfig).
 *
 * This interface provides a vendor-agnostic way to interact with feature flag services, allowing
 * the application to switch between different providers without changing the business logic.
 * Implementations handle the specific details of communicating with their respective feature flag
 * services.
 *
 * The interface supports multiple data types for feature flags including boolean, string, integer,
 * and double values. It also provides methods for bulk operations and user context management for
 * targeted feature flag evaluation.
 *
 * @example
 *
 * ```kotlin
 * // Using with ConfigCat
 * val configCatProvider = ConfigCatProvider(configCatClient)
 * val isEnabled = configCatProvider.getBoolean("new_feature", false)
 *
 * // Using with Firebase Remote Config
 * val firebaseProvider = RemoteConfigProvider(remoteConfig)
 * val featureValue = firebaseProvider.getString("feature_config", "default")
 * ```
 */
internal interface FeatureFlagProvider {
    /**
     * Gets a boolean feature flag value.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found or unavailable
     * @return The feature flag value or the default value if not found
     * @example
     *
     * ```kotlin
     * val isNewFeatureEnabled = provider.getBoolean("new_feature_enabled", false)
     * if (isNewFeatureEnabled) {
     *     // Show new feature
     * }
     * ```
     */
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /**
     * Gets a string feature flag value.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found or unavailable
     * @return The feature flag value or the default value if not found
     * @example
     *
     * ```kotlin
     * val themeColor = provider.getString("theme_color", "#FFFFFF")
     * val apiEndpoint = provider.getString("api_endpoint", "https://api.example.com")
     * ```
     */
    suspend fun getString(key: String, defaultValue: String = ""): String

    /**
     * Gets an integer feature flag value.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found or unavailable
     * @return The feature flag value or the default value if not found
     * @example
     *
     * ```kotlin
     * val maxRetries = provider.getInt("max_retries", 3)
     * val cacheTimeout = provider.getInt("cache_timeout_seconds", 300)
     * ```
     */
    suspend fun getInt(key: String, defaultValue: Int = 0): Int

    /**
     * Gets a double feature flag value.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found or unavailable
     * @return The feature flag value or the default value if not found
     * @example
     *
     * ```kotlin
     * val animationDuration = provider.getDouble("animation_duration", 0.3)
     * val threshold = provider.getDouble("error_threshold", 0.95)
     * ```
     */
    suspend fun getDouble(key: String, defaultValue: Double = 0.0): Double

    /**
     * Gets all available feature flag keys.
     *
     * This method returns all feature flag keys that are currently available from the provider.
     * This can be useful for debugging, monitoring, or bulk operations.
     *
     * @return Collection of all available feature flag keys
     * @example
     *
     * ```kotlin
     * val allKeys = provider.getAllKeys()
     * println("Available feature flags: ${allKeys.joinToString()}")
     * ```
     */
    suspend fun getAllKeys(): Collection<String>

    /**
     * Gets all feature flag values.
     *
     * This method returns all feature flag keys and their current values from the provider. The
     * values are returned as [Any?] to accommodate different data types.
     *
     * @return Map of all feature flag keys and their values
     * @example
     *
     * ```kotlin
     * val allValues = provider.getAllValues()
     * allValues.forEach { (key, value) ->
     *     println("$key: $value")
     * }
     * ```
     */
    suspend fun getAllValues(): Map<String, Any?>

    /**
     * Forces a refresh of the feature flags from the remote server.
     *
     * This method triggers an immediate fetch of the latest feature flag values from the remote
     * server, bypassing any local cache. This is useful when you need to ensure you have the most
     * up-to-date values.
     *
     * @return true if the refresh was successful, false otherwise
     * @example
     *
     * ```kotlin
     * val refreshSuccess = provider.refresh()
     * if (refreshSuccess) {
     *     println("Feature flags refreshed successfully")
     * } else {
     *     println("Failed to refresh feature flags")
     * }
     * ```
     */
    suspend fun refresh(): Boolean

    /**
     * Sets user properties for the feature flag provider.
     *
     * This method allows you to set user context information that can be used for targeted feature
     * flag evaluation. Different providers may use this information differently for user
     * segmentation and targeting.
     *
     * @param userId The unique identifier for the user
     * @param email The user's email address (optional)
     * @param country The user's country code (optional)
     * @example
     *
     * ```kotlin
     * provider.setUserProperties(
     *     userId = "user123",
     *     email = "user@example.com",
     *     country = "US"
     * )
     *
     * // Now feature flags can be evaluated based on user context
     * val isPremiumFeatureEnabled = provider.getBoolean("premium_feature", false)
     * ```
     */
    fun setUserProperties(userId: String, email: String?, country: String?)
}
