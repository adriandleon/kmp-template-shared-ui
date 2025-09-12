package com.example.project.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.example.project.auth.presentation.component.AuthComponent
import com.example.project.common.util.Url
import com.example.project.onboarding.presentation.component.OnboardingComponent
import com.example.project.tabs.presentation.component.TabsComponent

/**
 * The root component
 *
 * @see DefaultRootComponent
 */
interface RootComponent : BackHandlerOwner {

    val slot: Value<ChildSlot<*, Child>>

    /** Function to be called when navigation to home should occur */
    fun onNavigateToHome()

    /** Function to handle deeplink URLs */
    fun handleDeepLink(deepLinkUrl: Url)

    sealed interface Child {
        /** @param component Child component for the auth screen */
        data class Auth(val component: AuthComponent) : Child

        /** @param component Child component for the onboarding screen */
        data class Onboarding(val component: OnboardingComponent) : Child

        /** @param component Child component for the home bottom navigation container */
        data class Tabs(val component: TabsComponent) : Child
    }
}
