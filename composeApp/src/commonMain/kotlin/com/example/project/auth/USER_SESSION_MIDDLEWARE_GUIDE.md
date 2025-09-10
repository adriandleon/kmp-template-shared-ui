# User Session Middleware for Compose Multiplatform with Decompose

This guide provides comprehensive instructions on how to use the user session middleware in your Compose Multiplatform application with Decompose components.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Setup](#setup)
- [Basic Usage](#basic-usage)
- [Advanced Usage](#advanced-usage)
- [Compose Integration](#compose-integration)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Examples](#examples)

## 🎯 Overview

The user session middleware provides a comprehensive solution for managing user authentication state in Decompose applications. It follows the **Custom ComponentContext** pattern and provides:

- **Provider-agnostic** authentication management
- **Automatic route protection** based on authentication state
- **Reactive session state** with StateFlow/SharedFlow
- **Compose integration** with easy-to-use utilities
- **Type-safe** session management

## 🏗️ Architecture

### Core Components

```
┌─────────────────────────────────────────────────────────────┐
│                    SessionManager                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │   Supabase      │  │   Firebase      │  │   Custom    │ │
│  │   Integration   │  │   Integration   │  │   Provider  │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│              AuthenticatedComponentContext                  │
│  • Provides auth context to all components                 │
│  • Manages session state and events                        │
│  • Handles navigation callbacks                            │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    AuthGuard                                │
│  • Automatically protects routes                           │
│  • Navigates between auth and protected content            │
│  • Handles session expiry and refresh                      │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│              SessionAwareComponent                          │
│  • Base interface for auth-aware components                │
│  • Provides session state and methods                      │
│  • Easy integration with existing components               │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Setup

### 1. Add Dependencies

Add the authentication middleware to your `build.gradle.kts`:

```kotlin
// In your commonMain source set
dependencies {
    implementation(project(":auth")) // If using a separate module
    // Or include the auth files directly in your project
}
```

### 2. Configure Dependency Injection

Set up Koin modules for dependency injection:

```kotlin
// In your Koin module setup
val appModule = module {
    // Include auth module
    includes(authModule)
    
    // Other app dependencies...
}

// Start Koin
startKoin {
    modules(appModule)
}
```

### 3. Initialize Session Manager

Create and configure your session manager:

```kotlin
// In your application setup
val sessionManager: SessionManager by inject()

// Or create manually
val sessionManager = DefaultSessionManager(authRepository)
```

## 📖 Basic Usage

### 1. Root Component Setup

Set up your root component with authentication middleware:

```kotlin
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
    
    // Your root component logic...
}
```

### 2. Create Session-Aware Components

Create components that need authentication context:

```kotlin
class ProfileComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun updateProfile() {
        if (!isAuthenticated) {
            requireAuthentication() // Automatically navigates to auth
            return
        }
        
        // Safe to access protected resources
        authContext.sessionManager.updateProfile(displayName = "New Name")
    }
    
    fun signOut() {
        authContext.sessionManager.signOut()
    }
}
```

### 3. Use in Compose Views

Integrate with your Compose UI:

```kotlin
@Composable
fun ProfileView(component: SessionAwareComponent) {
    val currentSession by component.currentSession.collectAsState()
    
    // Listen to session changes
    SessionListener(
        component = component,
        onSessionExpired = { /* Handle session expiry */ },
        onUserSignedIn = { session -> /* Handle sign in */ }
    ) {
        when (currentSession) {
            is UserSession.Authenticated -> {
                ProfileContent(user = currentSession.user)
            }
            is UserSession.Unauthenticated -> {
                SignInPrompt()
            }
            is UserSession.Refreshing -> {
                LoadingIndicator()
            }
            is UserSession.Expired -> {
                SessionExpiredMessage()
            }
        }
    }
}
```

## 🔧 Advanced Usage

### 1. Custom Session Manager

Create a custom session manager for your specific needs:

```kotlin
class CustomSessionManager(
    private val authRepository: AuthRepository,
    private val customAuthService: CustomAuthService,
) : SessionManager {
    
    // Implement SessionManager interface
    // Add custom logic for your specific authentication provider
}
```

### 2. Session State Management

Handle complex session state scenarios:

```kotlin
class AdvancedComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    init {
        // Listen to session events
        authContext.sessionEvents.collectLatest { event ->
            when (event) {
                is SessionEvent.UserSignedIn -> {
                    // Handle successful sign in
                    loadUserData()
                }
                is SessionEvent.SessionExpired -> {
                    // Handle session expiry
                    clearUserData()
                    requireAuthentication()
                }
                is SessionEvent.AuthenticationFailed -> {
                    // Handle authentication errors
                    showError(event.error)
                }
                // Handle other events...
            }
        }
    }
}
```

### 3. Route Protection

Implement fine-grained route protection:

```kotlin
class ProtectedFeatureComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
    private val requiredPermissions: List<String>,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun accessProtectedFeature() {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        if (!hasRequiredPermissions()) {
            showPermissionDenied()
            return
        }
        
        // Access protected feature
        performProtectedAction()
    }
    
    private fun hasRequiredPermissions(): Boolean {
        val user = currentUser ?: return false
        return requiredPermissions.all { permission ->
            user.permissions.contains(permission)
        }
    }
}
```

## 🎨 Compose Integration

### 1. Session Provider

Use the SessionProvider for easy session state access:

```kotlin
@Composable
fun MyScreen(component: SessionAwareComponent) {
    SessionProvider(component) { session ->
        when (session) {
            is UserSession.Authenticated -> {
                AuthenticatedContent(session.user)
            }
            is UserSession.Unauthenticated -> {
                UnauthenticatedContent()
            }
            // Handle other states...
        }
    }
}
```

### 2. Session Listener

Handle session changes reactively:

```kotlin
@Composable
fun MyView(component: SessionAwareComponent) {
    SessionListener(
        component = component,
        onSessionExpired = {
            // Navigate to login
            component.requireAuthentication()
        },
        onUserSignedIn = { session ->
            // Navigate to home
            component.navigateToHome()
        },
        onAuthenticationFailed = { error ->
            // Show error message
            showSnackbar("Authentication failed: ${error.message}")
        }
    ) {
        // Your UI content
    }
}
```

### 3. Reactive State Updates

Use StateFlow for reactive UI updates:

```kotlin
@Composable
fun UserProfile(component: SessionAwareComponent) {
    val currentUser by component.currentSession
        .map { session -> (session as? UserSession.Authenticated)?.user }
        .collectAsState(initial = null)
    
    currentUser?.let { user ->
        UserProfileContent(user = user)
    } ?: SignInPrompt()
}
```

## 🧪 Testing

### 1. Mock Session Manager

Create mock implementations for testing:

```kotlin
class MockSessionManager : SessionManager {
    private val _currentSession = MutableStateFlow<UserSession>(UserSession.Unauthenticated)
    override val currentSession: StateFlow<UserSession> = _currentSession.asStateFlow()
    
