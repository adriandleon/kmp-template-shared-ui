package com.example.project.auth.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project.auth.domain.entity.UserSession
import com.example.project.auth.presentation.component.SessionAwareComponent

/**
 * Example view demonstrating how to use the session-aware authentication middleware.
 *
 * This view shows how to integrate the authentication context into your Compose UI and react to
 * session state changes.
 */
@Composable
fun AuthExampleView(component: SessionAwareComponent, modifier: Modifier = Modifier) {
    val currentSession by component.currentSession.collectAsState()

    // Listen to session changes and handle navigation
    SessionListener(
        component = component,
        onSessionExpired = {
            // Navigate to login when session expires
            component.requireAuthentication()
        },
        onUserSignedIn = { session ->
            // Navigate to home when user signs in
            component.navigateToHome()
        },
        onUserSignedOut = {
            // Handle user sign out
        },
        onAuthenticationFailed = { error ->
            // Handle authentication errors
        },
    )

    MaterialTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (val session = currentSession) {
                    is UserSession.Authenticated -> {
                        AuthenticatedContent(component = component, session = session)
                    }
                    is UserSession.Unauthenticated -> {
                        UnauthenticatedContent(component = component)
                    }
                    is UserSession.Refreshing -> {
                        RefreshingContent()
                    }
                    is UserSession.Expired -> {
                        ExpiredContent(component = component)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthenticatedContent(
    component: SessionAwareComponent,
    session: UserSession.Authenticated,
) {
    Text(
        text = "Welcome, ${session.user.displayName ?: session.user.email}!",
        style = MaterialTheme.typography.headlineMedium,
    )

    Text(
        text = "You are authenticated and can access protected content.",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 8.dp),
    )

    Button(onClick = { /* Sign out logic */ }, modifier = Modifier.padding(top = 16.dp)) {
        Text("Sign Out")
    }
}

@Composable
private fun UnauthenticatedContent(component: SessionAwareComponent) {
    Text(text = "Please sign in to continue", style = MaterialTheme.typography.headlineMedium)

    Button(
        onClick = { component.requireAuthentication() },
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Text("Sign In")
    }
}

@Composable
private fun RefreshingContent() {
    CircularProgressIndicator()
    Text(
        text = "Refreshing session...",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp),
    )
}

@Composable
private fun ExpiredContent(component: SessionAwareComponent) {
    Text(text = "Your session has expired", style = MaterialTheme.typography.headlineMedium)

    Text(
        text = "Please sign in again to continue",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 8.dp),
    )

    Button(
        onClick = { component.requireAuthentication() },
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Text("Sign In Again")
    }
}
