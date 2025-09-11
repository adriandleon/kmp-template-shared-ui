package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.auth.domain.entity.SessionEvent
import com.example.project.auth.domain.entity.UserEntity
import com.example.project.auth.domain.entity.UserSession
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base interface for components that need to be aware of authentication state.
 *
 * This interface provides a clean way for components to access authentication context and react to
 * session changes.
 */
interface SessionAwareComponent : ComponentContext {
    /** Current session state */
    val currentSession: StateFlow<UserSession>

    /** Session events stream */
    val sessionEvents: SharedFlow<SessionEvent>

    /** Check if user is currently authenticated */
    val isAuthenticated: Boolean

    /** Get current user if authenticated */
    val currentUserEntity: com.example.project.auth.domain.entity.UserEntity?

    /** Get current access token if authenticated */
    val accessToken: String?

    /** Require authentication - navigate to auth flow if not authenticated */
    fun requireAuthentication()

    /** Navigate to home when user is authenticated */
    fun navigateToHome()
}

/**
 * Default implementation of SessionAwareComponent.
 *
 * This implementation delegates to the AuthenticatedComponentContext to provide session awareness
 * to components.
 */
internal class DefaultSessionAwareComponent(
    componentContext: ComponentContext,
    private val authContext: AuthenticatedComponentContext,
) : SessionAwareComponent, ComponentContext by componentContext {

    override val currentSession: StateFlow<UserSession> = authContext.currentSession
    override val sessionEvents: SharedFlow<SessionEvent> = authContext.sessionEvents
    override val isAuthenticated: Boolean = authContext.isAuthenticated
    override val currentUserEntity: UserEntity? = authContext.currentUserEntity
    override val accessToken: String? = authContext.accessToken

    override fun requireAuthentication() {
        authContext.requireAuthentication()
    }

    override fun navigateToHome() {
        authContext.navigateToHome()
    }
}
