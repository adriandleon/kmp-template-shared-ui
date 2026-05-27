package com.example.project.common.di

import com.example.project.common.util.createDataStore
import com.example.project.common.util.provideDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * # Android Platform Module
 *
 * This file contains the Android-specific implementation of the platform module. It provides
 * Android-specific dependencies that are not available on other platforms.
 *
 * ## Adding New Android-Specific Dependencies
 *
 * ### Example: Adding a new Android service
 *
 * ```kotlin
 * internal actual val platformModule: Module = module {
 *     // Existing dependencies
 *     factoryOf(::provideDispatcher)
 *     single { createDataStore(androidContext()) }
 *
 *     // New Android-specific dependency
 *     single<AndroidNotificationService> {
 *         AndroidNotificationServiceImpl(androidContext())
 *     }
 *
 *     // Or using factory for lightweight objects
 *     factory<AndroidFileManager> {
 *         AndroidFileManagerImpl(androidContext())
 *     }
 * }
 * ```
 *
 * ### Example: Adding Android-specific repository
 *
 * ```kotlin
 * internal actual val platformModule: Module = module {
 *     // ... existing dependencies
 *
 *     single<AndroidSettingsRepository> {
 *         AndroidSettingsRepositoryImpl(
 *             context = androidContext(),
 *             dataStore = get()
 *         )
 *     }
 * }
 * ```
 *
 * ## Common Android Dependencies to Consider
 * - **Context-dependent services**: NotificationManager, LocationManager, etc.
 * - **Android-specific utilities**: FileManager, SharedPreferences, etc.
 * - **Platform-specific repositories**: Settings, Permissions, etc.
 * - **Android-specific data sources**: Room database, WorkManager, etc.
 *
 * ## Best Practices
 * - **Use `single`** for expensive objects that should be reused (repositories, services)
 * - **Use `factory`** for lightweight objects or those that need fresh instances
 * - **Always use `androidContext()`** for Android-specific dependencies
 * - **Group related dependencies** in the same module
 * - **Document complex dependencies** with KDoc comments
 *
 * @see [PlatformModule.ios.kt] for iOS implementation
 * @see [Modules.kt] for shared module definitions
 * @see [KoinApp.kt] for initialization logic
 */
internal actual val platformModule: Module = module {
    // Coroutine dispatcher provider for Android
    // Provides Main and IO dispatchers appropriate for Android platform
    factoryOf(::provideDispatcher)

    // DataStore configuration for Android
    // Uses Android Context to create platform-specific DataStore instance
    single { createDataStore(androidContext()) }
}
