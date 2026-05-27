package com.example.project.common.di

import co.touchlab.crashkios.crashlytics.CrashlyticsKotlin
import co.touchlab.kermit.koin.KermitKoinLogger
import com.example.project.BuildKonfig
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

/**
 * # Dependency Injection Setup with Koin
 *
 * This file contains the core Koin initialization logic for the Compose Multiplatform application.
 * It provides a centralized way to configure and start the dependency injection container.
 *
 * ## Architecture Overview
 *
 * The DI setup follows a modular approach with three types of modules:
 * 1. **Platform Modules** - Platform-specific implementations using `actual/expect` pattern
 * 2. **Shared Modules** - Common dependencies that work across all platforms
 * 3. **Platform Configuration** - Platform-specific Koin configuration (Android Context, etc.)
 *
 * ## Usage
 *
 * ### Android
 *
 * ```kotlin
 * // In MainApplication.kt
 * class MainApplication : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *         initKoin { androidContext(this@MainApplication) }
 *     }
 * }
 * ```
 *
 * ### iOS
 *
 * ```kotlin
 * // In MainViewController.kt
 * fun MainViewController() = ComposeUIViewController {
 *     initKoin() // No additional config needed for iOS
 *     // ... rest of your setup
 * }
 * ```
 *
 * ## Module Structure
 *
 * ```
 * Koin Container
 * ├── Platform Module (actual/expect)
 * │   ├── Android: PlatformModule.android.kt
 * │   └── iOS: PlatformModule.ios.kt
 * └── Shared Modules
 *     ├── AnalyticsModule
 *     ├── FeatureFlagModule
 *     ├── LoggerModule
 *     └── OnboardingModule
 * ```
 *
 * @param config Optional platform-specific configuration. For Android, this should include
 *   `androidContext()`. For iOS, this can be null or empty.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config)
        // Load all modules: platform-specific + shared modules
        modules(platformModule + sharedModules)

        // Configure logging using Kermit (cross-platform logging)
        logger(KermitKoinLogger(koin.get()))

        // Set custom values for crash reporting
        CrashlyticsKotlin.setCustomValue("flavor", if (BuildKonfig.DEBUG) "debug" else "release")
    }
}
