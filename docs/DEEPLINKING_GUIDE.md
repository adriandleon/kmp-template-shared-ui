# Deeplinking Guide

Complete guide for implementing deeplinks in Compose Multiplatform projects targeting Android and iOS.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Platform Setup](#platform-setup)
- [Implementation](#implementation)
- [Adding New Deeplinks](#adding-new-deeplinks)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

## Overview

Deeplinking allows users to navigate directly to specific content within the app via URLs. This project supports both custom URL schemes and Universal Links.

### Supported URL Formats

- **Custom Schemes**: `example://tabs/home?itemId=123`
- **Universal Links**: `https://yourapp.com/tabs/home?itemId=123`

## Architecture

```
Platform (iOS/Android) → ExternalUriHandler → RootContent → RootComponent → Navigation
```

### Key Components

1. **ExternalUriHandler**: Singleton that manages URI caching and listener registration
2. **RootContent**: Compose UI that sets up the deeplink listener
3. **RootComponent**: Handles deeplink processing and navigation
4. **Platform-specific capture**: iOS `onOpenURL` / Android `handleDeepLink`

## Platform Setup

### Android

#### 1. AndroidManifest.xml
Add intent filters to your `AndroidManifest.xml`:

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:launchMode="singleTop">
    
    <!-- Custom URL schemes -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="example" />
    </intent-filter>
    
    <!-- Universal Links -->
    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="https" android:host="yourapp.com" />
    </intent-filter>
</activity>
```

#### 2. MainActivity.kt
The Android implementation uses Decompose's built-in `handleDeepLink`:

```kotlin
val root = handleDeepLink { uri ->
    val deepLink = Url(uri.toString())
    val initialItemId = uri?.extractInitialItemId()
    retainedComponent(discardSavedState = initialItemId != null) {
        KoinSetup.createRootComponent(it, deepLink)
    }
} ?: return
```

### iOS

#### 1. Info.plist
Add URL schemes to your `Info.plist`:

```xml
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>example</string>
            <string>cmptemplate</string>
        </array>
        <key>CFBundleURLName</key>
        <string>template</string>
    </dict>
    <dict>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>https</string>
        </array>
        <key>CFBundleURLName</key>
        <string>universal-links</string>
    </dict>
</array>
```

#### 2. CMP-Template.swift
Use SwiftUI's `onOpenURL` to capture deeplinks:

```swift
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}
```

## Implementation

### 1. RootComponent Interface
Add `handleDeepLink` method to your `RootComponent`:

```kotlin
interface RootComponent : BackHandlerOwner {
    val slot: Value<ChildSlot<*, Child>>
    fun onNavigateToHome()
    fun handleDeepLink(deepLinkUrl: Url)
}
```

### 2. RootComponent Implementation
Implement deeplink handling in `DefaultRootComponent`:

```kotlin
override fun handleDeepLink(deepLinkUrl: Url) {
    val (path, childUrl) = deepLinkUrl.consumePathSegment()
    when (path) {
        pathSegmentOf<Auth>() -> navigation.activate(Auth)
        pathSegmentOf<Onboarding>() -> navigation.activate(Onboarding)
        pathSegmentOf<Tabs>() -> navigation.activate(Tabs(deepLinkUrl = childUrl))
        else -> navigation.activate(Tabs(deepLinkUrl = deepLinkUrl))
    }
}
```

### 3. RootContent Integration
Set up the deeplink listener in `RootContent`:

```kotlin
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            component.handleDeepLink(deepLinkUrl = Url(uri))
        }
        onDispose { ExternalUriHandler.listener = null }
    }
    // ... rest of UI
}
```

## Adding New Deeplinks

### 1. Update Navigation Logic
Add new cases to `DefaultRootComponent.handleDeepLink()`:

```kotlin
override fun handleDeepLink(deepLinkUrl: Url) {
    val (path, childUrl) = deepLinkUrl.consumePathSegment()
    when (path) {
        // Existing cases...
        "profile" -> {
            val userId = deepLinkUrl.parameters["userId"]
            navigation.activate(Tabs(deepLinkUrl = childUrl))
            // Additional navigation logic
        }
        "settings" -> {
            navigation.activate(Tabs(deepLinkUrl = childUrl))
        }
        else -> navigation.activate(Tabs(deepLinkUrl = deepLinkUrl))
    }
}
```

### 2. Add URL Parsing Utilities (Optional)
Create extension functions for complex URL parsing:

```kotlin
fun Url.extractUserId(): String? = parameters["userId"]
fun Url.extractItemId(): Long? = parameters["itemId"]?.toLongOrNull()
```

### 3. Update Platform Configurations
Add new URL schemes to platform-specific files if needed.

## Testing

### Android Testing
```bash
# Custom URL schemes
adb shell am start -W -a android.intent.action.VIEW -d "example://tabs/home?itemId=123" com.example.project

# Universal Links
adb shell am start -W -a android.intent.action.VIEW -d "https://yourapp.com/tabs/home?itemId=123" com.example.project
```

### iOS Testing
```bash
# Custom URL schemes
xcrun simctl openurl booted "example://tabs/home?itemId=123"
xcrun simctl openurl booted "cmptemplate://auth"

# Universal Links
xcrun simctl openurl booted "https://yourapp.com/tabs/home?itemId=123"
```

### Debug Logs
Look for these console logs:
- `ExternalUriHandler: Received URI: ...`
- `RootContent: Received URI: ...`

## Troubleshooting

| Issue | Solution |
|-------|----------|
| iOS deeplinks not working | Check `Info.plist` URL schemes and `onOpenURL` setup |
| Android deeplinks not working | Verify `AndroidManifest.xml` intent filters and `android:exported="true"` |
| Navigation not working | Check `RootComponent.handleDeepLink()` implementation |
| Universal Links not working | Verify domain association and `android:autoVerify="true"` |

### Common Issues

**Deeplinks not captured on iOS:**
- Verify `Info.plist` configuration
- Check `onOpenURL` is properly set up
- Ensure app is not running in background

**Deeplinks not captured on Android:**
- Check `AndroidManifest.xml` intent filters
- Verify `android:exported="true"` is set
- Check `android:launchMode` configuration

**Navigation fails after deeplink capture:**
- Verify `RootComponent.handleDeepLink()` implementation
- Check URL parsing logic
- Ensure Decompose navigation is properly configured

## Best Practices

### URL Design
- Use consistent URL structure
- Include meaningful path segments
- Use query parameters for data
- Keep URLs short and memorable

### Error Handling
- Always provide fallback navigation
- Handle malformed URLs gracefully
- Log deeplink processing for debugging

### Security
- Validate URL parameters
- Sanitize user input
- Don't trust external URLs blindly

### Testing
- Test on both platforms
- Test with app in different states (background, foreground, closed)
- Test with various URL formats
- Test error scenarios

## Quick Reference

### Setup Checklist
- [ ] Android: Add intent filters to `AndroidManifest.xml`
- [ ] iOS: Add URL schemes to `Info.plist`
- [ ] Shared: Implement `handleDeepLink` in `RootComponent`
- [ ] Shared: Set up `ExternalUriHandler` listener in `RootContent`

### Testing Commands
```bash
# Android
adb shell am start -W -a android.intent.action.VIEW -d "example://tabs/home?itemId=123" com.example.project

# iOS
xcrun simctl openurl booted "example://tabs/home?itemId=123"
```

### Common URL Schemes
```xml
<!-- Android: AndroidManifest.xml -->
<data android:scheme="example" />

<!-- iOS: Info.plist -->
<string>example</string>
```

This guide provides everything you need to implement, test, and maintain deeplinks in your Compose Multiplatform project.