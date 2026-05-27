package com.example.project.features.data.provider

import com.configcat.ConfigCatClient
import com.configcat.ConfigCatUser
import com.configcat.getValue
import com.example.project.features.domain.provider.FeatureFlagProvider

/**
 * ConfigCat implementation of the [FeatureFlagProvider] interface.
 *
 * This provider integrates with ConfigCat's feature flag service, allowing the application to
 * retrieve feature flags from ConfigCat's cloud-based platform. ConfigCat provides advanced
 * targeting capabilities, A/B testing, and real-time feature flag updates.
 *
 * The provider supports all standard feature flag data types (boolean, string, integer, double) and
 * provides methods for bulk operations and user context management for targeted evaluation.
 *
 * @param configCatClient The ConfigCat client instance for API communication
 * @example
 *
 * ```kotlin
 * // Create ConfigCat client
 * val configCatClient = ConfigCatClient("your-sdk-key") {
 *     pollingMode = lazyLoad { cacheRefreshInterval = 5.minutes }
 * }
 *
 * // Create provider
 * val provider = ConfigCatProvider(configCatClient)
 *
 * // Use feature flags
 * val isEnabled = provider.getBoolean("new_feature", false)
 * val configValue = provider.getString("api_endpoint", "https://api.example.com")
 * ```
 */
internal class ConfigCatProvider(private val configCatClient: ConfigCatClient) :
    FeatureFlagProvider {

    /**
     * Gets a boolean feature flag value from ConfigCat.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found
     * @return The feature flag value or the default value if not found
     */
    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        configCatClient.getValue(key, defaultValue)

    /**
     * Gets a string feature flag value from ConfigCat.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found
     * @return The feature flag value or the default value if not found
     */
    override suspend fun getString(key: String, defaultValue: String): String =
        configCatClient.getValue(key, defaultValue)

    /**
     * Gets an integer feature flag value from ConfigCat.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found
     * @return The feature flag value or the default value if not found
     */
    override suspend fun getInt(key: String, defaultValue: Int): Int =
        configCatClient.getValue(key, defaultValue)

    /**
     * Gets a double feature flag value from ConfigCat.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found
     * @return The feature flag value or the default value if not found
     */
    override suspend fun getDouble(key: String, defaultValue: Double): Double =
        configCatClient.getValue(key, defaultValue)

    /**
     * Gets all available feature flag keys from ConfigCat.
     *
     * @return Collection of all available feature flag keys
     */
    override suspend fun getAllKeys(): Collection<String> = configCatClient.getAllKeys()

    /**
     * Gets all feature flag values from ConfigCat.
     *
     * @return Map of all feature flag keys and their values
     */
    override suspend fun getAllValues(): Map<String, Any?> = configCatClient.getAllValues()

    /**
     * Forces a refresh of the feature flags from ConfigCat's servers.
     *
     * @return true if the refresh was successful, false otherwise
     */
    override suspend fun refresh(): Boolean = configCatClient.forceRefresh().isSuccess

    /**
     * Sets user properties for targeted feature flag evaluation in ConfigCat.
     *
     * ConfigCat uses user context for advanced targeting rules, allowing you to show different
     * feature flag values to different users based on their attributes, location, or other
     * criteria.
     *
     * @param userId The unique identifier for the user
     * @param email The user's email address (optional)
     * @param country The user's country code (optional)
     */
    override fun setUserProperties(userId: String, email: String?, country: String?) {
        val user = ConfigCatUser(identifier = userId, email = email, country = country)
        configCatClient.setDefaultUser(user)
    }
}
