package com.example.project.analytics.domain

/**
 * Custom analytics event for tracking events not covered by [CommonAnalyticsEvent].
 *
 * This class provides a flexible way to create custom analytics events for specific use cases that
 * don't fit into the predefined common events. It's useful for:
 * - Business-specific events (e.g., "purchase_completed", "subscription_started")
 * - Feature-specific tracking (e.g., "search_performed", "filter_applied")
 * - A/B testing events (e.g., "experiment_viewed", "variant_selected")
 * - Integration-specific events (e.g., "payment_method_added", "social_share")
 *
 * When creating custom events, follow these guidelines:
 * - Use descriptive, snake_case event names
 * - Include relevant context in parameters
 * - Keep parameter values serializable
 * - Document custom events for team consistency
 *
 * @param name The unique identifier for this custom event. Should follow snake_case convention and
 *   be descriptive (e.g., "user_preference_changed").
 * @param parameters Additional context data for the event. Should include relevant information for
 *   analysis and debugging.
 * @see AnalyticsEvent for the base interface
 * @see CommonAnalyticsEvent for predefined common events
 * @see AnalyticsParam for standardized parameter constants
 * @example
 *
 * ```kotlin
 * // Business event
 * val purchaseEvent = CustomAnalyticsEvent(
 *     name = "purchase_completed",
 *     parameters = mapOf(
 *         "product_id" to "premium_subscription",
 *         "price" to 9.99,
 *         "currency" to "USD",
 *         "payment_method" to "credit_card"
 *     )
 * )
 *
 * // Feature-specific event
 * val searchEvent = CustomAnalyticsEvent(
 *     name = "search_performed",
 *     parameters = mapOf(
 *         "query" to "kotlin multiplatform",
 *         "results_count" to 25,
 *         "search_type" to "global"
 *     )
 * )
 *
 * // A/B testing event
 * val experimentEvent = CustomAnalyticsEvent(
 *     name = "experiment_viewed",
 *     parameters = mapOf(
 *         "experiment_id" to "new_ui_design",
 *         "variant" to "control",
 *         "user_segment" to "premium"
 *     )
 * )
 * ```
 */
internal data class CustomAnalyticsEvent(
    override val name: String,
    override val parameters: Map<String, Any> = emptyMap(),
) : AnalyticsEvent
