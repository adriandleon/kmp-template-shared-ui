package com.example.project.auth.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.project.auth.domain.repository.AuthRepository
import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.presentation.store.AuthStore.Action
import com.example.project.auth.presentation.store.AuthStore.Intent
import com.example.project.auth.presentation.store.AuthStore.Label
import com.example.project.auth.presentation.store.AuthStore.Message
import com.example.project.auth.presentation.store.AuthStore.Message.AuthResultReceived
import com.example.project.auth.presentation.store.AuthStore.Message.SetLoading
import com.example.project.auth.presentation.store.AuthStore.Message.UserChanged
import com.example.project.auth.presentation.store.AuthStore.State
import com.example.project.common.util.DispatcherProvider
import kotlinx.coroutines.launch

internal class AuthExecutor(
    private val authRepository: AuthRepository,
    private val dispatcher: DispatcherProvider,
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext = dispatcher.default) {

    override fun executeAction(action: Action) {
        when (action) {
            is Action.UserChanged -> dispatch(UserChanged(action.userEntity))
            is Action.AuthResultReceived -> {}
            Action.ClearError -> {}
            is Action.SetLoading -> {}
        }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.SignUpWithEmail -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result =
                        authRepository.signUpWithEmail(
                            intent.email,
                            intent.password,
                            intent.displayName,
                        )
                    dispatch(AuthResultReceived(result))
                }
            }

            is Intent.ClearError -> {
                dispatch(Message.ClearError)
            }
            is Intent.DeleteAccount -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.deleteAccount(intent.password)
                    dispatch(AuthResultReceived(result))
                    if (result is AuthResult.Success) {
                        publish(Label.ShowAccountDeleted)
                        publish(Label.NavigateToAuth)
                    }
                }
            }
            Intent.RefreshSession -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.refreshSession()
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.ResetPassword -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.resetPassword(intent.email)
                    dispatch(AuthResultReceived(result))
                    if (result is AuthResult.Success) {
                        publish(Label.ShowPasswordResetSent)
                    }
                }
            }
            is Intent.SendEmailOtp -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.sendEmailOtp(intent.email)
                    dispatch(AuthResultReceived(result))
                    if (result is AuthResult.Success) {
                        publish(Label.ShowOtpSent)
                    }
                }
            }
            is Intent.SendPhoneOtp -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.sendPhoneOtp(intent.phone)
                    dispatch(AuthResultReceived(result))
                    if (result is AuthResult.Success) {
                        publish(Label.ShowOtpSent)
                    }
                }
            }
            is Intent.SignInWithEmail -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.signInWithEmail(intent.email, intent.password)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.SignInWithEmailOtp -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.signInWithEmailOtp(intent.email, intent.otp)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.SignInWithPhone -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.signInWithPhone(intent.phone, intent.password)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.SignInWithPhoneOtp -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.signInWithPhoneOtp(intent.phone, intent.otp)
                    dispatch(AuthResultReceived(result))
                }
            }
            Intent.SignOut -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.signOut()
                    dispatch(AuthResultReceived(result))
                    if (result is AuthResult.Success) {
                        publish(Label.NavigateToAuth)
                    }
                }
            }
            is Intent.SignUpWithPhone -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result =
                        authRepository.signUpWithPhone(
                            intent.phone,
                            intent.password,
                            intent.displayName,
                        )
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.UpdateAvatarUrl -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.updateAvatarUrl(intent.avatarUrl)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.UpdateDisplayName -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.updateDisplayName(intent.displayName)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.UpdateEmail -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.updateEmail(intent.newEmail, intent.password)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.UpdatePassword -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result =
                        authRepository.updatePassword(intent.currentPassword, intent.newPassword)
                    dispatch(AuthResultReceived(result))
                }
            }
            is Intent.UpdatePhone -> {
                dispatch(SetLoading(true))
                scope.launch {
                    val result = authRepository.updatePhone(intent.newPhone, intent.password)
                    dispatch(AuthResultReceived(result))
                }
            }
        }
    }
}
