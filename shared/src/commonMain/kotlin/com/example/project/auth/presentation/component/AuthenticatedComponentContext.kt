package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.auth.domain.SessionManager
import com.example.project.auth.domain.entity.SessionEvent
import com.example.project.auth.domain.entity.UserEntity
import com.example.project.auth.domain.entity.UserSession
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Custom ComponentContext that provides authentication context to all components.
 *
 * This follows the pattern suggested in the Decompose discussions for handling authentication
 * across the component tree. Components can access the current session state and listen to
 * authentication events.
 */
internal interface AuthenticatedComponentContext : ComponentContext {
    /** Session manager for authentication operations */
    val sessionManager: SessionManager

    /** Current user session state */
    val currentSession: StateFlow<UserSession>

    /** Session events stream */
    val sessionEvents: SharedFlow<SessionEvent>

    /** Check if user is currently authenticated */
    val isAuthenticated: Boolean

    /** Get current user if authenticated */
    val currentUserEntity: UserEntity?

    /** Get current access token if authenticated */
    val accessToken: String?

    /** Navigate to authentication flow when session is required */
    fun requireAuthentication()

    /** Navigate to home when user is authenticated */
    fun navigateToHome()
}

/**
 * Default implementation of AuthenticatedComponentContext.
 *
 * This implementation provides authentication context to all child components while maintaining the
 * standard ComponentContext functionality.
 */
internal class DefaultAuthenticatedComponentContext(
    componentContext: ComponentContext,
    override val sessionManager: SessionManager,
    private val onRequireAuthentication: () -> Unit,
    private val onNavigateToHome: () -> Unit,
) : AuthenticatedComponentContext, ComponentContext by componentContext {

    override val currentSession: StateFlow<UserSession> = sessionManager.currentSession
    override val sessionEvents: SharedFlow<SessionEvent> = sessionManager.sessionEvents
    override val isAuthenticated: Boolean = sessionManager.isAuthenticated
    override val currentUserEntity: UserEntity? = sessionManager.currentUserEntity
    override val accessToken: String? = sessionManager.accessToken

    override fun requireAuthentication() {
        onRequireAuthentication()
    }

    override fun navigateToHome() {
        onNavigateToHome()
    }
}
