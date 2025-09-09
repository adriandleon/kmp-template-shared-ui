package com.example.project.auth.data.mapper

import com.example.project.auth.domain.entity.AuthError
// Removed unused imports

/**
 * Mapper for converting Supabase authentication errors to domain AuthError entities.
 *
 * This mapper handles the conversion between provider-specific error messages and
 * the domain layer's AuthError sealed interface, providing consistent error handling.
 */
object AuthErrorMapper {
    /**
     * Converts Supabase error message to domain AuthError.
     *
     * @param message Error message from Supabase
     * @param errorCode Optional error code for more specific error handling
     * @return Domain AuthError entity
     */
    fun toDomain(message: String, errorCode: String? = null): AuthError {
        return when {
            // Network-related errors
            message.contains("network", ignoreCase = true) ||
                message.contains("timeout", ignoreCase = true) ||
                message.contains("connection", ignoreCase = true) -> {
                AuthError.NetworkError(message)
            }

            // Invalid credentials
            message.contains("invalid credentials", ignoreCase = true) ||
                message.contains("invalid login", ignoreCase = true) ||
                message.contains("wrong password", ignoreCase = true) ||
                message.contains("incorrect password", ignoreCase = true) -> {
                AuthError.InvalidCredentials(message)
            }

            // User not found
            message.contains("user not found", ignoreCase = true) ||
                message.contains("no user found", ignoreCase = true) ||
                message.contains("user does not exist", ignoreCase = true) -> {
                AuthError.UserNotFound(message)
            }

            // Email already exists
            message.contains("email already registered", ignoreCase = true) ||
                message.contains("user already registered", ignoreCase = true) ||
                message.contains("email already exists", ignoreCase = true) -> {
                AuthError.EmailAlreadyExists(message)
            }

            // Phone already exists
            message.contains("phone already registered", ignoreCase = true) ||
                message.contains("phone already exists", ignoreCase = true) -> {
                AuthError.PhoneAlreadyExists(message)
            }

            // Email not verified
            message.contains("email not confirmed", ignoreCase = true) ||
                message.contains("email not verified", ignoreCase = true) ||
                message.contains("confirm your email", ignoreCase = true) -> {
                AuthError.EmailNotVerified(message)
            }

            // Phone not verified
            message.contains("phone not confirmed", ignoreCase = true) ||
                message.contains("phone not verified", ignoreCase = true) ||
                message.contains("confirm your phone", ignoreCase = true) -> {
                AuthError.PhoneNotVerified(message)
            }

            // Weak password
            message.contains("password too weak", ignoreCase = true) ||
                message.contains("password too short", ignoreCase = true) ||
                message.contains("password requirements", ignoreCase = true) -> {
                AuthError.WeakPassword(message)
            }

            // Invalid email
            message.contains("invalid email", ignoreCase = true) ||
                message.contains("email format", ignoreCase = true) ||
                message.contains("malformed email", ignoreCase = true) -> {
                AuthError.InvalidEmail(message)
            }

            // Invalid phone
            message.contains("invalid phone", ignoreCase = true) ||
                message.contains("phone format", ignoreCase = true) ||
                message.contains("malformed phone", ignoreCase = true) -> {
                AuthError.InvalidPhone(message)
            }

            // Invalid OTP
            message.contains("invalid otp", ignoreCase = true) ||
                message.contains("invalid code", ignoreCase = true) ||
                message.contains("wrong otp", ignoreCase = true) -> {
                AuthError.InvalidOtp(message)
            }

            // OTP expired
            message.contains("otp expired", ignoreCase = true) ||
                message.contains("code expired", ignoreCase = true) ||
                message.contains("expired code", ignoreCase = true) -> {
                AuthError.OtpExpired(message)
            }

            // Too many attempts
            message.contains("too many attempts", ignoreCase = true) ||
                message.contains("rate limit", ignoreCase = true) ||
                message.contains("too many requests", ignoreCase = true) -> {
                AuthError.TooManyAttempts(message)
            }

            // User disabled
            message.contains("user disabled", ignoreCase = true) ||
                message.contains("account disabled", ignoreCase = true) ||
                message.contains("user banned", ignoreCase = true) -> {
                AuthError.UserDisabled(message)
            }

            // Generic error for unknown cases
            else -> AuthError.GenericError(message)
        }
    }

    /**
     * Converts Supabase exception to domain AuthError.
     *
     * @param throwable Exception from Supabase
     * @return Domain AuthError entity
     */
    fun toDomain(throwable: Throwable): AuthError {
        return when (throwable) {
            is java.net.UnknownHostException,
            is java.net.ConnectException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> {
                AuthError.NetworkError(throwable.message ?: "Network error occurred")
            }
            else -> {
                AuthError.UnknownError(throwable.message ?: "Unknown error occurred")
            }
        }
    }
}
