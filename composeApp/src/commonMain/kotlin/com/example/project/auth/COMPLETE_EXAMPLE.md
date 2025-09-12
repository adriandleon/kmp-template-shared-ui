# Complete Example - User Session Middleware

This example shows a complete implementation of the user session middleware in a Compose Multiplatform app with Decompose.

## 📁 Project Structure

```
src/commonMain/kotlin/com/example/project/
├── auth/
│   ├── domain/
│   │   ├── SessionManager.kt
│   │   ├── DefaultSessionManager.kt
│   │   └── entity/
│   │       ├── UserSession.kt
│   │       └── User.kt
│   ├── presentation/
│   │   ├── context/
│   │   │   └── AuthenticatedComponentContext.kt
│   │   ├── guard/
│   │   │   └── AuthGuard.kt
│   │   ├── component/
│   │   │   └── SessionAwareComponent.kt
│   │   └── view/
│   │       ├── SessionListener.kt
│   │       └── AuthExampleView.kt
│   └── di/
│       └── AuthModule.kt
├── app/
│   ├── RootComponent.kt
│   └── MainApp.kt
└── features/
    ├── profile/
    │   ├── ProfileComponent.kt
    │   └── ProfileView.kt
    └── settings/
        ├── SettingsComponent.kt
        └── SettingsView.kt
```

## 🏗️ Complete Implementation

### 1. Root Component with Authentication

```kotlin
// app/RootComponent.kt
interface RootComponent : ComponentContext {
    val slot: Value<ChildSlot<*, Child>>
    
    sealed class Child {
        data class AuthGuard(val component: AuthGuard) : Child()
        data class MainApp(val component: MainAppComponent) : Child()
    }
}

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
) : RootComponent, ComponentContext by componentContext {
    
    private val navigation = SlotNavigation<Configuration>()
    
    override val slot: Value<ChildSlot<*, Child>> = childSlot(
        source = navigation,
        serializer = Configuration.serializer(),
        handleBackButton = true,
        childFactory = ::createChild,
    )
    
    init {
        // Start with auth guard
        navigation.activate(Configuration.AuthGuard)
    }
    
    private fun createChild(configuration: Configuration, context: ComponentContext): Child {
        return when (configuration) {
            is Configuration.AuthGuard -> {
                val authGuard = DefaultAuthGuard(
                    componentContext = context,
                    sessionManager = sessionManager,
                    createAuthComponent = { authContext -> 
                        SignInComponent(authContext) 
                    },
                    createProtectedComponent = { authContext -> 
                        MainAppComponent(authContext) 
                    }
                )
                Child.AuthGuard(authGuard)
            }
            is Configuration.MainApp -> {
                val authContext = createAuthenticatedContext(
                    componentContext = context,
                    sessionManager = sessionManager,
                    onRequireAuthentication = { navigation.activate(Configuration.AuthGuard) },
                    onNavigateToHome = { navigation.activate(Configuration.MainApp) }
                )
                Child.MainApp(MainAppComponent(authContext))
            }
        }
    }
    
    @Serializable
    private sealed interface Configuration {
        @Serializable data object AuthGuard : Configuration
        @Serializable data object MainApp : Configuration
    }
}
```

### 2. Main App Component

```kotlin
// app/MainAppComponent.kt
interface MainAppComponent : ComponentContext {
    val slot: Value<ChildSlot<*, Child>>
    
    fun navigateToProfile()
    fun navigateToSettings()
    fun signOut()
    
    sealed class Child {
        data class Home(val component: HomeComponent) : Child()
        data class Profile(val component: ProfileComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
    }
}

internal class DefaultMainAppComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : MainAppComponent, ComponentContext by componentContext {
    
    private val navigation = SlotNavigation<Configuration>()
    
    override val slot: Value<ChildSlot<*, Child>> = childSlot(
        source = navigation,
        serializer = Configuration.serializer(),
        handleBackButton = true,
        childFactory = ::createChild,
    )
    
    init {
        // Start with home
        navigation.activate(Configuration.Home)
    }
    
    override fun navigateToProfile() {
        navigation.activate(Configuration.Profile)
    }
    
    override fun navigateToSettings() {
        navigation.activate(Configuration.Settings)
    }
    
    override fun signOut() {
        authContext.sessionManager.signOut()
    }
    
    private fun createChild(configuration: Configuration, context: ComponentContext): Child {
        return when (configuration) {
            is Configuration.Home -> {
                Child.Home(HomeComponent(context, authContext))
            }
            is Configuration.Profile -> {
                Child.Profile(ProfileComponent(context, authContext))
            }
            is Configuration.Settings -> {
                Child.Settings(SettingsComponent(context, authContext))
            }
        }
    }
    
    @Serializable
    private sealed interface Configuration {
        @Serializable data object Home : Configuration
        @Serializable data object Profile : Configuration
        @Serializable data object Settings : Configuration
    }
}
```

### 3. Profile Component with Session Awareness

```kotlin
// features/profile/ProfileComponent.kt
interface ProfileComponent : SessionAwareComponent {
    val user: User?
    val isLoading: Boolean
    val error: String?
    
    fun updateDisplayName(name: String)
    fun updateEmail(email: String)
    fun deleteAccount()
    fun refreshProfile()
}

internal class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : ProfileComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    
    override val user: User? = currentUser
    override val isLoading: Boolean by _isLoading.collectAsState()
    override val error: String? by _error.collectAsState()
    
    override fun updateDisplayName(name: String) {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        scope.launch {
            try {
                authContext.sessionManager.updateProfile(displayName = name)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    override fun updateEmail(email: String) {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        scope.launch {
            try {
                authContext.sessionManager.updateEmail(email)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    override fun deleteAccount() {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        scope.launch {
            try {
                authContext.sessionManager.deleteAccount("") // In real app, ask for password
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
    
    override fun refreshProfile() {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        scope.launch {
            try {
                authContext.sessionManager.refreshSession()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
}
```

