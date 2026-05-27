# Quick Start Guide - User Session Middleware

## 🚀 Get Started in 5 Minutes

### 1. Setup (2 minutes)

```kotlin
// Add to your Koin module
val authModule = module {
    singleOf(::DefaultSessionManager) { bind<SessionManager>() }
}

// Start Koin
startKoin {
    modules(authModule)
}
```

### 2. Root Component (1 minute)

```kotlin
class RootComponent(
    componentContext: ComponentContext,
    sessionManager: SessionManager,
) : ComponentContext by componentContext {
    
    private val authGuard = DefaultAuthGuard(
        componentContext = componentContext,
        sessionManager = sessionManager,
        createAuthComponent = { authContext -> SignInComponent(authContext) },
        createProtectedComponent = { authContext -> MainAppComponent(authContext) }
    )
}
```

### 3. Create Session-Aware Component (1 minute)

```kotlin
class MyFeatureComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun doSomething() {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        // Safe to access protected resources
        val user = currentUser
        // Do something with user...
    }
}
```

### 4. Use in Compose View (1 minute)

```kotlin
@Composable
fun MyView(component: SessionAwareComponent) {
    val currentSession by component.currentSession.collectAsState()
    
    when (currentSession) {
        is UserSession.Authenticated -> {
            Text("Welcome, ${currentSession.user.displayName}!")
        }
        is UserSession.Unauthenticated -> {
            Text("Please sign in")
        }
        is UserSession.Refreshing -> {
            CircularProgressIndicator()
        }
        is UserSession.Expired -> {
            Text("Session expired, please sign in again")
        }
    }
}
```

## 🎯 Common Patterns

### Check Authentication
```kotlin
if (!isAuthenticated) {
    requireAuthentication()
    return
}
```

### Access Current User
```kotlin
val user = currentUser ?: return
// Use user...
```

### Listen to Session Changes
```kotlin
SessionListener(
    component = component,
    onSessionExpired = { /* Handle expiry */ },
    onUserSignedIn = { /* Handle sign in */ }
) {
    // Your UI
}
```

### Sign Out
```kotlin
authContext.sessionManager.signOut()
```

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| UI not updating | Use `collectAsState()` not `subscribeAsState()` |
| Navigation not working | Check navigation callbacks are set |
| Session not persisting | Ensure SessionManager is properly configured |

## 📚 Next Steps

- Read the [Full Documentation](USER_SESSION_MIDDLEWARE_GUIDE.md)
- Check out the [Examples](USER_SESSION_MIDDLEWARE_GUIDE.md#examples)
- Learn about [Testing](USER_SESSION_MIDDLEWARE_GUIDE.md#testing)

That's it! You're ready to use the user session middleware in your Compose Multiplatform app. 🎉

