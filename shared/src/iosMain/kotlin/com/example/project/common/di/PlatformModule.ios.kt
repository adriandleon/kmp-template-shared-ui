package com.example.project.common.di

import com.example.project.common.util.createDataStore
import com.example.project.common.util.provideDispatcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * # iOS Platform Module
 *
 * This file contains the iOS-specific implementation of the platform module. It provides
 * iOS-specific dependencies that are not available on other platforms.
 *
 * ## Adding New iOS-Specific Dependencies
 *
 * ### Example: Adding a new iOS service
 *
 * ```kotlin
 * internal actual val platformModule: Module = module {
 *     // Existing dependencies
 *     factoryOf(::provideDispatcher)
 *     single { createDataStore() }
 *
 *     // New iOS-specific dependency
 *     single<IOSNotificationService> {
 *         IOSNotificationServiceImpl()
 *     }
 *
 *     // Or using factory for lightweight objects
 *     factory<IOSFileManager> {
 *         IOSFileManagerImpl()
 *     }
 * }
 * ```
 *
 * ### Example: Adding iOS-specific repository
 *
 * ```kotlin
 * internal actual val platformModule: Module = module {
 *     // ... existing dependencies
 *
 *     single<IOSSettingsRepository> {
 *         IOSSettingsRepositoryImpl(
 *             dataStore = get(),
 *             userDefaults = NSUserDefaults.standardUserDefaults
 *         )
 *     }
 * }
 * ```
 *
 * ## Common iOS Dependencies to Consider
 * - **iOS-specific services**: UserNotifications, CoreLocation, etc.
 * - **iOS-specific utilities**: FileManager, UserDefaults, etc.
 * - **Platform-specific repositories**: Settings, Permissions, etc.
 * - **iOS-specific data sources**: Core Data, Keychain, etc.
 * - **Native iOS integrations**: HealthKit, HomeKit, etc.
 *
 * ## Best Practices
 * - **Use `single`** for expensive objects that should be reused (repositories, services)
 * - **Use `factory`** for lightweight objects or those that need fresh instances
 * - **Avoid Android-specific APIs** - this is iOS-only code
 * - **Use iOS-specific APIs** when available (UserDefaults, Keychain, etc.)
 * - **Group related dependencies** in the same module
 * - **Document complex dependencies** with KDoc comments
 *
 * ## iOS-Specific Considerations
 * - **No Android Context**: iOS doesn't have Android Context, use iOS-specific alternatives
 * - **UserDefaults**: Use `NSUserDefaults` for simple key-value storage
 * - **Keychain**: Use iOS Keychain for secure storage
 * - **Core Data**: Use Core Data for complex local database needs
 * - **Native integrations**: Leverage iOS-specific frameworks when beneficial
 *
 * @see [PlatformModule.android.kt] for Android implementation
 * @see [Modules.kt] for shared module definitions
 * @see [KoinApp.kt] for initialization logic
 */
internal actual val platformModule: Module = module {
    // Coroutine dispatcher provider for iOS
    // Provides Main and IO dispatchers appropriate for iOS platform
    factoryOf(::provideDispatcher)

    // DataStore configuration for iOS
    // Uses iOS-specific configuration without Android Context
    single { createDataStore() }
}
