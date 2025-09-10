# Authentication Middleware for Decompose

This authentication middleware provides a comprehensive solution for managing user sessions in Decompose applications, following the patterns discussed in the [Decompose repository discussions](https://github.com/arkivanov/Decompose/discussions/151).

## 🏗️ Architecture

The middleware is built around the concept of **Custom ComponentContext** as suggested by [@arkivanov](https://github.com/arkivanov/Decompose/discussions/151#issuecomment-1192000000) and [@malliaridis](https://github.com/arkivanov/Decompose/discussions/151#issuecomment-1192000000), providing authentication context throughout the component tree.

### Key Components

1. **SessionManager** - Provider-agnostic session management
2. **AuthenticatedComponentContext** - Custom context with auth capabilities
3. **AuthGuard** - Route protection based on authentication state
4. **SessionAwareComponent** - Base interface for auth-aware components
5. **SessionListener** - Compose utilities for session state observation

## 🚀 Quick Start

### 1. Set up SessionManager

```kotlin
// Create session manager with your auth repository
val sessionManager = DefaultSessionManager(authRepository)

// Or use dependency injection
val sessionManager: SessionManager by inject()
```

### 2. Create AuthenticatedComponentContext

```kotlin
val authContext = DefaultAuthenticatedComponentContext(
    componentContext = componentContext,
    sessionManager = sessionManager,
    onRequireAuthentication = { /* Navigate to auth */ },
    onNavigateToHome = { /* Navigate to home */ }
)
```

### 3. Use AuthGuard for Route Protection

```kotlin
val authGuard = DefaultAuthGuard(
    componentContext = componentContext,
    sessionManager = sessionManager,
    createAuthComponent = { authContext -> /* Create auth component */ },
    createProtectedComponent = { authContext -> /* Create protected component */ }
)
```

### 4. Use SessionAwareComponent in Your Components

```kotlin
class MyFeatureComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    // Your component logic here
    // Access auth state via authContext.currentSession, etc.
}
```

### 5. Use SessionListener in Compose Views

```kotlin
@Composable
fun MyView(component: SessionAwareComponent) {
    SessionListener(
        component = component,
        onSessionExpired = { /* Handle session expiry */ },
        onUserSignedIn = { session -> /* Handle sign in */ },
        onUserSignedOut = { /* Handle sign out */ }
    ) {
        // Your UI content
    }
}
```

## 📋 Features

### ✅ Provider Agnostic
- Works with any authentication provider (Supabase, Firebase, custom)
- Clean abstraction over provider-specific implementations

### ✅ Automatic Route Protection
- AuthGuard automatically navigates between auth and protected content
- No need to manually check authentication state in every component

### ✅ Session State Management
- Centralized session state management
- Automatic session refresh handling
- Session expiry detection

### ✅ Compose Integration
- Easy-to-use Compose utilities
- Reactive session state observation
- Automatic UI updates on session changes

### ✅ Type Safety
- Strongly typed session states and events
- Compile-time safety for authentication operations

## 🔧 Configuration

### Session States

```kotlin
sealed interface UserSession {
    object Unauthenticated : UserSession
    data class Authenticated(
        val user: User,
        val accessToken: String,
        val refreshToken: String? = null,
        val expiresAt: Long? = null
    ) : UserSession
    object Refreshing : UserSession
    object Expired : UserSession
}
```

### Session Events

```kotlin
sealed interface SessionEvent {
    data class UserSignedIn(val session: UserSession.Authenticated) : SessionEvent
    object UserSignedOut : SessionEvent
    data class SessionRefreshed(val session: UserSession.Authenticated) : SessionEvent
    object SessionExpired : SessionEvent
    data class AuthenticationFailed(val error: AuthError) : SessionEvent
}
```

## 🎯 Usage Examples

### Basic Authentication Flow

```kotlin
// In your root component
class RootComponent(
    componentContext: ComponentContext,
    sessionManager: SessionManager,
) : ComponentContext by componentContext {
    
    private val authGuard = DefaultAuthGuard(
        componentContext = componentContext,
        sessionManager = sessionManager,
        createAuthComponent = { authContext -> 
            SignInComponent(authContext) 
        },
        createProtectedComponent = { authContext -> 
            MainAppComponent(authContext) 
        }
    )
}
```

### Protected Component

```kotlin
class ProfileComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun updateProfile() {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        // Update profile logic
        authContext.sessionManager.updateProfile(displayName = "New Name")
    }
}
```

### Compose View with Session Awareness

```kotlin
@Composable
fun ProfileView(component: SessionAwareComponent) {
    SessionProvider(component) { session ->
        when (session) {
            is UserSession.Authenticated -> {
                ProfileContent(user = session.user)
            }
            is UserSession.Unauthenticated -> {
                SignInPrompt()
            }
            // Handle other states...
        }
    }
}
```

## 🔒 Security Considerations

1. **Token Management**: Access tokens are managed securely by the SessionManager
2. **Session Refresh**: Automatic session refresh prevents token expiry issues
3. **Route Protection**: AuthGuard ensures protected routes are only accessible when authenticated
4. **Error Handling**: Comprehensive error handling for authentication failures

## 🧪 Testing

The middleware is designed to be easily testable:

```kotlin
// Mock session manager for testing
val mockSessionManager = object : SessionManager {
    override val currentSession = MutableStateFlow(UserSession.Unauthenticated)
    // ... implement other methods
}

// Test your components with mock session manager
val testComponent = MyFeatureComponent(
    componentContext = testComponentContext,
    authContext = DefaultAuthenticatedComponentContext(
        componentContext = testComponentContext,
        sessionManager = mockSessionManager,
        onRequireAuthentication = {},
        onNavigateToHome = {}
    )
)
```

## 📚 References

- [Decompose Authentication Discussion #151](https://github.com/arkivanov/Decompose/discussions/151)
- [Decompose Custom ComponentContext Documentation](https://arkivanov.github.io/Decompose/component/custom-component-context/)
- [Supabase Kotlin Documentation](https://supabase.com/docs/reference/kotlin)

## 🤝 Contributing

This middleware follows the patterns established in the Decompose community discussions and can be extended to support additional authentication providers or features as needed.