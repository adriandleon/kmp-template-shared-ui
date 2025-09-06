package com.example.project.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.example.project.home.HomeComponent
import com.example.project.onboarding.presentation.OnboardingComponent

/**
 * The root component
 *
 * @see DefaultRootComponent
 */
interface RootComponent : BackHandlerOwner {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        /** @param component Child component for the onboarding screen */
        data class Onboarding(val component: OnboardingComponent) : Child

        /** @param component Child component for the home screen */
        data class Home(val component: HomeComponent) : Child
    }
}
