package com.example.project.analytics.data.provider

import com.example.project.analytics.domain.AnalyticsEvent
import com.example.project.analytics.domain.AnalyticsProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics

/**
 * Firebase Analytics provider implementation for tracking analytics events.
 *
 * This provider integrates with Firebase Analytics to track user events and app behavior. It uses
 * the GitLive Firebase Kotlin SDK for cross-platform compatibility, allowing the same analytics
 * code to work on both Android and iOS platforms.
 *
 * **Features:**
 * - Cross-platform Firebase Analytics integration
 * - Direct event forwarding with minimal overhead
 * - Built-in Firebase parameter handling
 * - Real-time event tracking
 *
 * **Prerequisites:**
 * - Firebase project configured with Analytics enabled
 * - `google-services.json` (Android) and `GoogleService-Info.plist` (iOS) files
 * - Firebase Analytics initialized in the app
 *
 * @see AnalyticsProvider for the base interface
 * @see AnalyticsEvent for event definitions
 * @example
 *
 * ```kotlin
 * // The provider is automatically configured in AnalyticsModule
 * val analytics: Analytics = koinInject()
 *
 * // Track events - they will be sent to Firebase Analytics
 * analytics.track(
 *     CommonAnalyticsEvent.ScreenView(
 *         screenName = "HomeScreen",
 *         screenClass = "HomeComponent"
 *     )
 * )
 *
 * // Custom events are also supported
 * analytics.track(
 *     CustomAnalyticsEvent(
 *         name = "user_preference_changed",
 *         parameters = mapOf(
 *             "preference_key" to "theme",
 *             "new_value" to "dark"
 *         )
 *     )
 * )
 * ```
 */
internal class FirebaseAnalyticsProvider : AnalyticsProvider {

    /**
     * Firebase Analytics instance for cross-platform event tracking.
     *
     * This instance is automatically initialized using the GitLive Firebase SDK, which provides a
     * unified API for both Android and iOS platforms.
     */
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    /**
     * Tracks an analytics event through Firebase Analytics.
     *
     * This method sends the analytics event directly to Firebase Analytics using the GitLive
     * Firebase SDK. The event parameters are passed through as-is, relying on Firebase's built-in
     * parameter handling and type conversion.
     *
     * **Implementation Notes:**
     * - Event parameters are passed directly to Firebase Analytics
     * - Firebase handles parameter type conversion internally
     * - No additional error handling is performed at this level
     * - The GitLive SDK provides cross-platform compatibility
     *
     * @param event The analytics event to track. Must not be null.
     * @example
     *
     * ```kotlin
     * val event = CommonAnalyticsEvent.ButtonClick("LoginButton")
     * firebaseProvider.track(event)
     * // Event will appear in Firebase Analytics as "button_click" with parameter "button_name"
     * ```
     */
    override fun track(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.parameters)
    }
}
