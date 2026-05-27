package com.example.project.analytics.domain

/**
 * Base class for common analytics events used throughout the application.
 *
 * This sealed class provides a comprehensive set of predefined analytics events that cover the most
 * common user interactions and system events. Using these predefined events ensures consistency
 * across the application and makes it easier to analyze user behavior patterns.
 *
 * All events in this class:
 * - Use standardized parameter names from [AnalyticsParam]
 * - Follow consistent naming conventions
 * - Include relevant context information
 * - Are optimized for common analytics queries
 *
 * For custom events not covered by these common events, use [CustomAnalyticsEvent].
 *
 * @see AnalyticsEvent for the base interface
 * @see CustomAnalyticsEvent for custom event implementation
 * @see AnalyticsParam for parameter constants
 * @example
 *
 * ```kotlin
 * // Track screen views
 * analytics.track(
 *     CommonAnalyticsEvent.ScreenView(
 *         screenName = "HomeScreen",
 *         screenClass = "HomeComponent"
 *     )
 * )
 *
 * // Track user interactions
 * analytics.track(CommonAnalyticsEvent.ButtonClick("LoginButton"))
 * analytics.track(CommonAnalyticsEvent.SelectItem("item123", "Product A"))
 *
 * // Track errors
 * analytics.track(CommonAnalyticsEvent.Error("Network request failed"))
 * ```
 */
internal sealed class CommonAnalyticsEvent : AnalyticsEvent {

    /**
     * Event fired when a user views a screen or page.
     *
     * This event is typically fired when:
     * - A user navigates to a new screen
     * - A screen becomes visible to the user
     * - A page loads in a web view
     *
     * @param screenName The name of the screen being viewed. Should be descriptive and consistent
     *   across the app (e.g., "HomeScreen", "UserProfile").
     * @param screenClass The class or component name of the screen. Used for grouping similar
     *   screens together (e.g., "HomeComponent", "ProfileComponent"). Defaults to empty string if
     *   not specified.
     * @param parameters Additional parameters for the event. Can be used to include extra context
     *   like user ID, session info, etc.
     * @example
     *
     * ```kotlin
     * // Basic screen view
     * analytics.track(
     *     CommonAnalyticsEvent.ScreenView(
     *         screenName = "HomeScreen",
     *         screenClass = "HomeComponent"
     *     )
     * )
     *
     * // Screen view with additional context
     * analytics.track(
     *     CommonAnalyticsEvent.ScreenView(
     *         screenName = "UserProfile",
     *         screenClass = "ProfileComponent",
     *         parameters = mapOf(
     *             "user_id" to userId,
     *             "profile_type" to "public"
     *         )
     *     )
     * )
     * ```
     */
    data class ScreenView(
        val screenName: String,
        val screenClass: String = "",
        override val parameters: Map<String, Any> =
            mapOf(
                AnalyticsParam.SCREEN_NAME to screenName,
                AnalyticsParam.SCREEN_CLASS to screenClass,
            ),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.SCREEN_VIEW
    }

    /**
     * Event fired when a user clicks a button.
     *
     * This event is typically fired when:
     * - A user taps a button in the UI
     * - A user performs an action that triggers a button click
     * - A programmatic button click occurs
     *
     * @param buttonName The name or identifier of the button that was clicked. Should be
     *   descriptive and consistent (e.g., "LoginButton", "SaveButton").
     * @param parameters Additional parameters for the event. Can include context like button
     *   location, user state, etc.
     * @example
     *
     * ```kotlin
     * // Basic button click
     * analytics.track(CommonAnalyticsEvent.ButtonClick("LoginButton"))
     *
     * // Button click with context
     * analytics.track(
     *     CommonAnalyticsEvent.ButtonClick(
     *         buttonName = "SaveButton",
     *         parameters = mapOf(
     *             "form_type" to "user_profile",
     *             "validation_errors" to 0
     *         )
     *     )
     * )
     * ```
     */
    data class ButtonClick(
        val buttonName: String,
        override val parameters: Map<String, Any> = mapOf(AnalyticsParam.BUTTON_NAME to buttonName),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.BUTTON_CLICK
    }

    /**
     * Event fired when a user taps on any UI element.
     *
     * This event is typically fired when:
     * - A user taps on a non-button UI element
     * - A user interacts with a custom component
     * - A user performs a gesture on an element
     *
     * @param elementName The name or identifier of the element that was tapped. Should be
     *   descriptive and consistent (e.g., "ProductCard", "MenuIcon").
     * @param parameters Additional parameters for the event. Can include element position,
     *   interaction type, etc.
     * @example
     *
     * ```kotlin
     * // Basic element tap
     * analytics.track(CommonAnalyticsEvent.ElementTap("ProductCard"))
     *
     * // Element tap with context
     * analytics.track(
     *     CommonAnalyticsEvent.ElementTap(
     *         elementName = "VideoThumbnail",
     *         parameters = mapOf(
     *             "video_id" to videoId,
     *             "position" to "home_feed"
     *         )
     *     )
     * )
     * ```
     */
    data class ElementTap(
        val elementName: String,
        override val parameters: Map<String, Any> =
            mapOf(AnalyticsParam.ELEMENT_NAME to elementName),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.ELEMENT_TAP
    }

