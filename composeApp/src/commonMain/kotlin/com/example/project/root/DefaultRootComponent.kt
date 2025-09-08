package com.example.project.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.example.project.BuildKonfig
import com.example.project.home.DefaultHomeComponent
import com.example.project.home.HomeComponent
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.presentation.component.DefaultOnboardingComponent
import com.example.project.onboarding.presentation.component.OnboardingComponent
import com.example.project.root.DefaultRootComponent.Configuration.Home
import com.example.project.root.DefaultRootComponent.Configuration.Onboarding
import com.example.project.root.RootComponent.Child
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val onboardingRepository: OnboardingRepository,
) : RootComponent, ComponentContext by componentContext {

    private val hasSeenOnboarding = runBlocking {
        if (BuildKonfig.DEBUG) {
            onboardingRepository.resetOnboardingStatus()
        }
        onboardingRepository.hasSeenOnboarding()
    }
    private val navigation = StackNavigation<Configuration>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = if (hasSeenOnboarding) Home else Onboarding,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override fun onNavigateToHome() {
        navigation.replaceCurrent(Home)
    }

    private fun createChild(configuration: Configuration, context: ComponentContext): Child =
        when (configuration) {
            is Onboarding -> {
                Child.Onboarding(onboardingComponent(context))
            }
            is Home -> {
                Child.Home(homeComponent(context))
            }
        }

    private fun onboardingComponent(componentContext: ComponentContext): OnboardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            onNavigateToHome = ::onNavigateToHome,
        )

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(componentContext = componentContext)

    @Serializable
    private sealed interface Configuration {

        @Serializable data object Onboarding : Configuration

        @Serializable data object Home : Configuration
    }
}
