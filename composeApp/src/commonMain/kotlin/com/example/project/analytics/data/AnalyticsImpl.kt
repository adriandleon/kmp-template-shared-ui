package com.example.project.analytics.data

import com.example.project.analytics.domain.Analytics
import com.example.project.analytics.domain.AnalyticsEvent
import com.example.project.analytics.domain.AnalyticsProvider

/**
 * Main analytics implementation that supports multiple providers simultaneously.
 *
 * This implementation acts as a coordinator that forwards analytics events to all configured
 * providers. It provides several benefits:
 * - **Provider redundancy**: Events are sent to multiple services for reliability
 * - **A/B testing**: Compare different analytics providers
 * - **Gradual migration**: Switch providers without losing data
 * - **Cost optimization**: Use different providers for different event types
 *
 * The implementation is designed to be:
 * - **Thread-safe**: Can be called from any thread
 * - **Non-blocking**: Provider failures don't affect the main application
 * - **Efficient**: Optimized for batch event processing
 * - **Extensible**: Easy to add new providers
 *
 * @param providers The list of analytics providers to use. Each provider will receive all events.
 *   Providers should handle errors gracefully internally.
 * @see Analytics for the interface contract
 * @see AnalyticsProvider for provider implementations
 * @example
 *
 * ```kotlin
 * // Create analytics with multiple providers
 * val analytics = AnalyticsImpl(
 *     providers = listOf(
 *         FirebaseAnalyticsProvider(),
 *         CustomBackendProvider(),
 *         LocalStorageProvider()
 *     )
 * )
 *
 * // Track events - will be sent to all providers
 * analytics.track(CommonAnalyticsEvent.ScreenView("HomeScreen", "HomeComponent"))
 *
 * // Track multiple events efficiently
 * val events = listOf(
 *     CommonAnalyticsEvent.ButtonClick("LoginButton"),
 *     CommonAnalyticsEvent.SelectItem("item123", "Product A")
 * )
 * analytics.track(events)
 * ```
 */
internal class AnalyticsImpl(private val providers: List<AnalyticsProvider>) : Analytics {

    /**
     * Tracks a single analytics event by forwarding it to all configured providers.
     *
     * This method iterates through all providers and calls their [AnalyticsProvider.track] method.
     * The implementation is simplified to provide minimal overhead - providers are responsible for
     * handling errors internally and should not propagate exceptions to avoid affecting other
     * providers or the main application.
     *
     * @param event The analytics event to track. Must not be null.
     */
    override fun track(event: AnalyticsEvent) {
        providers.forEach { provider -> provider.track(event) }
    }

    /**
     * Tracks multiple analytics events efficiently by forwarding each event to all providers.
     *
     * This method processes events in sequence, sending each event to all providers. The
     * implementation is optimized for simplicity and performance. For better performance with large
     * batches, consider implementing batch processing in individual providers.
     *
     * @param events The list of analytics events to track. Must not be null. Empty lists are
     *   allowed and will be ignored.
     */
    override fun track(events: List<AnalyticsEvent>) {
        events.forEach { event -> track(event) }
    }
}