    override val sessionEvents: SharedFlow<SessionEvent> = MutableSharedFlow()
    override val isAuthenticated: Boolean = false
    override val currentUser: User? = null
    override val accessToken: String? = null
    
    // Implement other methods...
}
```

### 2. Test Components

Test your session-aware components:

```kotlin
class ProfileComponentTest {
    @Test
    fun `should require authentication when not signed in`() {
        val mockAuthContext = createMockAuthContext(isAuthenticated = false)
        val component = ProfileComponent(componentContext, mockAuthContext)
        
        component.updateProfile()
        
        verify(mockAuthContext).requireAuthentication()
    }
    
    @Test
    fun `should update profile when authenticated`() {
        val mockAuthContext = createMockAuthContext(isAuthenticated = true)
        val component = ProfileComponent(componentContext, mockAuthContext)
        
        component.updateProfile()
        
        verify(mockAuthContext.sessionManager).updateProfile(any())
    }
}
```

### 3. Test Compose Views

Test your Compose UI with session state:

```kotlin
@Test
fun `should show sign in prompt when unauthenticated`() {
    val mockComponent = createMockSessionAwareComponent(
        session = UserSession.Unauthenticated
    )
    
    composeTestRule.setContent {
        ProfileView(mockComponent)
    }
    
    composeTestRule.onNodeWithText("Please sign in").assertIsDisplayed()
}
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Session State Not Updating

**Problem**: UI not reacting to session changes

**Solution**: Ensure you're using `collectAsState()` for StateFlow:

```kotlin
// ❌ Wrong
val session by component.currentSession.subscribeAsState()

// ✅ Correct
val session by component.currentSession.collectAsState()
```

