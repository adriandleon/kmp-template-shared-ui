package com.example.project.auth.domain

import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.domain.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 *
 * This interface defines all authentication-related operations in a provider-agnostic way, allowing
 * the application to easily switch between different authentication providers (Supabase, Firebase,
 * etc.) without changing the business logic.
 */
interface AuthRepository {
    /**
     * Current authentication state as a Flow. Emits the current user if authenticated, null if not
     * authenticated.
     */
    val currentUser: Flow<User?>

    /**
     * Current authentication session status. Emits true if user is authenticated, false otherwise.
     */
    val isAuthenticated: Flow<Boolean>

    /**
     * Sign up a new user with email and password.
     *
     * @param email User's email address
     * @param password User's password
     * @param displayName Optional display name for the user
     * @return AuthResult containing the created user or error information
     */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String? = null,
    ): AuthResult

    /**
     * Sign up a new user with phone number and password.
     *
     * @param phone User's phone number
     * @param password User's password
     * @param displayName Optional display name for the user
     * @return AuthResult containing the created user or error information
     */
    suspend fun signUpWithPhone(
        phone: String,
        password: String,
        displayName: String? = null,
    ): AuthResult

    /**
     * Sign in with email and password.
     *
     * @param email User's email address
     * @param password User's password
     * @return AuthResult containing the authenticated user or error information
     */
    suspend fun signInWithEmail(email: String, password: String): AuthResult

    /**
     * Sign in with phone number and password.
     *
     * @param phone User's phone number
     * @param password User's password
     * @return AuthResult containing the authenticated user or error information
     */
    suspend fun signInWithPhone(phone: String, password: String): AuthResult

    /**
     * Sign in with OTP (One-Time Password) sent to email.
     *
     * @param email User's email address
     * @param otp One-time password received via email
     * @return AuthResult containing the authenticated user or error information
     */
    suspend fun signInWithEmailOtp(email: String, otp: String): AuthResult

    /**
     * Sign in with OTP (One-Time Password) sent to phone.
     *
     * @param phone User's phone number
     * @param otp One-time password received via SMS
     * @return AuthResult containing the authenticated user or error information
     */
    suspend fun signInWithPhoneOtp(phone: String, otp: String): AuthResult

    /**
     * Send OTP to email for authentication.
     *
     * @param email User's email address
     * @return AuthResult indicating success or failure
     */
    suspend fun sendEmailOtp(email: String): AuthResult

    /**
     * Send OTP to phone for authentication.
     *
     * @param phone User's phone number
     * @return AuthResult indicating success or failure
     */
    suspend fun sendPhoneOtp(phone: String): AuthResult

    /**
     * Sign out the current user.
     *
     * @return AuthResult indicating success or failure
     */
    suspend fun signOut(): AuthResult

    /**
     * Reset password for the given email address.
     *
     * @param email User's email address
     * @return AuthResult indicating success or failure
     */
    suspend fun resetPassword(email: String): AuthResult

    /**
     * Update user's password.
     *
     * @param currentPassword Current password for verification
     * @param newPassword New password to set
     * @return AuthResult indicating success or failure
     */
    suspend fun updatePassword(currentPassword: String, newPassword: String): AuthResult

    /**
     * Update user's email address.
     *
     * @param newEmail New email address
     * @param password Current password for verification
     * @return AuthResult indicating success or failure
     */
    suspend fun updateEmail(newEmail: String, password: String): AuthResult

    /**
     * Update user's phone number.
     *
     * @param newPhone New phone number
     * @param password Current password for verification
     * @return AuthResult indicating success or failure
     */
    suspend fun updatePhone(newPhone: String, password: String): AuthResult

    /**
     * Update user's display name.
     *
     * @param displayName New display name
     * @return AuthResult indicating success or failure
     */
    suspend fun updateDisplayName(displayName: String): AuthResult

    /**
     * Update user's avatar URL.
     *
     * @param avatarUrl New avatar URL
     * @return AuthResult indicating success or failure
     */
    suspend fun updateAvatarUrl(avatarUrl: String): AuthResult

    /**
     * Delete the current user account.
     *
     * @param password Current password for verification
     * @return AuthResult indicating success or failure
     */
    suspend fun deleteAccount(password: String): AuthResult

    /**
     * Refresh the current user's session.
     *
     * @return AuthResult containing the refreshed user or error information
     */
    suspend fun refreshSession(): AuthResult

    /**
     * Get the current user synchronously. Returns null if no user is authenticated.
     *
     * @return Current user or null
     */
    fun getCurrentUser(): User?

    /**
     * Check if the current user is authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    fun isUserAuthenticated(): Boolean
}
