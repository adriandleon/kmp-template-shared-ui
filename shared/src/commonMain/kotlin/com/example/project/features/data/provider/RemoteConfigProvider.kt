package com.example.project.features.data.provider

import com.example.project.features.domain.provider.FeatureFlagProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig

/**
 * Firebase Remote Config implementation of the [FeatureFlagProvider] interface.
 *
 * This provider integrates with Firebase Remote Config, allowing the application to retrieve
 * feature flags from Firebase's cloud-based configuration service. Firebase Remote Config provides
 * real-time configuration updates and A/B testing capabilities.
 *
 * The provider supports all standard feature flag data types (boolean, string, integer, double) and
 * provides methods for bulk operations. User context is managed through Firebase Analytics for
 * targeted evaluation.
 *
 * @param remoteConfig The Firebase Remote Config instance for API communication
 * @example
 *
 * ```kotlin
 * // Create Firebase Remote Config
 * val remoteConfig = Firebase.remoteConfig
 * remoteConfig.settings { minimumFetchInterval = 5.minutes }
 *
 * // Create provider
 * val provider = RemoteConfigProvider(remoteConfig)
 *
 * // Use feature flags
 * val isEnabled = provider.getBoolean("new_feature", false)
 * val configValue = provider.getString("api_endpoint", "https://api.example.com")
 * ```
 */
internal class RemoteConfigProvider(private val remoteConfig: FirebaseRemoteConfig) :
    FeatureFlagProvider {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    /**
     * Gets a boolean feature flag value from Firebase Remote Config.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found (not used in
     *   Firebase)
     * @return The feature flag value from Remote Config
     */
    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        remoteConfig.getValue(key).asBoolean()

    /**
     * Gets a string feature flag value from Firebase Remote Config.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found (not used in
     *   Firebase)
     * @return The feature flag value from Remote Config
     */
    override suspend fun getString(key: String, defaultValue: String): String =
        remoteConfig.getValue(key).asString()

    /**
     * Gets an integer feature flag value from Firebase Remote Config.
     *
     * Note: Firebase Remote Config stores numbers as Long, so this method converts the Long value
     * to Int.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found (not used in
     *   Firebase)
     * @return The feature flag value from Remote Config converted to Int
     */
    override suspend fun getInt(key: String, defaultValue: Int): Int =
        remoteConfig.getValue(key).asLong().toInt()

    /**
     * Gets a double feature flag value from Firebase Remote Config.
     *
     * @param key The feature flag key to retrieve
     * @param defaultValue The default value to return if the flag is not found (not used in
     *   Firebase)
     * @return The feature flag value from Remote Config
     */
    override suspend fun getDouble(key: String, defaultValue: Double): Double =
        remoteConfig.getValue(key).asDouble()

    /**
     * Gets all available feature flag keys from Firebase Remote Config.
     *
     * @return Collection of all available feature flag keys
     */
    override suspend fun getAllKeys(): Collection<String> = remoteConfig.all.keys

    /**
     * Gets all feature flag values from Firebase Remote Config.
     *
     * @return Map of all feature flag keys and their values
     */
    override suspend fun getAllValues(): Map<String, Any?> = remoteConfig.all

    /**
     * Forces a refresh of the feature flags from Firebase Remote Config servers.
     *
     * @return true if the refresh was successful, false otherwise
     */
    override suspend fun refresh(): Boolean = remoteConfig.fetchAndActivate()

    /**
     * Sets user properties for targeted feature flag evaluation using Firebase Analytics.
     *
     * Firebase Remote Config uses Firebase Analytics user properties for targeting and A/B testing.
     * This method sets the user ID and custom properties that can be used in Remote Config
     * targeting rules.
     *
     * @param userId The unique identifier for the user
     * @param email The user's email address (optional)
     * @param country The user's country code (optional)
     */
    override fun setUserProperties(userId: String, email: String?, country: String?) {
        firebaseAnalytics.setUserId(userId)
        email?.let { firebaseAnalytics.setUserProperty("email", it) }
        country?.let { firebaseAnalytics.setUserProperty("country", it) }
    }
}
