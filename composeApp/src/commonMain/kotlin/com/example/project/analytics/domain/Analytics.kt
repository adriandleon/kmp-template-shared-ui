package com.example.project.analytics.domain


/**
 * Main analytics interface for tracking user events and app behavior.
 *
 * This interface provides a provider-agnostic way to track analytics events across the application.
 * It supports both single event tracking and batch event tracking for improved performance.
 *
 * The analytics system is designed to be:
 * - **Provider-agnostic**: Easy to switch between different analytics providers
 * - **Type-safe**: Uses strongly-typed event classes
 * - **Extensible**: Supports custom events and parameters
 * - **Performance-optimized**: Supports batch tracking for multiple events
 *
 * @see AnalyticsEvent for event definitions
 * @see AnalyticsProvider for provider implementations
 * @see CommonAnalyticsEvent for predefined common events
 * @example
 *
 * ```kotlin
 * class UserRepository(
 *     private val analytics: Analytics
 * ) {
 *     fun getUser(id: String): User? {
 *         analytics.track(
 *             CommonAnalyticsEvent.ScreenView(
 *                 screenName = "UserProfile",
 *                 screenClass = "UserRepository"
 *             )
 *         )
 *
 *         return try {
 *             // Fetch user logic
 *             analytics.track(CommonAnalyticsEvent.ButtonClick("FetchUser"))
 *             user
 *         } catch (e: Exception) {
 *             analytics.track(
 *                 CommonAnalyticsEvent.Error("Failed to fetch user: $id")
 *             )
 *             null
 *         }
 *     }
 * }
 * ```
 */
internal interface Analytics {

    /**
     * Tracks a single analytics event.
     *
     * This method sends the event to all configured analytics providers. The event is processed
     * asynchronously and should not block the main thread.
     *
     * @param event The analytics event to track. Must not be null.
     * @example
     *
     * ```kotlin
     * analytics.track(
     *     CommonAnalyticsEvent.ScreenView(
     *         screenName = "HomeScreen",
     *         screenClass = "HomeComponent"
     *     )
     * )
     * ```
     */
    fun track(event: AnalyticsEvent)

    /**
     * Tracks multiple analytics events in batch.
     *
     * This method is more efficient than calling [track] multiple times for multiple events, as it
     * can optimize the delivery to analytics providers.
     *
     * @param events The list of analytics events to track. Must not be null. Empty lists are
     *   allowed and will be ignored.
     * @example
     *
     * ```kotlin
     * val events = listOf(
     *     CommonAnalyticsEvent.ScreenView("HomeScreen", "HomeComponent"),
     *     CommonAnalyticsEvent.ButtonClick("LoginButton"),
     *     CommonAnalyticsEvent.SelectItem("item123", "Product A")
     * )
     * analytics.track(events)
     * ```
     */
    fun track(events: List<AnalyticsEvent>)
}

