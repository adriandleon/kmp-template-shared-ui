package com.example.project.auth.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.project.auth.domain.entity.SessionEvent
import com.example.project.auth.domain.entity.UserSession
import com.example.project.auth.presentation.component.SessionAwareComponent
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable that listens to session changes and handles navigation.
 *
 * This composable can be used in any view that needs to react to authentication state changes, such
 * as automatically navigating to login when session expires.
 */
@Composable
fun SessionListener(
    component: SessionAwareComponent,
    onSessionExpired: () -> Unit = {},
    onUserSignedIn: (UserSession.Authenticated) -> Unit = {},
    onUserSignedOut: () -> Unit = {},
    onAuthenticationFailed: (com.example.project.auth.domain.entity.AuthError) -> Unit = {},
) {
    val currentSession by component.currentSession.collectAsState()

    // Handle session state changes
    LaunchedEffect(currentSession) {
        when (val session = currentSession) {
            is UserSession.Expired -> {
                onSessionExpired()
            }
            is UserSession.Authenticated -> {
                onUserSignedIn(session)
            }
            is UserSession.Unauthenticated -> {
                onUserSignedOut()
            }
            is UserSession.Refreshing -> {
                // Handle refreshing state if needed
            }
        }
    }

    // Handle session events
    LaunchedEffect(Unit) {
        component.sessionEvents.collectLatest { event ->
            when (event) {
                is SessionEvent.SessionExpired -> onSessionExpired()
                is SessionEvent.UserSignedIn -> onUserSignedIn(event.session)
                is SessionEvent.UserSignedOut -> onUserSignedOut()
                is SessionEvent.AuthenticationFailed -> onAuthenticationFailed(event.error)
                is SessionEvent.SessionRefreshed -> {
                    // Handle session refresh if needed
                }
            }
        }
    }
}

/**
 * Composable that provides session state to child composables.
 *
 * This composable can be used to wrap content that needs access to authentication state without
 * requiring a full component.
 */
@Composable
fun SessionProvider(component: SessionAwareComponent, content: @Composable (UserSession) -> Unit) {
    val currentSession by component.currentSession.collectAsState()
    content(currentSession)
}
