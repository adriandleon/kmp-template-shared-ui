package com.example.project.analytics.domain

/**
 * Base interface for all analytics events.
 *
 * This interface defines the contract for analytics events that can be tracked by the analytics
 * system. All events must have a name and can optionally include additional parameters for context.
 *
 * The analytics system supports two types of events:
 * - **Common events**: Predefined events with standardized parameters (see [CommonAnalyticsEvent])
 * - **Custom events**: User-defined events for specific use cases (see [CustomAnalyticsEvent])
 *
 * @property name The unique identifier for this event type. Should be in snake_case format and
 *   follow a consistent naming convention across the application.
 * @property parameters Additional context data for the event. Keys should be descriptive and values
 *   should be serializable (String, Number, Boolean, etc.).
 * @see CommonAnalyticsEvent for predefined common events
 * @see CustomAnalyticsEvent for custom event implementation
 * @example
 *
 * ```kotlin
 * // Using a common event
 * val screenViewEvent = CommonAnalyticsEvent.ScreenView(
 *     screenName = "HomeScreen",
 *     screenClass = "HomeComponent"
 * )
 *
 * // Using a custom event
 * val customEvent = CustomAnalyticsEvent(
 *     name = "user_preference_changed",
 *     parameters = mapOf(
 *         "preference_key" to "theme",
 *         "old_value" to "light",
 *         "new_value" to "dark"
 *     )
 * )
 * ```
 */
internal interface AnalyticsEvent {

    /**
     * The unique identifier for this event type.
     *
     * Event names should:
     * - Use snake_case format (e.g., "screen_view", "button_click")
     * - Be descriptive and self-explanatory
     * - Follow a consistent naming convention across the app
     * - Be unique within the application scope
     *
     * @return The event name as a non-null, non-empty string
     */
    val name: String

    /**
     * Additional context data for the event.
     *
     * Parameters provide additional context about the event and should include relevant information
     * that helps with analysis and debugging.
     *
     * Parameter values should be:
     * - Serializable (String, Number, Boolean, etc.)
     * - Non-null (use empty string instead of null for optional strings)
     * - Consistent in type across similar events
     *
     * @return A map of parameter key-value pairs. Can be empty but should not be null.
     */
    val parameters: Map<String, Any>
}
