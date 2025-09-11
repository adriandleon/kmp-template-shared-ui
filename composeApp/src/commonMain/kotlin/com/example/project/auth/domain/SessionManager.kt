package com.example.project.auth.domain

import com.example.project.auth.domain.entity.SessionEvent
import com.example.project.auth.domain.entity.UserEntity
import com.example.project.auth.domain.entity.UserSession
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Session manager interface for handling user authentication state.
 *
 * This interface provides a provider-agnostic way to manage user sessions, making it easy to switch
 * between different authentication providers.
 */
internal interface SessionManager {
    /** Current session state */
    val currentSession: StateFlow<UserSession>

    /** Session events stream */
    val sessionEvents: SharedFlow<SessionEvent>

    /** Check if user is currently authenticated */
    val isAuthenticated: Boolean

    /** Get current user if authenticated */
    val currentUserEntity: UserEntity?

    /** Get current access token if authenticated */
    val accessToken: String?

    /** Sign in with email and password */
    //    suspend fun signIn(email: String, password: String): Result<Unit>
    //
    //    /** Sign up with email and password */
    //    suspend fun signUp(email: String, password: String, displayName: String? = null):
    // Result<Unit>
    //
    //    /** Sign in with phone and password */
    //    suspend fun signInWithPhone(phone: String, password: String): Result<Unit>
    //
    //    /** Sign up with phone and password */
    //    suspend fun signUpWithPhone(
    //        phone: String,
    //        password: String,
    //        displayName: String? = null,
    //    ): Result<Unit>
    //
    //    /** Sign in with OTP */
    //    suspend fun signInWithOtp(identifier: String, otp: String): Result<Unit>
    //
    //    /** Send OTP to email or phone */
    //    suspend fun sendOtp(identifier: String): Result<Unit>
    //
    //    /** Sign out current user */
    //    suspend fun signOut(): Result<Unit>
    //
    //    /** Refresh current session */
    //    suspend fun refreshSession(): Result<Unit>
    //
    //    /** Reset password for email */
    //    suspend fun resetPassword(email: String): Result<Unit>
    //
    //    /** Update user profile */
    //    suspend fun updateProfile(displayName: String? = null, avatarUrl: String? = null):
    // Result<Unit>
    //
    //    /** Update password */
    //    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit>
    //
    //    /** Delete user account */
    //    suspend fun deleteAccount(password: String): Result<Unit>
    //
    //    /** Clear any error state */
    //    fun clearError()
}