#### 2. Navigation Not Working

**Problem**: `requireAuthentication()` not navigating

**Solution**: Ensure navigation callbacks are properly set up:

```kotlin
val authContext = DefaultAuthenticatedComponentContext(
    componentContext = componentContext,
    sessionManager = sessionManager,
    onRequireAuthentication = { /* Navigate to auth */ },
    onNavigateToHome = { /* Navigate to home */ }
)
```

#### 3. Session Expiry Not Handled

**Problem**: App doesn't react to session expiry

**Solution**: Use SessionListener to handle session events:

```kotlin
SessionListener(
    component = component,
    onSessionExpired = { /* Handle expiry */ }
) {
    // Your UI
}
```

### Debug Tips

1. **Enable Logging**: Add logging to track session state changes
2. **Use Debug Builds**: Test with debug builds to see detailed error messages
3. **Check State Flow**: Verify StateFlow is emitting values correctly
4. **Test Navigation**: Ensure navigation callbacks are working

## 📚 Examples

### Complete Example: User Profile Screen

```kotlin
// Component
class UserProfileComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun updateDisplayName(name: String) {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        authContext.sessionManager.updateProfile(displayName = name)
    }
    
    fun signOut() {
        authContext.sessionManager.signOut()
    }
}

// View
@Composable
fun UserProfileView(component: UserProfileComponent) {
    val currentSession by component.currentSession.collectAsState()
    
    SessionListener(
        component = component,
        onSessionExpired = { component.requireAuthentication() }
    ) {
        when (val session = currentSession) {
            is UserSession.Authenticated -> {
                UserProfileContent(
                    user = session.user,
                    onUpdateName = component::updateDisplayName,
                    onSignOut = component::signOut
                )
            }
            is UserSession.Unauthenticated -> {
                SignInPrompt()
            }
            is UserSession.Refreshing -> {
                LoadingIndicator()
            }
            is UserSession.Expired -> {
                SessionExpiredMessage()
            }
        }
    }
}

@Composable
private fun UserProfileContent(
    user: User,
    onUpdateName: (String) -> Unit,
    onSignOut: () -> Unit
) {
    Column {
        Text("Welcome, ${user.displayName ?: user.email}!")
        
        Button(onClick = { onUpdateName("New Name") }) {
            Text("Update Name")
        }
        
        Button(onClick = onSignOut) {
            Text("Sign Out")
        }
    }
}
```

### Example: Protected Feature Access

```kotlin
class ProtectedFeatureComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    fun accessFeature() {
        when {
            !isAuthenticated -> {
                requireAuthentication()
            }
            currentUser?.isPremium != true -> {
                showUpgradePrompt()
            }
            else -> {
                performFeatureAction()
            }
        }
    }
    
    private fun performFeatureAction() {
        // Access premium feature
    }
    
    private fun showUpgradePrompt() {
        // Show upgrade prompt
    }
}
```

## 🎯 Best Practices

1. **Always Check Authentication**: Verify authentication state before accessing protected resources
2. **Handle All Session States**: Account for all possible session states in your UI
3. **Use SessionListener**: Leverage SessionListener for automatic session change handling
4. **Test Thoroughly**: Write comprehensive tests for all authentication scenarios
5. **Error Handling**: Implement proper error handling for authentication failures
6. **Performance**: Use StateFlow efficiently to avoid unnecessary recompositions

## 📖 API Reference

### SessionManager

```kotlin
interface SessionManager {
    val currentSession: StateFlow<UserSession>
    val sessionEvents: SharedFlow<SessionEvent>
    val isAuthenticated: Boolean
    val currentUser: User?
    val accessToken: String?
    
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, displayName: String?): Result<Unit>
    suspend fun signOut(): Result<Unit>
    // ... other methods
}
```

### SessionAwareComponent

```kotlin
interface SessionAwareComponent : ComponentContext {
    val currentSession: StateFlow<UserSession>
    val sessionEvents: SharedFlow<SessionEvent>
    val isAuthenticated: Boolean
    val currentUser: User?
    val accessToken: String?
    
    fun requireAuthentication()
    fun navigateToHome()
}
```

### UserSession

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

This middleware provides a robust, type-safe, and easy-to-use solution for managing user authentication in your Compose Multiplatform application with Decompose components.
