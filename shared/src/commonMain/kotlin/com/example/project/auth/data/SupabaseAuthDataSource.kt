package com.example.project.auth.data

import com.example.project.auth.data.mapper.AuthErrorMapper
import com.example.project.auth.data.mapper.UserMapper
import com.example.project.auth.domain.entity.AuthError
import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.domain.entity.UserEntity
import com.example.project.auth.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.providers.builtin.Phone
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Supabase implementation of the AuthRepository interface.
 *
 * This class provides all authentication operations using Supabase as the backend provider. It
 * implements the provider-agnostic AuthRepository interface, allowing easy switching between
 * different authentication providers in the future.
 */
class SupabaseAuthDataSource(private val supabase: SupabaseClient) : AuthRepository {
    private val _currentUserEntity = MutableStateFlow<UserEntity?>(null)
    private val scope = CoroutineScope(SupervisorJob())

    override val currentUserEntity: Flow<UserEntity?> = _currentUserEntity.asStateFlow()

    override val isAuthenticated: Flow<Boolean> = currentUserEntity.map { it != null }

    init {
        // Initialize current user from existing session
        val currentSession = supabase.auth.currentSessionOrNull()
        currentSession?.user?.let { user -> _currentUserEntity.value = UserMapper.toDomain(user) }

        // Start listening to session changes
        scope.launch { startSessionListener() }
    }

    private suspend fun startSessionListener() {
        supabase.auth.sessionStatus.collect { sessionStatus ->
            when (sessionStatus) {
                is SessionStatus.Authenticated -> {
                    sessionStatus.session.user?.let { user ->
                        _currentUserEntity.value = UserMapper.toDomain(user)
                    }
                }
                is SessionStatus.NotAuthenticated -> {
                    _currentUserEntity.value = null
                }
                else -> {
                    // Handle loading or error states if needed
                }
            }
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String?,
    ): AuthResult {
        return try {
            val result =
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    if (displayName != null) {
                        data = buildJsonObject { put("display_name", displayName) }
                    }
                }

            val user =
                result?.let { UserMapper.toDomain(it) }
                    ?: return AuthResult.Error(AuthError.GenericError("User creation failed"))
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signUpWithPhone(
        phone: String,
        password: String,
        displayName: String?,
    ): AuthResult {
        return try {
            val result =
                supabase.auth.signUpWith(Phone) {
                    this.phone = phone
                    this.password = password
                    if (displayName != null) {
                        data = buildJsonObject { put("display_name", displayName) }
                    }
                }

            val user =
                result?.let { UserMapper.toDomain(it) }
                    ?: return AuthResult.Error(AuthError.GenericError("User creation failed"))
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            val result =
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signInWithPhone(phone: String, password: String): AuthResult {
        return try {
            val result =
                supabase.auth.signInWith(Phone) {
                    this.phone = phone
                    this.password = password
                }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signInWithEmailOtp(email: String, otp: String): AuthResult {
        return try {
            val result =
                supabase.auth.verifyEmailOtp(
                    type = io.github.jan.supabase.auth.OtpType.Email.SIGNUP,
                    token = otp,
                    email = email,
                )

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signInWithPhoneOtp(phone: String, otp: String): AuthResult {
        return try {
            val result =
                supabase.auth.verifyPhoneOtp(
                    type = io.github.jan.supabase.auth.OtpType.Phone.SMS,
                    token = otp,
                    phone = phone,
                )

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun sendEmailOtp(email: String): AuthResult {
        return try {
            supabase.auth.signInWith(OTP) { this.email = email }
            AuthResult.Success(UserEntity("", "")) // Dummy success result for OTP sending
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun sendPhoneOtp(phone: String): AuthResult {
        return try {
            supabase.auth.signInWith(OTP) { this.phone = phone }
            AuthResult.Success(UserEntity("", "")) // Dummy success result for OTP sending
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun signOut(): AuthResult {
        return try {
            supabase.auth.signOut()
            _currentUserEntity.value = null
            AuthResult.Success(UserEntity("", "")) // Dummy success result
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun resetPassword(email: String): AuthResult {
        return try {
            supabase.auth.resetPasswordForEmail(email)
            AuthResult.Success(UserEntity("", "")) // Dummy success result for password reset
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun updatePassword(currentPassword: String, newPassword: String): AuthResult {
        return try {
            val result = supabase.auth.updateUser { password = newPassword }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun updateEmail(newEmail: String, password: String): AuthResult {
        return try {
            val result = supabase.auth.updateUser { email = newEmail }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun updatePhone(newPhone: String, password: String): AuthResult {
        return try {
            val result = supabase.auth.updateUser { phone = newPhone }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun updateDisplayName(displayName: String): AuthResult {
        return try {
            val result =
                supabase.auth.updateUser {
                    data = buildJsonObject { put("display_name", displayName) }
                }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun updateAvatarUrl(avatarUrl: String): AuthResult {
        return try {
            val result =
                supabase.auth.updateUser { data = buildJsonObject { put("avatar_url", avatarUrl) } }

            val user = UserMapper.toDomain(result)
            _currentUserEntity.value = user
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun deleteAccount(password: String): AuthResult {
        return try {
            // TODO: Implement account deletion - may require admin API or custom implementation
            // For now, just sign out the user
            supabase.auth.signOut()
            _currentUserEntity.value = null
            AuthResult.Success(UserEntity("", "")) // Dummy success result
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override suspend fun refreshSession(): AuthResult {
        return try {
            // For now, just return the current user since session refresh is handled automatically
            val currentUser = _currentUserEntity.value
            if (currentUser != null) {
                AuthResult.Success(currentUser)
            } else {
                AuthResult.Error(AuthError.GenericError("No user in session"))
            }
        } catch (e: Exception) {
            AuthResult.Error(AuthErrorMapper.toDomain(e))
        }
    }

    override fun getCurrentUser(): UserEntity? = _currentUserEntity.value

    override fun isUserAuthenticated(): Boolean = _currentUserEntity.value != null
}
