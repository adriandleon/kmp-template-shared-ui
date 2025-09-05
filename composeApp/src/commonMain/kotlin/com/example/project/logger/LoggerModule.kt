package com.example.project.logger

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.platformLogWriter
import com.example.project.BuildKonfig
import com.example.project.logger.data.ConfigCatLoggerImpl
import com.example.project.logger.data.KermitLoggerImpl
import com.example.project.logger.domain.Logger
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin dependency injection module for logging components.
 *
 * This module provides the necessary dependencies for the logging system, including the main
 * [Logger] implementation and platform-specific log writers. It supports multiple logging backends
 * and automatically configures the appropriate logging strategy based on the build environment.
 *
 * The module is designed to be:
 * - **Environment-aware**: Different logging strategies for debug and release builds
 * - **Multi-platform**: Works across Android, iOS, and other Kotlin Multiplatform targets
 * - **Crash-reporting integrated**: Automatically sends logs to Crashlytics in production
 * - **Configurable**: Easy to customize logging behavior and add new log writers
 * - **Testable**: Supports test implementations and mock loggers
 *
 * **Logging Strategy:**
 * - **Debug builds**: Uses platform-specific log writers (Android Log, iOS NSLog, etc.)
 * - **Release builds**: Uses Crashlytics integration for centralized crash reporting
 * - **ConfigCat integration**: Provides specialized logger for ConfigCat SDK
 *
 * @see Logger for the main logging interface
 * @see KermitLoggerImpl for the Kermit-based implementation
 * @see ConfigCatLoggerImpl for ConfigCat SDK integration
 * @see co.touchlab.kermit.Logger for the underlying Kermit logger
 * @example
 *
 * ```kotlin
 * // In your Koin setup
 * startKoin {
 *     modules(
 *         loggerModule,
 *         // other modules...
 *     )
 * }
 *
 * // Usage in your classes
 * class MyRepository(
 *     private val logger: Logger
 * ) {
 *     fun doSomething() {
 *         logger.d("Repository", "Doing something important")
 *         logger.e("Repository", "Something went wrong", exception)
 *     }
 * }
 *
 * // Usage with ConfigCat
 * class FeatureFlagService(
 *     private val configCatLogger: com.configcat.log.Logger
 * ) {
 *     fun initialize() {
 *         configCatLogger.info("ConfigCat initialized successfully")
 *     }
 * }
 * ```
 */
internal val loggerModule = module {
    singleOf(::kermitLogger)
    singleOf(::KermitLoggerImpl) { bind<Logger>() }
    singleOf(::ConfigCatLoggerImpl) { bind<com.configcat.log.Logger>() }
}

private typealias KermitLogger = co.touchlab.kermit.Logger

/**
 * Creates and configures a Kermit logger instance.
 *
 * This function sets up the Kermit logger with appropriate configuration based on the build
 * environment. It automatically selects the most suitable log writer strategy:
 * - **Debug builds**: Uses platform-specific log writers for immediate console output
 * - **Release builds**: Uses Crashlytics integration for centralized crash reporting
 *
 * The logger is configured with:
 * - Appropriate log writers based on build type
 * - Application tag for easy filtering
 * - Minimum severity level for production logging
 *
 * @return Configured Kermit logger instance
 */
@OptIn(ExperimentalKermitApi::class)
private fun kermitLogger(): KermitLogger {
    val logWriter =
        if (BuildKonfig.DEBUG) {
            platformLogWriter()
        } else {
            CrashlyticsLogWriter(minSeverity = Severity.Verbose)
        }

    KermitLogger.setLogWriters(logWriter)
    KermitLogger.setTag("AppTemplate")
    return co.touchlab.kermit.Logger
}
