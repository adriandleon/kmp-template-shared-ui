# Dependency Injection with Koin - Developer Guide

This guide explains how to work with the Koin dependency injection setup in this Compose Multiplatform application.

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Quick Start](#quick-start)
4. [Adding New Dependencies](#adding-new-dependencies)
5. [Platform-Specific Dependencies](#platform-specific-dependencies)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)
8. [Examples](#examples)

## Overview

This project uses [Koin](https://insert-koin.io/) for dependency injection, providing a lightweight and pragmatic solution for managing dependencies across Android and iOS platforms.

### Key Benefits

- **Lightweight**: No code generation, no reflection
- **Cross-platform**: Works seamlessly on Android and iOS
- **Easy to use**: Simple DSL for defining dependencies
- **Testable**: Easy to mock and test dependencies

## Architecture

### Module Structure

```
Koin Container
├── Platform Module (actual/expect)
│   ├── Android: PlatformModule.android.kt
│   └── iOS: PlatformModule.ios.kt
└── Shared Modules
    ├── AnalyticsModule
    ├── FeatureFlagModule
    ├── LoggerModule
    └── OnboardingModule
```

### File Organization

```
composeApp/src/
├── commonMain/kotlin/com/example/project/common/di/
│   ├── KoinApp.kt              # Koin initialization
│   └── Modules.kt              # Module definitions
├── androidMain/kotlin/com/example/project/common/di/
│   └── PlatformModule.android.kt  # Android-specific dependencies
└── iosMain/kotlin/com/example/project/common/di/
    └── PlatformModule.ios.kt      # iOS-specific dependencies
```

## Quick Start

### 1. Initialization

**Android** (`MainApplication.kt`):
```kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@MainApplication) }
    }
}
```

**iOS** (`MainViewController.kt`):
```kotlin
fun MainViewController() = ComposeUIViewController {
    initKoin() // No additional config needed for iOS
    // ... rest of your setup
}
```

### 2. Using Dependencies

**In a Component**:
```kotlin
class MyComponent : KoinComponent {
    private val myRepository: MyRepository by inject()
    
    fun doSomething() {
        myRepository.getData()
    }
}
```

**In a Composable**:
```kotlin
@Composable
fun MyScreen() {
    val myRepository: MyRepository = koinInject()
    
    // Use the repository
}
```

## Adding New Dependencies

### Shared Dependencies (Common to All Platforms)

1. **Create your module** in the appropriate feature package:

```kotlin
// In: features/user/UserModule.kt
val userModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory<GetUserUseCase> { GetUserUseCase(get()) }
    single<UpdateUserUseCase> { UpdateUserUseCase(get()) }
}
```

2. **Add to sharedModules** in `Modules.kt`:

```kotlin
import com.example.project.features.user.userModule

internal val sharedModules: List<Module> = listOf(
    analyticsModule,
    featureFlagModule,
    loggerModule,
    onboardingModule,
    userModule  // Add your module here
)
```

### Platform-Specific Dependencies

#### Step 1: Create Expect Declaration

```kotlin
// In: common/di/YourPlatformModule.kt
expect val yourPlatformModule: Module
```

#### Step 2: Create Android Implementation

```kotlin
// In: androidMain/kotlin/.../YourPlatformModule.android.kt
actual val yourPlatformModule: Module = module {
    single<AndroidNotificationService> { 
        AndroidNotificationServiceImpl(androidContext()) 
    }
    
    factory<AndroidFileManager> { 
        AndroidFileManagerImpl(androidContext()) 
    }
}
```

#### Step 3: Create iOS Implementation

```kotlin
// In: iosMain/kotlin/.../YourPlatformModule.ios.kt
actual val yourPlatformModule: Module = module {
    single<IOSNotificationService> { 
        IOSNotificationServiceImpl() 
    }
    
    factory<IOSFileManager> { 
        IOSFileManagerImpl() 
    }
}
```

#### Step 4: Include in Platform Module

**Android** (`PlatformModule.android.kt`):
```kotlin
internal actual val platformModule: Module = module {
    factoryOf(::provideDispatcher)
    single { createDataStore(androidContext()) }
    includes(yourPlatformModule)  // Add your platform module
}
```

**iOS** (`PlatformModule.ios.kt`):
```kotlin
internal actual val platformModule: Module = module {
    factoryOf(::provideDispatcher)
    single { createDataStore() }
    includes(yourPlatformModule)  // Add your platform module
}
```

## Platform-Specific Dependencies

### Android-Specific Dependencies

Common Android dependencies you might need:

```kotlin
// Android-specific services
single<NotificationManager> { 
    get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

// Android-specific repositories
single<AndroidSettingsRepository> { 
    AndroidSettingsRepositoryImpl(
        context = androidContext(),
        dataStore = get()
    )
}

// Android-specific utilities
factory<AndroidFileManager> { 
    AndroidFileManagerImpl(androidContext()) 
}
```

### iOS-Specific Dependencies

Common iOS dependencies you might need:

```kotlin
// iOS-specific services
single<IOSNotificationService> { 
    IOSNotificationServiceImpl() 
}

// iOS-specific repositories
single<IOSSettingsRepository> { 
    IOSSettingsRepositoryImpl(
        dataStore = get(),
        userDefaults = NSUserDefaults.standardUserDefaults
    )
}

// iOS-specific utilities
factory<IOSFileManager> { 
    IOSFileManagerImpl() 
}
```

## Best Practices

### 1. Module Organization

- **Group by feature**: Each feature should have its own module
- **Keep modules focused**: One module per feature/concern
- **Use descriptive names**: `userModule`, `settingsModule`, `notificationModule`

### 2. Dependency Scope

- **`single`**: For expensive objects that should be reused (repositories, services)
- **`factory`**: For lightweight objects or those that need fresh instances
- **`scoped`**: For objects with specific lifecycle (rarely used)

### 3. Naming Conventions

- **Modules**: `featureModule` (camelCase)
- **Interfaces**: `UserRepository` (PascalCase)
- **Implementations**: `UserRepositoryImpl` (PascalCase + Impl suffix)

### 4. Documentation

- **Document complex modules** with KDoc comments
- **Explain platform differences** in actual implementations
- **Provide usage examples** for complex dependencies

### 5. Testing

- **Mock dependencies** in tests using Koin's test modules
- **Use `startKoin`** with test modules in test setup
- **Clean up** Koin context after tests

## Troubleshooting

### Common Issues

#### 1. "No definition found for type"

**Problem**: Koin can't find a dependency definition.

**Solution**: 
- Check if the module is included in `sharedModules` or `platformModule`
- Verify the module is imported correctly
- Ensure the dependency is defined in the correct scope

#### 2. "KoinApplication has not been started"

**Problem**: Trying to inject dependencies before Koin is initialized.

**Solution**: 
- Ensure `initKoin()` is called before any dependency injection
- Check that initialization happens in the correct lifecycle method

#### 3. "No definition found for type 'android.content.Context'"

**Problem**: Android Context is not available in Koin.

**Solution**: 
- Ensure `androidContext()` is called in the Koin configuration
- Check that `MainApplication` is properly configured in `AndroidManifest.xml`

#### 4. Platform-specific dependency not found

**Problem**: Platform-specific dependency is not available on the current platform.

**Solution**: 
- Check if the dependency is defined in the correct platform module
- Verify the actual/expect pattern is implemented correctly
- Ensure the platform module is included in the platform-specific `platformModule`

### Debug Tips

1. **Enable Koin logging**:
```kotlin
initKoin {
    // ... other config
    logger(PrintLogger()) // For debugging
}
```

2. **Check module loading**:
```kotlin
// Add this to verify modules are loaded
println("Loaded modules: ${koin.get<Module>().allModules}")
```

3. **Verify dependency resolution**:
```kotlin
// Test if a dependency can be resolved
try {
    val dependency = koin.get<YourDependency>()
    println("Dependency resolved: $dependency")
} catch (e: Exception) {
    println("Failed to resolve dependency: ${e.message}")
}
```

## Examples

### Complete Feature Module Example

```kotlin
// UserModule.kt
val userModule = module {
    // Repository
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    
    // Use cases
    factory<GetUserUseCase> { GetUserUseCase(get()) }
    factory<UpdateUserUseCase> { UpdateUserUseCase(get()) }
    factory<DeleteUserUseCase> { DeleteUserUseCase(get()) }
    
    // Data sources
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get()) }
}
```

### Platform-Specific Service Example

```kotlin
// Common interface
interface NotificationService {
    fun showNotification(title: String, message: String)
}

// Android implementation
class AndroidNotificationService(
    private val context: Context
) : NotificationService {
    override fun showNotification(title: String, message: String) {
        // Android-specific notification logic
    }
}

// iOS implementation
class IOSNotificationService : NotificationService {
    override fun showNotification(title: String, message: String) {
        // iOS-specific notification logic
    }
}
```

### Testing Example

```kotlin
class UserRepositoryTest {
    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single<UserRepository> { mockk<UserRepository>() }
                }
            )
        }
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun testUserRepository() {
        val repository: UserRepository = koinInject()
        // Test your repository
    }
}
```

## Additional Resources

- [Koin Documentation](https://insert-koin.io/)
- [Koin Multiplatform Guide](https://insert-koin.io/docs/reference/koin-mp/koin-mp)
- [Koin Testing Guide](https://insert-koin.io/docs/reference/koin-test/testing)
- [Compose Multiplatform Documentation](https://www.jetbrains.com/lp/compose-multiplatform/)

---

**Need help?** Check the existing modules in the codebase for examples, or refer to the Koin documentation for advanced usage patterns.
