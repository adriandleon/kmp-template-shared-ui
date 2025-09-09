package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.auth.domain.AuthRepository
import com.example.project.auth.presentation.store.AuthIntent
import com.example.project.auth.presentation.store.AuthState
import com.example.project.auth.presentation.store.AuthStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Default implementation of AuthComponent.
 *
 * This class provides the concrete implementation of the AuthComponent interface,
 * managing the authentication store and handling component lifecycle.
 */
class DefaultAuthComponent(
    componentContext: ComponentContext,
) : AuthComponent, ComponentContext by componentContext, KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val authStore = AuthStore(authRepository)

    private val componentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override val state: StateFlow<AuthState> = authStore.state

    init {
        // Initialize authentication state
        // No actions handling needed with direct state management
    }

    override fun signUpWithEmail(email: String, password: String, displayName: String?) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignUpWithEmail(email, password, displayName))
        }
    }

    override fun signUpWithPhone(phone: String, password: String, displayName: String?) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignUpWithPhone(phone, password, displayName))
        }
    }

    override fun signInWithEmail(email: String, password: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignInWithEmail(email, password))
        }
    }

    override fun signInWithPhone(phone: String, password: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignInWithPhone(phone, password))
        }
    }

    override fun signInWithEmailOtp(email: String, otp: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignInWithEmailOtp(email, otp))
        }
    }

    override fun signInWithPhoneOtp(phone: String, otp: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignInWithPhoneOtp(phone, otp))
        }
    }

    override fun sendEmailOtp(email: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SendEmailOtp(email))
        }
    }

    override fun sendPhoneOtp(phone: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SendPhoneOtp(phone))
        }
    }

    override fun signOut() {
        componentScope.launch {
            authStore.dispatch(AuthIntent.SignOut)
        }
    }

    override fun resetPassword(email: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.ResetPassword(email))
        }
    }

    override fun updatePassword(currentPassword: String, newPassword: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.UpdatePassword(currentPassword, newPassword))
        }
    }

    override fun updateEmail(newEmail: String, password: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.UpdateEmail(newEmail, password))
        }
    }

    override fun updatePhone(newPhone: String, password: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.UpdatePhone(newPhone, password))
        }
    }

    override fun updateDisplayName(displayName: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.UpdateDisplayName(displayName))
        }
    }

    override fun updateAvatarUrl(avatarUrl: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.UpdateAvatarUrl(avatarUrl))
        }
    }

    override fun deleteAccount(password: String) {
        componentScope.launch {
            authStore.dispatch(AuthIntent.DeleteAccount(password))
        }
    }

    override fun refreshSession() {
        componentScope.launch {
            authStore.dispatch(AuthIntent.RefreshSession)
        }
    }

    override fun clearError() {
        componentScope.launch {
            authStore.dispatch(AuthIntent.ClearError)
        }
    }

    fun onDestroy() {
        componentScope.cancel()
    }
}
