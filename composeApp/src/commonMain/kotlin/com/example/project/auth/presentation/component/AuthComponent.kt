package com.example.project.auth.presentation.component

import com.example.project.auth.presentation.store.AuthState
import kotlinx.coroutines.flow.StateFlow

/**
 * Component interface for authentication functionality.
 *
 * This interface defines the contract for authentication components in the presentation layer.
 * It provides access to the authentication state and intents for UI components.
 */
interface AuthComponent {
    val state: StateFlow<AuthState>
    
    fun signUpWithEmail(email: String, password: String, displayName: String? = null)
    fun signUpWithPhone(phone: String, password: String, displayName: String? = null)
    fun signInWithEmail(email: String, password: String)
    fun signInWithPhone(phone: String, password: String)
    fun signInWithEmailOtp(email: String, otp: String)
    fun signInWithPhoneOtp(phone: String, otp: String)
    fun sendEmailOtp(email: String)
    fun sendPhoneOtp(phone: String)
    fun signOut()
    fun resetPassword(email: String)
    fun updatePassword(currentPassword: String, newPassword: String)
    fun updateEmail(newEmail: String, password: String)
    fun updatePhone(newPhone: String, password: String)
    fun updateDisplayName(displayName: String)
    fun updateAvatarUrl(avatarUrl: String)
    fun deleteAccount(password: String)
    fun refreshSession()
    fun clearError()
}
