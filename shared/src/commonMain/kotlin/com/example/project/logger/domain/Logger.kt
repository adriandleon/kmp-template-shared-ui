package com.example.project.logger.domain

/**
 * A multiplatform logging interface that provides consistent logging capabilities across different
 * platforms.
 *
 * This interface abstracts logging operations and allows for platform-specific implementations
 * while maintaining a unified API for logging throughout the application.
 *
 * ## Usage Examples
 *
 * ```kotlin
 * class MyService(private val logger: Logger) {
 *     fun processData() {
 *         logger.info { "Starting data processing" }
 *
 *         try {
 *             // ... processing logic
 *             logger.debug { "Data processed successfully" }
 *         } catch (e: Exception) {
 *             logger.error(e) { "Failed to process data" }
 *         }
 *     }
 * }
 * ```
 *
 * ## Platform Implementations
 * - **Android**: Uses Android's Log system with appropriate log levels
 * - **iOS**: Uses NSLog or custom logging framework
 * - **Common**: Provides a no-op implementation for testing
 *
 * ## Log Levels
 * - **Error**: For errors and exceptions that need immediate attention
 * - **Debug**: For detailed information useful during development
 * - **Info**: For general information about application flow
 *
 * @see com.example.project.logger.data.KermitLoggerImpl
 * @see com.example.project.logger.data.ConfigCatLoggerImpl
 */
interface Logger {

    /**
     * Logs an error message with an optional throwable.
     *
     * Use this function to log errors, exceptions, and critical issues that require attention. The
     * throwable parameter is optional but recommended for errors to provide stack trace
     * information.
     *
     * @param throwable The exception or error that occurred. Can be null if no exception is
     *   available.
     * @param message A lambda that returns the error message. The lambda is only evaluated if
     *   logging is enabled.
     *
     * ## Examples
     *
     * ```kotlin
     * // Log error with exception
     * logger.error(exception) { "Failed to connect to server" }
     *
     * // Log error without exception
     * logger.error { "Invalid configuration detected" }
     *
     * // Log error with context
     * logger.error(exception) { "API call failed for endpoint: $endpoint" }
     * ```
     */
    fun error(throwable: Throwable? = null, message: () -> String)

    /**
     * Logs a debug message with an optional throwable.
     *
     * Use this function to log detailed information useful during development and debugging. Debug
     * messages are typically only shown in debug builds and can include sensitive information.
     *
     * @param throwable The exception or error that occurred. Can be null if no exception is
     *   available.
     * @param message A lambda that returns the debug message. The lambda is only evaluated if
     *   logging is enabled.
     *
     * ## Examples
     *
     * ```kotlin
     * // Log debug information
     * logger.debug { "Processing user request: $userId" }
     *
     * // Log debug with exception
     * logger.debug(exception) { "Recovered from non-critical error" }
     *
     * // Log debug with complex data
     * logger.debug { "Cache hit ratio: ${cache.hitRatio}, size: ${cache.size}" }
     * ```
     */
    fun debug(throwable: Throwable? = null, message: () -> String)

    /**
     * Logs an informational message.
     *
     * Use this function to log general information about application flow, user actions, and other
     * events that are useful for monitoring and understanding application behavior.
     *
     * @param message A lambda that returns the informational message. The lambda is only evaluated
     *   if logging is enabled.
     *
     * ## Examples
     *
     * ```kotlin
     * // Log user actions
     * logger.info { "User $userId logged in successfully" }
     *
     * // Log application state
     * logger.info { "Application started with version $version" }
     *
     * // Log business events
     * logger.info { "Order $orderId completed with total: $total" }
     * ```
     */
    fun info(message: () -> String)
}
