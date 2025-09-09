package com.example.project.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.presentation.component.DefaultOnboardingComponent
import com.example.project.onboarding.presentation.component.OnboardingComponent
import com.example.project.root.DefaultRootComponent.Configuration.Onboarding
import com.example.project.root.DefaultRootComponent.Configuration.Tabs
import com.example.project.root.RootComponent.Child
import com.example.project.tabs.presentation.component.DefaultTabsComponent
import com.example.project.tabs.presentation.component.TabsComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val onboardingRepository: OnboardingRepository,
) : RootComponent, ComponentContext by componentContext {

    private val hasSeenOnboarding = runBlocking { onboardingRepository.hasSeenOnboarding() }
    private val navigation = SlotNavigation<Configuration>()

    override val slot: Value<ChildSlot<*, Child>> =
        childSlot(
            source = navigation,
            serializer = Configuration.serializer(),
            handleBackButton = true,
            childFactory = ::createChild,
        )

    init {
        navigation.activate(if (hasSeenOnboarding) Tabs else Onboarding)
    }

    override fun onNavigateToHome() {
        navigation.activate(Tabs)
    }

    private fun createChild(configuration: Configuration, context: ComponentContext): Child =
        when (configuration) {
            is Onboarding -> {
                Child.Onboarding(onboardingComponent(context))
            }
            is Tabs -> {
                Child.Tabs(tabsComponent(context))
            }
        }

    private fun onboardingComponent(componentContext: ComponentContext): OnboardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            onNavigateToHome = ::onNavigateToHome,
        )

    private fun tabsComponent(componentContext: ComponentContext): TabsComponent =
        DefaultTabsComponent(componentContext = componentContext)

    @Serializable
    private sealed interface Configuration {

        @Serializable data object Onboarding : Configuration

        @Serializable data object Tabs : Configuration
    }
}
