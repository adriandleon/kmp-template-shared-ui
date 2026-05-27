package com.example.project.analytics.domain

/**
 * Constants for analytics event names.
 *
 * This object contains all the standardized event names used throughout the application. Using
 * these constants ensures consistency and makes it easier to maintain and refactor event names
 * across the codebase.
 *
 * Event names follow the snake_case convention and are descriptive of the user action or system
 * event being tracked.
 *
 * @see AnalyticsParam for parameter constants
 * @see CommonAnalyticsEvent for event implementations
 */
internal object AnalyticsEvents {

    /** Event fired when a user views a screen or page */
    internal const val SCREEN_VIEW: String = "screen_view"

    /** Event fired when a user clicks a button */
    internal const val BUTTON_CLICK: String = "button_click"

    /** Event fired when a user selects content (video, article, etc.) */
    internal const val SELECT_CONTENT: String = "select_content"

    /** Event fired when a user selects an item from a list */
    internal const val SELECT_ITEM: String = "select_item"

    /** Event fired when an error occurs in the application */
    internal const val ERROR: String = "error"

    /** Event fired when a user taps on any UI element */
    internal const val ELEMENT_TAP: String = "element_tap"
}

/**
 * Constants for analytics event parameters.
 *
 * This object contains all the standardized parameter keys used in analytics events. Using these
 * constants ensures consistency across different event types and makes it easier to query and
 * analyze analytics data.
 *
 * Parameter names follow the snake_case convention and are descriptive of the data they contain.
 *
 * @see AnalyticsEvents for event name constants
 * @see CommonAnalyticsEvent for event implementations
 */
internal object AnalyticsParam {

    /** The type of content being interacted with (e.g., "video", "article", "category") */
    internal const val CONTENT_TYPE: String = "content_type"

    /** The name of the screen or page being viewed */
    internal const val SCREEN_NAME: String = "screen_name"

    /** The class or component name of the screen being viewed */
    internal const val SCREEN_CLASS: String = "screen_class"

    /** The unique identifier of an item */
    internal const val ITEM_ID: String = "item_id"

    /** The unique identifier of a list containing items */
    internal const val ITEM_LIST_ID: String = "item_list_id"

    /** The display name of a list containing items */
    internal const val ITEM_LIST_NAME: String = "item_list_name"

    /** The name or identifier of a button that was clicked */
    internal const val BUTTON_NAME: String = "button_name"

    /** The name or identifier of a UI element that was tapped */
    internal const val ELEMENT_NAME: String = "element_name"

    /** The error message or description of an error that occurred */
    internal const val ERROR_MESSAGE: String = "error_message"
}
