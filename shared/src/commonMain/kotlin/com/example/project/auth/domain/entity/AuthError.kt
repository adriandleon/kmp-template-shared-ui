package com.example.project.auth.domain.entity

/**
 * Represents authentication errors in a provider-agnostic way.
 *
 * This sealed interface abstracts away provider-specific error details and provides a consistent
 * error handling experience across different authentication providers.
 */
sealed interface AuthError {
    /** Network-related errors (no internet, timeout, etc.) */
    data class NetworkError(val message: String) : AuthError

    /** Invalid credentials provided during login */
    data class InvalidCredentials(val message: String) : AuthError

    /** User account not found */
    data class UserNotFound(val message: String) : AuthError

    /** Email address is already registered */
    data class EmailAlreadyExists(val message: String) : AuthError

    /** Phone number is already registered */
    data class PhoneAlreadyExists(val message: String) : AuthError

    /** Email address is not verified */
    data class EmailNotVerified(val message: String) : AuthError

    /** Phone number is not verified */
    data class PhoneNotVerified(val message: String) : AuthError

    /** Password is too weak or doesn't meet requirements */
    data class WeakPassword(val message: String) : AuthError

    /** Invalid email format */
    data class InvalidEmail(val message: String) : AuthError

    /** Invalid phone number format */
    data class InvalidPhone(val message: String) : AuthError

    /** OTP (One-Time Password) is invalid or expired */
    data class InvalidOtp(val message: String) : AuthError

    /** OTP has expired */
    data class OtpExpired(val message: String) : AuthError

    /** Too many authentication attempts */
    data class TooManyAttempts(val message: String) : AuthError

    /** User account is disabled or banned */
    data class UserDisabled(val message: String) : AuthError

    /** Generic authentication error */
    data class GenericError(val message: String) : AuthError

    /** Unknown or unexpected error */
    data class UnknownError(val message: String) : AuthError
}
