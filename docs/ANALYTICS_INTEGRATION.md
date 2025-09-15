# Analytics Integration

Table of Contents
-----------------

- [Overview](#overview)
- [Installing Firebase Analytics](#installing-firebase-analytics)
- [Usage](#usage)
  - [Basic Usage](#basic-usage)
  - [Common Events](#common-events)
  - [Event Constants](#event-constants)
- [Implementing a New Provider](#implementing-a-new-provider)
- [Best Practices](#best-practices)
  - [Event Naming](#event-naming)
  - [Parameters](#parameters)
  - [Provider Implementation](#provider-implementation)
  - [Testing](#testing)
- [Firebase Analytics Integration](#firebase-analytics-integration)
- [Dependency Injection](#dependency-injection)

## Overview

The analytics system provides a flexible way to track events across the application with support for multiple analytics providers simultaneously. The system is designed to be easy to use and extend.

## Installing Firebase Analytics

Firebase Analytics is already included in the project dependencies. The current version is defined in `gradle/libs.versions.toml`:

```toml
[versions]
firebase-gitlive = "2.3.0"

[libraries]
firebase-analytics = { group = "dev.gitlive", name = "firebase-analytics", version.ref = "firebase-gitlive" }
```

The dependency is automatically included in the `composeApp` module's `build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation(libs.firebase.analytics)
}
```

## Usage

### Basic Usage

```kotlin
// Initialize analytics with providers through dependency injection
class MyComponent(private val analytics: Analytics) {
    fun onScreenVisible() {
        analytics.track(
            CommonAnalyticsEvent.ScreenView(
                screenName = "MyScreen",
                screenClass = "MyComponent"
            )
        )
    }

    fun onButtonClick() {
        analytics.track(CommonAnalyticsEvent.ButtonClick("LoginButton"))
    }
}
```

### Common Events

The system provides several predefined common events:

- `ScreenView`: Track when a screen is viewed
  - Parameters: 
    - `screen_name`: Name of the screen
    - `screen_class`: Class name of the screen component
- `ButtonClick`: Track button clicks
  - Parameters: `button_name`
- `ElementTap`: Track when any UI element is tapped
  - Parameters: `element_name`
- `SelectContent`: Track when content is selected
  - Parameters: 
    - `item_id`: ID of the selected item
    - `content_type`: Type of content ("video", "category", "banner")
- `SelectItem`: Track when an item is selected from a list
  - Parameters:
    - `item_list_id`: ID of the selected item
    - `item_list_name`: Name of the selected item
- `Error`: Track errors
  - Parameters: `error_message`

### Event Constants

All event names and parameter keys are defined as constants to ensure consistency:

```kotlin
object AnalyticsEvents {
    const val SCREEN_VIEW = "screen_view"
    const val BUTTON_CLICK = "button_click"
    const val SELECT_CONTENT = "select_content"
    const val SELECT_ITEM = "select_item"
    const val ERROR = "error"
    const val ELEMENT_TAP = "element_tap"
}

object AnalyticsParam {
    const val CONTENT_TYPE = "content_type"
    const val SCREEN_NAME = "screen_name"
    const val SCREEN_CLASS = "screen_class"
    const val ITEM_ID = "item_id"
    const val ITEM_LIST_ID = "item_list_id"
    const val ITEM_LIST_NAME = "item_list_name"
    const val BUTTON_NAME = "button_name"
    const val ELEMENT_NAME = "element_name"
    const val ERROR_MESSAGE = "error_message"
}
```

## Implementing a New Provider

To add a new analytics provider:

1. Create a new class that implements the `AnalyticsProvider` interface:

```kotlin
class MyCustomProvider : AnalyticsProvider {
    override fun track(event: AnalyticsEvent) {
        // Implement tracking logic here
        val eventName = event.name
        val parameters = event.parameters
        
        // Send to your analytics service
        myAnalyticsService.logEvent(eventName, parameters)
    }
}
```

2. Add your provider to the analytics providers list in the DI module:

```kotlin
internal val analyticsModule = module {
    factoryOf(::analyticsProviders)
    singleOf(::AnalyticsImpl) { bind<Analytics>() }
}

private fun analyticsProviders(): List<AnalyticsProvider> = listOf(
    FirebaseAnalyticsProvider(),
    MyCustomProvider()
)
```

## Best Practices

### Event Naming

- Use snake_case for event names
- Be descriptive but concise
- Use predefined constants from `AnalyticsEvents`
- Be consistent across the app

### Parameters

- Use predefined parameter constants from `AnalyticsParam`
- Keep parameter names consistent across events
- Document custom parameters
- Use appropriate parameter types (String, Int, Long, etc.)

### Provider Implementation

- Handle errors gracefully
- Consider adding logging for debugging
- Document any provider-specific limitations
- Keep providers independent and focused

### Testing

The project includes a `TestAnalytics` implementation for testing:

```kotlin
class TestAnalytics : Analytics {
    private val _events = mutableListOf<AnalyticsEvent>()
    val events: List<AnalyticsEvent> = _events
    val lastTrackEvent: AnalyticsEvent get() = events.last()

    fun reset() {
        _events.clear()
    }

    override fun track(event: AnalyticsEvent) {
        _events.add(event)
    }

    override fun track(events: List<AnalyticsEvent>) {
        _events.addAll(events)
    }
}
```

Use it in your tests to verify analytics events:

```kotlin
val testAnalytics = TestAnalytics()

@Test
fun shouldTrackScreenView() {
    val component = MyComponent(testAnalytics)
    
    component.onScreenVisible()
    
    testAnalytics.lastTrackEvent.name shouldBeEqual "screen_view"
    testAnalytics.lastTrackEvent.parameters shouldContain ("screen_name" to "MyScreen")
}
```

## Firebase Analytics Integration

The `FirebaseAnalyticsProvider` uses the Kotlin Multiplatform Firebase SDK:

```kotlin
internal class FirebaseAnalyticsProvider : AnalyticsProvider {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun track(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.parameters)
    }
}
```

## Dependency Injection

The analytics system is set up using Koin for dependency injection:

```kotlin
internal val analyticsModule = module {
    factoryOf(::analyticsProviders)
    singleOf(::AnalyticsImpl) { bind<Analytics>() }
}

private fun analyticsProviders(): List<AnalyticsProvider> = listOf(FirebaseAnalyticsProvider())
```

To use analytics in your components, inject it through the constructor:

```kotlin
class MyComponent(
    componentContext: ComponentContext,
    private val analytics: Analytics
) : ComponentContext by componentContext {
    // Use analytics here
} 