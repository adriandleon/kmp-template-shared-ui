package com.example.project.auth.domain

import com.example.project.auth.domain.entity.SessionEvent
import com.example.project.auth.domain.entity.UserEntity
import com.example.project.auth.domain.entity.UserSession
import com.example.project.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Default implementation of SessionManager using Supabase.
 *
 * This implementation provides a clean abstraction over Supabase authentication while maintaining
 * provider-agnostic interfaces.
 */
internal class DefaultSessionManager(private val authRepository: AuthRepository) : SessionManager {

    private val _currentSession = MutableStateFlow<UserSession>(UserSession.Unauthenticated)
    override val currentSession: StateFlow<UserSession> = _currentSession.asStateFlow()

    private val _sessionEvents = MutableSharedFlow<SessionEvent>()
    override val sessionEvents: SharedFlow<SessionEvent> = _sessionEvents.asSharedFlow()

    override val isAuthenticated: Boolean
        get() = _currentSession.value is UserSession.Authenticated

    override val currentUserEntity: UserEntity?
        get() = (_currentSession.value as? UserSession.Authenticated)?.userEntity

    override val accessToken: String?
        get() = (_currentSession.value as? UserSession.Authenticated)?.accessToken

    //    override suspend fun signIn(email: String, password: String): Result<Unit> {
    //        return handleAuthResult(authRepository.signInWithEmail(email, password))
    //    }
    //
    //    override suspend fun signUp(
    //        email: String,
    //        password: String,
    //        displayName: String?,
    //    ): Result<Unit> {
    //        return handleAuthResult(authRepository.signUpWithEmail(email, password, displayName))
    //    }
    //
    //    override suspend fun signInWithPhone(phone: String, password: String): Result<Unit> {
    //        return handleAuthResult(authRepository.signInWithPhone(phone, password))
    //    }
    //
    //    override suspend fun signUpWithPhone(
    //        phone: String,
    //        password: String,
    //        displayName: String?,
    //    ): Result<Unit> {
    //        return handleAuthResult(authRepository.signUpWithPhone(phone, password, displayName))
    //    }
    //
    //    override suspend fun signInWithOtp(identifier: String, otp: String): Result<Unit> {
    //        val result =
    //            if (identifier.contains("@")) {
    //                authRepository.signInWithEmailOtp(identifier, otp)
    //            } else {
    //                authRepository.signInWithPhoneOtp(identifier, otp)
    //            }
    //        return handleAuthResult(result)
    //    }
    //
    //    override suspend fun sendOtp(identifier: String): Result<Unit> {
    //        val result =
    //            if (identifier.contains("@")) {
    //                authRepository.sendEmailOtp(identifier)
    //            } else {
    //                authRepository.sendPhoneOtp(identifier)
    //            }
    //        return handleAuthResult(result)
    //    }
    //
    //    override suspend fun signOut(): Result<Unit> {
    //        return handleAuthResult(authRepository.signOut())
    //    }
    //
    //    override suspend fun refreshSession(): Result<Unit> {
    //        _currentSession.value = UserSession.Refreshing
    //        return handleAuthResult(authRepository.refreshSession())
    //    }
    //
    //    override suspend fun resetPassword(email: String): Result<Unit> {
    //        return handleAuthResult(authRepository.resetPassword(email))
    //    }
    //
    //    override suspend fun updateProfile(displayName: String?, avatarUrl: String?): Result<Unit>
    // {
    //        val result =
    //            when {
    //                displayName != null -> authRepository.updateDisplayName(displayName)
    //                avatarUrl != null -> authRepository.updateAvatarUrl(avatarUrl)
    //                else -> return Result.success(Unit)
    //            }
    //        return handleAuthResult(result)
    //    }
    //
    //    override suspend fun updatePassword(
    //        currentPassword: String,
    //        newPassword: String,
    //    ): Result<Unit> {
    //        return handleAuthResult(authRepository.updatePassword(currentPassword, newPassword))
    //    }
    //
    //    override suspend fun deleteAccount(password: String): Result<Unit> {
    //        return handleAuthResult(authRepository.deleteAccount(password))
    //    }
    //
    //    override fun clearError() {
    //        // Clear any error state if needed
    //    }
    //
    //    /** Handle authentication results and update session state. */
    //    @OptIn(ExperimentalTime::class)
    //    private suspend fun handleAuthResult(result: AuthResult): Result<Unit> {
    //        return when (result) {
    //            is AuthResult.Success -> {
    //                val session =
    //                    UserSession.Authenticated(
    //                        userEntity = result.userEntity,
    //                        accessToken =
    //                            "mock_token_${Clock.System.now().toEpochMilliseconds()}", //
    // Replace
    //                                                                                      // with
    // actual
    //                                                                                      // token
    //                        refreshToken = "mock_refresh_token",
    //                        expiresAt = Clock.System.now().plus(1.hours).toEpochMilliseconds(), //
    // 1 hour
    //                    )
    //                _currentSession.value = session
    //                _sessionEvents.emit(SessionEvent.UserSignedIn(session))
    //                Result.success(Unit)
    //            }
    //            is AuthResult.Error -> {
    //                _currentSession.value = UserSession.Unauthenticated
    //                _sessionEvents.emit(SessionEvent.AuthenticationFailed(result.error))
    //                Result.failure(Exception(result.error.toString()))
    //            }
    //            is AuthResult.Loading -> {
    //                _currentSession.value = UserSession.Refreshing
    //                Result.success(Unit)
    //            }
    //        }
    //    }
}
