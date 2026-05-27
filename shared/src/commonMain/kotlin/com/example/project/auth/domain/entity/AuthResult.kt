package com.example.project.auth.domain.entity

/**
 * Represents the result of an authentication operation.
 *
 * This sealed interface provides type-safe handling of authentication results, allowing the
 * presentation layer to handle success and error cases appropriately.
 */
sealed interface AuthResult {
    /** Successful authentication result containing the authenticated user. */
    data class Success(val userEntity: UserEntity) : AuthResult

    /** Failed authentication result containing error information. */
    data class Error(val error: AuthError) : AuthResult

    /** Loading state during authentication operations. */
    object Loading : AuthResult
}
