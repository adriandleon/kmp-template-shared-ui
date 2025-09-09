package com.example.project.auth.domain.entity

/**
 * Represents an authenticated user in the domain layer.
 *
 * This entity is provider-agnostic and contains only the essential user information
 * needed by the application. It abstracts away provider-specific details.
 *
 * @property id Unique identifier for the user
 * @property email User's email address
 * @property phone User's phone number (optional)
 * @property displayName User's display name (optional)
 * @property avatarUrl URL to user's avatar image (optional)
 * @property isEmailVerified Whether the user's email is verified
 * @property isPhoneVerified Whether the user's phone is verified
 * @property createdAt Timestamp when the user was created
 * @property lastSignInAt Timestamp of the last sign-in
 */
data class User(
    val id: String,
    val email: String,
    val phone: String? = null,
    val displayName: String? = null,
    val avatarUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val createdAt: Long = 0L,
    val lastSignInAt: Long = 0L,
)
