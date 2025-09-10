package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.example.project.auth.domain.SessionManager
import kotlinx.serialization.Serializable

/**
 * Example root component that demonstrates how to use the authentication middleware.
 *
 * This component shows how to integrate the session manager and auth guard into your application's
 * root component.
 */
interface AuthRootComponent : ComponentContext {
    /** Current slot state showing either auth guard or other content */
    val slot: Value<ChildSlot<*, Child>>

    /** Child components that can be displayed */
    sealed class Child {
        /** Authentication guard that manages auth state */
        data class AuthGuard(
            val component: com.example.project.auth.presentation.component.AuthGuard
        ) : Child()

        /** Other root-level content */
        data class OtherContent(val component: Any) : Child()
    }
}

/**
 * Default implementation of AuthRootComponent.
 *
 * This implementation demonstrates how to set up the authentication middleware at the root level of
 * your application.
 */
internal class DefaultAuthRootComponent(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val createAuthComponent: (AuthenticatedComponentContext) -> Any,
    private val createProtectedComponent: (AuthenticatedComponentContext) -> Any,
    private val createOtherContent: () -> Any,
) : AuthRootComponent, ComponentContext by componentContext {

    private val navigation = SlotNavigation<Configuration>()

    override val slot: Value<ChildSlot<*, AuthRootComponent.Child>> =
        childSlot(
            source = navigation,
            serializer = Configuration.serializer(),
            handleBackButton = true,
            childFactory = ::createChild,
        )

    init {
        // Start with auth guard to handle authentication
        navigation.activate(Configuration.AuthGuard)
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext,
    ): AuthRootComponent.Child {
        return when (configuration) {
            is Configuration.AuthGuard -> {
                val authGuard =
                    DefaultAuthGuard(
                        componentContext = context,
                        sessionManager = sessionManager,
                        createAuthComponent = createAuthComponent,
                        createProtectedComponent = createProtectedComponent,
                    )
                AuthRootComponent.Child.AuthGuard(authGuard)
            }
            is Configuration.OtherContent -> {
                AuthRootComponent.Child.OtherContent(createOtherContent())
            }
        }
    }

    @Serializable
    private sealed interface Configuration {
        @Serializable data object AuthGuard : Configuration

        @Serializable data object OtherContent : Configuration
    }
}

/** Example of how to create an AuthenticatedComponentContext for child components. */
fun createAuthenticatedContext(
    componentContext: ComponentContext,
    sessionManager: SessionManager,
    onRequireAuthentication: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
): AuthenticatedComponentContext {
    return DefaultAuthenticatedComponentContext(
        componentContext = componentContext,
        sessionManager = sessionManager,
        onRequireAuthentication = onRequireAuthentication,
        onNavigateToHome = onNavigateToHome,
    )
}