### 4. Profile View with Session Integration

```kotlin
// features/profile/ProfileView.kt
@Composable
fun ProfileView(component: ProfileComponent) {
    val currentSession by component.currentSession.collectAsState()
    
    // Listen to session changes
    SessionListener(
        component = component,
        onSessionExpired = { 
            // Session expired, user will be redirected to auth
        },
        onUserSignedIn = { session ->
            // User signed in, refresh profile
            component.refreshProfile()
        }
    ) {
        when (currentSession) {
            is UserSession.Authenticated -> {
                ProfileContent(
                    component = component,
                    user = currentSession.user
                )
            }
            is UserSession.Unauthenticated -> {
                SignInPrompt()
            }
            is UserSession.Refreshing -> {
                LoadingScreen()
            }
            is UserSession.Expired -> {
                SessionExpiredMessage()
            }
        }
    }
}

@Composable
private fun ProfileContent(
    component: ProfileComponent,
    user: User
) {
    val isLoading by component.isLoading.collectAsState()
    val error by component.error.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User info
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Text(
                    text = "Name: ${user.displayName ?: "Not set"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = "Email: ${user.email}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        
        // Update name
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Update Display Name",
                    style = MaterialTheme.typography.titleMedium
                )
                
                var name by remember { mutableStateOf(user.displayName ?: "") }
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Button(
                    onClick = { component.updateDisplayName(name) },
                    enabled = !isLoading && name.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("Update Name")
                    }
                }
            }
        }
        
        // Error message
        error?.let { errorMessage ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Sign out button
        Button(
            onClick = { component.signOut() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Sign Out")
        }
    }
}

@Composable
private fun SignInPrompt() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please sign in to view your profile",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading profile...")
        }
    }
}

@Composable
private fun SessionExpiredMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your session has expired",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Please sign in again to continue",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
```

### 5. Settings Component

```kotlin
// features/settings/SettingsComponent.kt
interface SettingsComponent : SessionAwareComponent {
    fun changePassword(currentPassword: String, newPassword: String)
    fun enableNotifications(enabled: Boolean)
    fun clearCache()
}

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SettingsComponent, ComponentContext by DefaultSessionAwareComponent(componentContext, authContext) {
    
    override fun changePassword(currentPassword: String, newPassword: String) {
        if (!isAuthenticated) {
            requireAuthentication()
            return
        }
        
        scope.launch {
            try {
                authContext.sessionManager.updatePassword(currentPassword, newPassword)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    override fun enableNotifications(enabled: Boolean) {
        // Update notification settings
    }
    
    override fun clearCache() {
        // Clear app cache
    }
}
```

### 6. Main App View

```kotlin
// app/MainAppView.kt
@Composable
fun MainAppView(component: MainAppComponent) {
    val slot by component.slot.subscribeAsState()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(component = component)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            slot.child?.instance?.let { child ->
                when (child) {
                    is MainAppComponent.Child.Home -> {
                        HomeView(child.component)
                    }
                    is MainAppComponent.Child.Profile -> {
                        ProfileView(child.component)
                    }
                    is MainAppComponent.Child.Settings -> {
                        SettingsView(child.component)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(component: MainAppComponent) {
    val slot by component.slot.subscribeAsState()
    val currentIndex = when (slot.child?.configuration) {
        is MainAppComponent.Configuration.Home -> 0
        is MainAppComponent.Configuration.Profile -> 1
        is MainAppComponent.Configuration.Settings -> 2
        else -> 0
    }
    
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentIndex == 0,
            onClick = { component.navigateToHome() }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentIndex == 1,
            onClick = { component.navigateToProfile() }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentIndex == 2,
            onClick = { component.navigateToSettings() }
        )
    }
}
```

### 7. Dependency Injection Setup

```kotlin
// di/AppModule.kt
val appModule = module {
    // Auth module
    includes(authModule)
    
    // Session manager
    single<SessionManager> { 
        DefaultSessionManager(get<AuthRepository>()) 
    }
    
    // Root component
    factory<RootComponent> { (componentContext: ComponentContext) ->
        DefaultRootComponent(
            componentContext = componentContext,
            sessionManager = get()
        )
    }
    
    // Other dependencies...
}
```

### 8. Application Entry Point

```kotlin
// MainApp.kt
@Composable
fun App() {
    val rootComponent: RootComponent by remember { 
        get<RootComponent>(parametersOf(componentContext)) 
    }
    
    RootView(rootComponent)
}

@Composable
private fun RootView(component: RootComponent) {
    val slot by component.slot.subscribeAsState()
    
    slot.child?.instance?.let { child ->
        when (child) {
            is RootComponent.Child.AuthGuard -> {
                AuthGuardView(child.component)
            }
            is RootComponent.Child.MainApp -> {
                MainAppView(child.component)
            }
        }
    }
}
```

## 🎯 Key Features Demonstrated

1. **Automatic Route Protection**: AuthGuard automatically handles navigation between auth and protected content
2. **Session State Management**: Components react to session changes automatically
3. **Type Safety**: All session states are strongly typed
4. **Error Handling**: Comprehensive error handling for authentication failures
5. **Reactive UI**: Compose views update automatically when session state changes
6. **Clean Architecture**: Separation of concerns with clear component boundaries

## 🚀 Running the Example

1. Set up your authentication provider (Supabase, Firebase, etc.)
2. Configure the SessionManager with your provider
3. Add the auth module to your dependency injection
4. Use the components in your app

This complete example shows how to build a robust, session-aware application using the user session middleware with Compose Multiplatform and Decompose.

