package com.example.project.common.di

import com.example.project.about.aboutModule
import com.example.project.analytics.analyticsModule
import com.example.project.contact.contactModule
import com.example.project.features.featureFlagModule
import com.example.project.home.homeModule
import com.example.project.logger.loggerModule
import com.example.project.onboarding.onboardingModule
import org.koin.core.module.Module

/**
 * # Dependency Injection Modules Configuration
 *
 * This file defines the module structure for the Koin dependency injection container. It uses the
 * `actual/expect` pattern to provide platform-specific implementations while maintaining a shared
 * module list for common dependencies.
 *
 * ## Module Types
 *
 * ### 1. Platform Module (actual/expect)
 * - **Purpose**: Platform-specific dependencies that require different implementations
 * - **Pattern**: `expect` declaration in commonMain, `actual` implementations in platform-specific
 *   source sets
 * - **Examples**: DataStore creation, CoroutineDispatcher providers, platform-specific utilities
 *
 * ### 2. Shared Modules
 * - **Purpose**: Common dependencies that work identically across all platforms
 * - **Pattern**: Regular Koin modules defined in commonMain
 * - **Examples**: Business logic, repositories, use cases, shared utilities
 *
 * ## Adding New Modules
 *
 * ### Adding a Shared Module
 * 1. **Create your module** in the appropriate feature package:
 * ```kotlin
 * // In: features/yourfeature/YourFeatureModule.kt
 * val yourFeatureModule = module {
 *     single<YourRepository> { YourRepositoryImpl() }
 *     factory<YourUseCase> { YourUseCase(get()) }
 * }
 * ```
 * 2. **Import and add to sharedModules**:
 * ```kotlin
 * import com.example.project.features.yourfeature.yourFeatureModule
 *
 * internal val sharedModules = listOf(
 *     analyticsModule,
 *     featureFlagModule,
 *     loggerModule,
 *     onboardingModule,
 *     yourFeatureModule  // Add your module here
 * )
 * ```
 *
 * ### Adding a Platform-Specific Module
 * 1. **Create expect declaration** in commonMain:
 * ```kotlin
 * // In: common/di/YourPlatformModule.kt
 * expect val yourPlatformModule: Module
 * ```
 * 2. **Create actual implementations**:
 *
 * **Android** (`androidMain/kotlin/.../YourPlatformModule.android.kt`):
 * ```kotlin
 * actual val yourPlatformModule: Module = module {
 *     single<AndroidSpecificService> { AndroidSpecificServiceImpl(androidContext()) }
 * }
 * ```
 *
 * **iOS** (`iosMain/kotlin/.../YourPlatformModule.ios.kt`):
 * ```kotlin
 * actual val yourPlatformModule: Module = module {
 *     single<IOSSpecificService> { IOSSpecificServiceImpl() }
 * }
 * ```
 * 3. **Add to platformModule** in the respective platform files:
 * ```kotlin
 * // In PlatformModule.android.kt
 * internal actual val platformModule: Module = module {
 *     // ... existing modules
 *     includes(yourPlatformModule)
 * }
 * ```
 *
 * ## Module Organization Best Practices
 * - **Group by feature**: Each feature should have its own module
 * - **Keep modules focused**: One module per feature/concern
 * - **Use descriptive names**: `analyticsModule`, `userModule`, `settingsModule`
 * - **Order dependencies**: List modules in dependency order (dependencies first)
 * - **Document complex modules**: Add KDoc comments for complex dependency graphs
 *
 * ## Current Module Structure
 *
 * ```
 * sharedModules
 * ├── analyticsModule      # Firebase Analytics, Crashlytics
 * ├── featureFlagModule    # Feature flags and remote config
 * ├── loggerModule         # Cross-platform logging (Kermit)
 * └── onboardingModule     # User onboarding flow
 *
 * platformModule (actual/expect)
 * ├── Android: DataStore + Android Context
 * └── iOS: DataStore + iOS-specific services
 * ```
 *
 * @see [KoinApp.kt] for initialization logic
 * @see [PlatformModule.android.kt] for Android-specific implementations
 * @see [PlatformModule.ios.kt] for iOS-specific implementations
 */

/**
 * Platform-specific dependencies using actual/expect pattern.
 *
 * This module contains dependencies that require different implementations for each platform
 * (Android, iOS). The actual implementations are provided in platform-specific source sets.
 *
 * **Android Implementation**: Includes Android Context, DataStore with Android-specific
 * configuration **iOS Implementation**: Includes iOS-specific services and DataStore configuration
 */
internal expect val platformModule: Module

/**
 * Shared dependencies that work identically across all platforms.
 *
 * These modules contain business logic, repositories, use cases, and other dependencies that don't
 * require platform-specific implementations.
 *
 * **Module Order**: Listed in dependency order (dependencies first)
 * - `analyticsModule`: Firebase Analytics and Crashlytics
 * - `featureFlagModule`: Feature flags and remote configuration
 * - `loggerModule`: Cross-platform logging with Kermit
 * - `onboardingModule`: User onboarding flow and state management
 */
internal val sharedModules: List<Module> =
    listOf(
        analyticsModule, // Firebase Analytics, Crashlytics, Performance Monitoring
        featureFlagModule, // Feature flags, A/B testing, remote config
        loggerModule, // Cross-platform logging (Kermit)
        onboardingModule, // User onboarding flow, first-time user experience
        homeModule, // Home screen, dashboard, navigation
        aboutModule, // About screen, app version, build info
        contactModule, // Contact screen, support info FAQs
    )