    /**
     * Event fired when a user selects an item from a list or collection.
     *
     * This event is typically fired when:
     * - A user selects an item from a dropdown menu
     * - A user chooses an option from a list
     * - A user picks an item from a grid or carousel
     *
     * @param itemId The unique identifier of the selected item. Should be consistent across the
     *   application for the same item.
     * @param itemName The display name of the selected item. Used for human-readable analytics
     *   reports and debugging.
     * @param parameters Additional parameters for the event. Can include list context, selection
     *   method, etc.
     * @example
     *
     * ```kotlin
     * // Basic item selection
     * analytics.track(
     *     CommonAnalyticsEvent.SelectItem(
     *         itemId = "category_123",
     *         itemName = "Technology"
     *     )
     * )
     *
     * // Item selection with context
     * analytics.track(
     *     CommonAnalyticsEvent.SelectItem(
     *         itemId = "product_456",
     *         itemName = "Smartphone",
     *         parameters = mapOf(
     *             "list_type" to "search_results",
     *             "position" to 3,
     *             "search_query" to "smartphone"
     *         )
     *     )
     * )
     * ```
     */
    data class SelectItem(
        val itemId: String,
        val itemName: String,
        override val parameters: Map<String, Any> =
            mapOf(AnalyticsParam.ITEM_LIST_ID to itemId, AnalyticsParam.ITEM_LIST_NAME to itemName),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.SELECT_ITEM
    }

    /**
     * Event fired when a user selects a video for playback.
     *
     * This event is typically fired when:
     * - A user taps on a video thumbnail
     * - A user selects a video from a list
     * - A user starts video playback
     *
     * @param videoId The unique identifier of the selected video.
     * @param parameters Additional parameters for the event. Can include video metadata, user
     *   context, etc.
     * @example
     *
     * ```kotlin
     * analytics.track(
     *     CommonAnalyticsEvent.SelectVideo(
     *         videoId = 12345L,
     *         parameters = mapOf(
     *             "source" to "home_feed",
     *             "position" to 2
     *         )
     *     )
     * )
     * ```
     */
    data class SelectVideo(
        val videoId: Long,
        override val parameters: Map<String, Any> =
            mapOf(
                AnalyticsParam.ITEM_ID to videoId.toString(),
                AnalyticsParam.CONTENT_TYPE to "video",
            ),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.SELECT_CONTENT
    }

    /**
     * Event fired when a user selects a category for browsing.
     *
     * This event is typically fired when:
     * - A user taps on a category tab
     * - A user selects a category from a menu
     * - A user filters content by category
     *
     * @param categoryId The unique identifier of the selected category.
     * @param parameters Additional parameters for the event. Can include category metadata, user
     *   preferences, etc.
     * @example
     *
     * ```kotlin
     * analytics.track(
     *     CommonAnalyticsEvent.SelectCategory(
     *         categoryId = 42,
     *         parameters = mapOf(
     *             "category_name" to "Technology",
     *             "source" to "main_menu"
     *         )
     *     )
     * )
     * ```
     */
    data class SelectCategory(
        val categoryId: Int,
        override val parameters: Map<String, Any> =
            mapOf(
                AnalyticsParam.ITEM_ID to categoryId.toString(),
                AnalyticsParam.CONTENT_TYPE to "category",
            ),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.SELECT_CONTENT
    }

    /**
     * Event fired when a user interacts with a banner or promotional content.
     *
     * This event is typically fired when:
     * - A user taps on a promotional banner
     * - A user clicks on an advertisement
     * - A user interacts with marketing content
     *
     * @param bannerId The unique identifier of the selected banner.
     * @param parameters Additional parameters for the event. Can include banner metadata, campaign
     *   info, etc.
     * @example
     *
     * ```kotlin
     * analytics.track(
     *     CommonAnalyticsEvent.SelectBanner(
     *         bannerId = 789L,
     *         parameters = mapOf(
     *             "campaign_id" to "summer_sale",
     *             "banner_position" to "top"
     *         )
     *     )
     * )
     * ```
     */
    data class SelectBanner(
        val bannerId: Long,
        override val parameters: Map<String, Any> =
            mapOf(
                AnalyticsParam.ITEM_ID to bannerId.toString(),
                AnalyticsParam.CONTENT_TYPE to "banner",
            ),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.SELECT_CONTENT
    }

    /**
     * Event fired when an error occurs in the application.
     *
     * This event is typically fired when:
     * - A network request fails
     * - A user action results in an error
     * - An unexpected exception occurs
     * - A validation error happens
     *
     * @param message The error message or description. Should be descriptive enough for debugging
     *   but not expose sensitive information.
     * @param parameters Additional parameters for the event. Can include error context, user
     *   actions leading to the error, etc.
     * @example
     *
     * ```kotlin
     * // Basic error tracking
     * analytics.track(CommonAnalyticsEvent.Error("Network request failed"))
     *
     * // Error with context
     * analytics.track(
     *     CommonAnalyticsEvent.Error(
     *         message = "Failed to load user profile",
     *         parameters = mapOf(
     *             "user_id" to userId,
     *             "error_code" to 404,
     *             "retry_count" to 2
     *         )
     *     )
     * )
     * ```
     */
    data class Error(
        val message: String,
        override val parameters: Map<String, Any> = mapOf(AnalyticsParam.ERROR_MESSAGE to message),
    ) : CommonAnalyticsEvent() {
        override val name: String = AnalyticsEvents.ERROR
    }
}
