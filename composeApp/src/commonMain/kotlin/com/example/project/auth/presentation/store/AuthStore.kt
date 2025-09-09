package com.example.project.auth.presentation.store

import com.example.project.auth.domain.AuthRepository
import com.example.project.auth.domain.entity.AuthError
import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.domain.entity.User
// Store interface removed - using direct StateFlow implementation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Store for managing authentication state and operations.
 *
 * This store follows the MVI pattern and provides a reactive interface for
 * authentication operations. It manages the authentication state and provides
 * methods for all authentication operations.
 */
class AuthStore(
    private val authRepository: AuthRepository,
) {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    suspend fun dispatch(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SignUpWithEmail -> signUpWithEmail(intent.email, intent.password, intent.displayName)
            is AuthIntent.SignUpWithPhone -> signUpWithPhone(intent.phone, intent.password, intent.displayName)
            is AuthIntent.SignInWithEmail -> signInWithEmail(intent.email, intent.password)
            is AuthIntent.SignInWithPhone -> signInWithPhone(intent.phone, intent.password)
            is AuthIntent.SignInWithEmailOtp -> signInWithEmailOtp(intent.email, intent.otp)
            is AuthIntent.SignInWithPhoneOtp -> signInWithPhoneOtp(intent.phone, intent.otp)
            is AuthIntent.SendEmailOtp -> sendEmailOtp(intent.email)
            is AuthIntent.SendPhoneOtp -> sendPhoneOtp(intent.phone)
            is AuthIntent.SignOut -> signOut()
            is AuthIntent.ResetPassword -> resetPassword(intent.email)
            is AuthIntent.UpdatePassword -> updatePassword(intent.currentPassword, intent.newPassword)
            is AuthIntent.UpdateEmail -> updateEmail(intent.newEmail, intent.password)
            is AuthIntent.UpdatePhone -> updatePhone(intent.newPhone, intent.password)
            is AuthIntent.UpdateDisplayName -> updateDisplayName(intent.displayName)
            is AuthIntent.UpdateAvatarUrl -> updateAvatarUrl(intent.avatarUrl)
            is AuthIntent.DeleteAccount -> deleteAccount(intent.password)
            is AuthIntent.RefreshSession -> refreshSession()
            is AuthIntent.ClearError -> clearError()
        }
    }

    private suspend fun signUpWithEmail(email: String, password: String, displayName: String?) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signUpWithEmail(email, password, displayName)
        handleAuthResult(result)
    }

    private suspend fun signUpWithPhone(phone: String, password: String, displayName: String?) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signUpWithPhone(phone, password, displayName)
        handleAuthResult(result)
    }

    private suspend fun signInWithEmail(email: String, password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signInWithEmail(email, password)
        handleAuthResult(result)
    }

    private suspend fun signInWithPhone(phone: String, password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signInWithPhone(phone, password)
        handleAuthResult(result)
    }

    private suspend fun signInWithEmailOtp(email: String, otp: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signInWithEmailOtp(email, otp)
        handleAuthResult(result)
    }

    private suspend fun signInWithPhoneOtp(phone: String, otp: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signInWithPhoneOtp(phone, otp)
        handleAuthResult(result)
    }

    private suspend fun sendEmailOtp(email: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.sendEmailOtp(email)
        handleAuthResult(result)
    }

    private suspend fun sendPhoneOtp(phone: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.sendPhoneOtp(phone)
        handleAuthResult(result)
    }

    private suspend fun signOut() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.signOut()
        handleAuthResult(result)
    }

    private suspend fun resetPassword(email: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.resetPassword(email)
        handleAuthResult(result)
    }

    private suspend fun updatePassword(currentPassword: String, newPassword: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.updatePassword(currentPassword, newPassword)
        handleAuthResult(result)
    }

    private suspend fun updateEmail(newEmail: String, password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.updateEmail(newEmail, password)
        handleAuthResult(result)
    }

    private suspend fun updatePhone(newPhone: String, password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.updatePhone(newPhone, password)
        handleAuthResult(result)
    }

    private suspend fun updateDisplayName(displayName: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.updateDisplayName(displayName)
        handleAuthResult(result)
    }

    private suspend fun updateAvatarUrl(avatarUrl: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.updateAvatarUrl(avatarUrl)
        handleAuthResult(result)
    }

    private suspend fun deleteAccount(password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.deleteAccount(password)
        handleAuthResult(result)
    }

    private suspend fun refreshSession() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authRepository.refreshSession()
        handleAuthResult(result)
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private suspend fun handleAuthResult(result: AuthResult) {
        when (result) {
            is AuthResult.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = result.user,
                    isAuthenticated = true,
                    error = null,
                )
            }
            is AuthResult.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.error,
                )
            }
            is AuthResult.Loading -> {
                _state.value = _state.value.copy(isLoading = true)
            }
        }
    }
}

/**
 * Authentication state containing all necessary data for the UI.
 */
data class AuthState(
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: AuthError? = null,
)

/**
 * Authentication intents representing user actions.
 */
sealed interface AuthIntent {
    data class SignUpWithEmail(val email: String, val password: String, val displayName: String? = null) : AuthIntent
    data class SignUpWithPhone(val phone: String, val password: String, val displayName: String? = null) : AuthIntent
    data class SignInWithEmail(val email: String, val password: String) : AuthIntent
    data class SignInWithPhone(val phone: String, val password: String) : AuthIntent
    data class SignInWithEmailOtp(val email: String, val otp: String) : AuthIntent
    data class SignInWithPhoneOtp(val phone: String, val otp: String) : AuthIntent
    data class SendEmailOtp(val email: String) : AuthIntent
    data class SendPhoneOtp(val phone: String) : AuthIntent
    object SignOut : AuthIntent
    data class ResetPassword(val email: String) : AuthIntent
    data class UpdatePassword(val currentPassword: String, val newPassword: String) : AuthIntent
    data class UpdateEmail(val newEmail: String, val password: String) : AuthIntent
    data class UpdatePhone(val newPhone: String, val password: String) : AuthIntent
    data class UpdateDisplayName(val displayName: String) : AuthIntent
    data class UpdateAvatarUrl(val avatarUrl: String) : AuthIntent
    data class DeleteAccount(val password: String) : AuthIntent
    object RefreshSession : AuthIntent
    object ClearError : AuthIntent
}

// Messages and Actions removed - using direct state management
