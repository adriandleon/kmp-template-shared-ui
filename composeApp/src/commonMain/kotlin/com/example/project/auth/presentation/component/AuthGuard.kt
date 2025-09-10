package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.example.project.auth.domain.SessionManager
import com.example.project.auth.domain.entity.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

/**
 * Authentication guard component that protects routes based on authentication state.
 *
 * This component automatically navigates to authentication flow when the user
 * is not authenticated and the protected content requires authentication.
 */
interface AuthGuard : ComponentContext {
    /** Current slot state showing either protected content or auth flow */
    val slot: Value<ChildSlot<*, Child>>

    /** Child components that can be displayed */
    sealed class Child {
        /** Authentication flow (sign in/sign up) */
        data class Auth(val component: Any) : Child()

        /** Protected content that requires authentication */
        data class Protected(val component: Any) : Child()
    }
}

/**
 * Default implementation of AuthGuard.
 *
 * This implementation automatically manages navigation between authentication
 * and protected content based on the current session state.
 */
internal class DefaultAuthGuard(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val createAuthComponent: (AuthenticatedComponentContext) -> Any,
    private val createProtectedComponent: (AuthenticatedComponentContext) -> Any,
) : AuthGuard, ComponentContext by componentContext {

    private val navigation = SlotNavigation<Configuration>()

    override val slot: Value<ChildSlot<*, AuthGuard.Child>> = childSlot(
        source = navigation,
        serializer = Configuration.serializer(),
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private val scope = CoroutineScope(SupervisorJob())

    init {
        // Listen to session changes and navigate accordingly
        sessionManager.currentSession
            .onEach { session ->
                when (session) {
                    is UserSession.Authenticated -> {
                        navigation.activate(Configuration.Protected)
                    }
                    is UserSession.Unauthenticated,
                    is UserSession.Expired -> {
                        navigation.activate(Configuration.Auth)
                    }
                    is UserSession.Refreshing -> {
                        // Keep current state while refreshing
                    }
                }
            }
            .launchIn(scope)
    }

    private fun createChild(configuration: Configuration, context: ComponentContext): AuthGuard.Child {
        val authContext = DefaultAuthenticatedComponentContext(
            componentContext = context,
            sessionManager = sessionManager,
            onRequireAuthentication = { navigation.activate(Configuration.Auth) },
            onNavigateToHome = { navigation.activate(Configuration.Protected) }
        )

        return when (configuration) {
            is Configuration.Auth -> AuthGuard.Child.Auth(createAuthComponent(authContext))
            is Configuration.Protected -> AuthGuard.Child.Protected(createProtectedComponent(authContext))
        }
    }

    fun onDestroy() {
        scope.cancel()
    }

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object Auth : Configuration

        @Serializable
        data object Protected : Configuration
    }
}
