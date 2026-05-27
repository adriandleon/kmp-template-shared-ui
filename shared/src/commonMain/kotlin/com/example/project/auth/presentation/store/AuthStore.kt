package com.example.project.auth.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.project.auth.domain.entity.AuthError
import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.domain.entity.UserEntity

/**
 * Authentication Store interface following MVIKotlin pattern.
 *
 * This store manages authentication state and provides a reactive interface for authentication
 * operations using the MVIKotlin framework.
 */
interface AuthStore : Store<AuthStore.Intent, AuthStore.State, AuthStore.Label> {

    /** User actions that can be dispatched to the store. */
    sealed interface Intent {
        data class SignUpWithEmail(
            val email: String,
            val password: String,
            val displayName: String? = null,
        ) : Intent

        data class SignUpWithPhone(
            val phone: String,
            val password: String,
            val displayName: String? = null,
        ) : Intent

        data class SignInWithEmail(val email: String, val password: String) : Intent

        data class SignInWithPhone(val phone: String, val password: String) : Intent

        data class SignInWithEmailOtp(val email: String, val otp: String) : Intent

        data class SignInWithPhoneOtp(val phone: String, val otp: String) : Intent

        data class SendEmailOtp(val email: String) : Intent

        data class SendPhoneOtp(val phone: String) : Intent

        object SignOut : Intent

        data class ResetPassword(val email: String) : Intent

        data class UpdatePassword(val currentPassword: String, val newPassword: String) : Intent

        data class UpdateEmail(val newEmail: String, val password: String) : Intent

        data class UpdatePhone(val newPhone: String, val password: String) : Intent

        data class UpdateDisplayName(val displayName: String) : Intent

        data class UpdateAvatarUrl(val avatarUrl: String) : Intent

        data class DeleteAccount(val password: String) : Intent

        object RefreshSession : Intent

        object ClearError : Intent
    }

    /** Current authentication state. */
    data class State(
        val userEntity: UserEntity? = null,
        val isAuthenticated: Boolean = false,
        val isLoading: Boolean = false,
        val error: AuthError? = null,
    )

    /** Labels for side effects that need to be handled by the UI. */
    sealed interface Label {
        object NavigateToHome : Label

        object NavigateToAuth : Label

        object ShowPasswordResetSent : Label

        object ShowOtpSent : Label

        object ShowAccountDeleted : Label
    }

    /** Internal messages for state updates. */
    sealed interface Message {
        data class SetLoading(val isLoading: Boolean) : Message

        data class UserChanged(val userEntity: UserEntity?) : Message

        data class AuthResultReceived(val result: AuthResult) : Message

        object ClearError : Message
    }

    /** Internal actions for store communication. */
    sealed interface Action {
        data class SetLoading(val isLoading: Boolean) : Action

        data class UserChanged(val userEntity: UserEntity?) : Action

        data class AuthResultReceived(val result: AuthResult) : Action

        object ClearError : Action
    }
}
