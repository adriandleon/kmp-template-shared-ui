package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.example.project.auth.presentation.store.AuthStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Default implementation of AuthComponent.
 *
 * This class provides the concrete implementation of the AuthComponent interface, managing the
 * authentication store and handling component lifecycle using MVIKotlin.
 *
 * Deeplink URL: "example://app/auth"
 */
class DefaultAuthComponent(componentContext: ComponentContext) :
    AuthComponent, ComponentContext by componentContext, KoinComponent {

    private val authStore: AuthStore by inject()
    private val componentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override val state: StateFlow<AuthStore.State> =
        authStore.states.stateIn(
            scope = componentScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = AuthStore.State(),
        )

    init {
        // Listen to store labels for side effects
        componentScope.launch { authStore.labels.collect { label -> handleLabel(label) } }
    }

    private fun handleLabel(label: AuthStore.Label) {
        when (label) {
            is AuthStore.Label.NavigateToHome -> {
                // Handle navigation to home
                // This would typically be handled by the parent component
            }
            is AuthStore.Label.NavigateToAuth -> {
                // Handle navigation to auth
                // This would typically be handled by the parent component
            }
            is AuthStore.Label.ShowPasswordResetSent -> {
                // Handle showing password reset sent message
                // This would typically be handled by the UI
            }
            is AuthStore.Label.ShowOtpSent -> {
                // Handle showing OTP sent message
                // This would typically be handled by the UI
            }
            is AuthStore.Label.ShowAccountDeleted -> {
                // Handle showing account deleted message
                // This would typically be handled by the UI
            }
        }
    }

    override fun signUpWithEmail(email: String, password: String, displayName: String?) {
        authStore.accept(AuthStore.Intent.SignUpWithEmail(email, password, displayName))
    }

    override fun signUpWithPhone(phone: String, password: String, displayName: String?) {
        authStore.accept(AuthStore.Intent.SignUpWithPhone(phone, password, displayName))
    }

    override fun signInWithEmail(email: String, password: String) {
        authStore.accept(AuthStore.Intent.SignInWithEmail(email, password))
    }

    override fun signInWithPhone(phone: String, password: String) {
        authStore.accept(AuthStore.Intent.SignInWithPhone(phone, password))
    }

    override fun signInWithEmailOtp(email: String, otp: String) {
        authStore.accept(AuthStore.Intent.SignInWithEmailOtp(email, otp))
    }

    override fun signInWithPhoneOtp(phone: String, otp: String) {
        authStore.accept(AuthStore.Intent.SignInWithPhoneOtp(phone, otp))
    }

    override fun sendEmailOtp(email: String) {
        authStore.accept(AuthStore.Intent.SendEmailOtp(email))
    }

    override fun sendPhoneOtp(phone: String) {
        authStore.accept(AuthStore.Intent.SendPhoneOtp(phone))
    }

    override fun signOut() {
        authStore.accept(AuthStore.Intent.SignOut)
    }

    override fun resetPassword(email: String) {
        authStore.accept(AuthStore.Intent.ResetPassword(email))
    }

    override fun updatePassword(currentPassword: String, newPassword: String) {
        authStore.accept(AuthStore.Intent.UpdatePassword(currentPassword, newPassword))
    }

    override fun updateEmail(newEmail: String, password: String) {
        authStore.accept(AuthStore.Intent.UpdateEmail(newEmail, password))
    }

    override fun updatePhone(newPhone: String, password: String) {
        authStore.accept(AuthStore.Intent.UpdatePhone(newPhone, password))
    }

    override fun updateDisplayName(displayName: String) {
        authStore.accept(AuthStore.Intent.UpdateDisplayName(displayName))
    }

    override fun updateAvatarUrl(avatarUrl: String) {
        authStore.accept(AuthStore.Intent.UpdateAvatarUrl(avatarUrl))
    }

    override fun deleteAccount(password: String) {
        authStore.accept(AuthStore.Intent.DeleteAccount(password))
    }

    override fun refreshSession() {
        authStore.accept(AuthStore.Intent.RefreshSession)
    }

    override fun clearError() {
        authStore.accept(AuthStore.Intent.ClearError)
    }

    fun onDestroy() {
        componentScope.cancel()
    }
}
