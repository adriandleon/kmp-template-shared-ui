package com.example.project.auth.domain.entity

/**
 * Represents the current user session state.
 *
 * This sealed interface provides a clear representation of authentication state
 * that can be observed throughout the application.
 */
sealed interface UserSession {
    /** User is not authenticated */
    object Unauthenticated : UserSession

    /** User is authenticated with session data */
    data class Authenticated(
        val user: User,
        val accessToken: String,
        val refreshToken: String? = null,
        val expiresAt: Long? = null
    ) : UserSession

    /** Session is being refreshed */
    object Refreshing : UserSession

    /** Session has expired and needs re-authentication */
    object Expired : UserSession
}

/**
 * Session events that can be emitted by the session manager.
 */
sealed interface SessionEvent {
    /** User successfully authenticated */
    data class UserSignedIn(val session: UserSession.Authenticated) : SessionEvent

    /** User signed out */
    object UserSignedOut : SessionEvent

    /** Session was refreshed */
    data class SessionRefreshed(val session: UserSession.Authenticated) : SessionEvent

    /** Session expired */
    object SessionExpired : SessionEvent

    /** Authentication failed */
    data class AuthenticationFailed(val error: AuthError) : SessionEvent
}
