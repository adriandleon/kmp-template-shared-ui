package com.example.project.analytics.domain

/**
 * Interface for analytics providers that handle the actual delivery of analytics events.
 *
 * This interface allows the analytics system to support multiple providers simultaneously, enabling
 * features like:
 * - **Provider redundancy**: Send events to multiple services for reliability
 * - **A/B testing**: Compare different analytics providers
 * - **Gradual migration**: Switch providers without losing data
 * - **Cost optimization**: Use different providers for different event types
 *
 * Common provider implementations include:
 * - Firebase Analytics
 * - Google Analytics
 * - Custom backend services
 * - Local analytics storage
 * - Third-party services (Mixpanel, Amplitude, etc.)
 *
 * @see Analytics for the main analytics interface
 * @see AnalyticsEvent for event definitions
 * @example
 *
 * ```kotlin
 * class FirebaseAnalyticsProvider : AnalyticsProvider {
 *     override fun track(event: AnalyticsEvent) {
 *         // Convert to Firebase format and send
 *         firebaseAnalytics.logEvent(event.name, event.parameters)
 *     }
 * }
 *
 * class CustomBackendProvider : AnalyticsProvider {
 *     override fun track(event: AnalyticsEvent) {
 *         // Send to custom backend API
 *         apiClient.postEvent(event.name, event.parameters)
 *     }
 * }
 * ```
 */
internal interface AnalyticsProvider {

    /**
     * Tracks an analytics event through this provider.
     *
     * This method is called by the [Analytics] implementation for each configured provider when an
     * event needs to be tracked. The provider is responsible for:
     * - Converting the event to the provider's format
     * - Handling network requests or local storage
     * - Managing errors gracefully (should not throw exceptions)
     * - Ensuring thread safety if needed
     *
     * **Important**: This method should not block the calling thread and should handle errors
     * internally without propagating them to the caller.
     *
     * @param event The analytics event to track. Must not be null.
     * @example
     *
     * ```kotlin
     * class FirebaseAnalyticsProvider : AnalyticsProvider {
     *     private val firebaseAnalytics = FirebaseAnalytics.getInstance()
     *
     *     override fun track(event: AnalyticsEvent) {
     *         try {
     *             val bundle = Bundle().apply {
     *                 event.parameters.forEach { (key, value) ->
     *                     when (value) {
     *                         is String -> putString(key, value)
     *                         is Int -> putInt(key, value)
     *                         is Long -> putLong(key, value)
     *                         is Double -> putDouble(key, value)
     *                         is Boolean -> putBoolean(key, value)
     *                         else -> putString(key, value.toString())
     *                     }
     *                 }
     *             }
     *             firebaseAnalytics.logEvent(event.name, bundle)
     *         } catch (e: Exception) {
     *             // Log error but don't propagate to avoid breaking the app
     *             Log.e("Analytics", "Failed to track event: ${event.name}", e)
     *         }
     *     }
     * }
     * ```
     */
    fun track(event: AnalyticsEvent)
}
