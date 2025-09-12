package com.example.project.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.example.project.auth.domain.repository.AuthRepository
import com.example.project.auth.presentation.component.AuthComponent
import com.example.project.auth.presentation.component.DefaultAuthComponentFactory
import com.example.project.common.util.DispatcherProvider
import com.example.project.common.util.Url
import com.example.project.common.util.consumePathSegment
import com.example.project.common.util.pathSegmentOf
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.presentation.component.DefaultOnboardingComponent
import com.example.project.onboarding.presentation.component.OnboardingComponent
import com.example.project.root.DefaultRootComponent.Configuration.Auth
import com.example.project.root.DefaultRootComponent.Configuration.Onboarding
import com.example.project.root.DefaultRootComponent.Configuration.Tabs
import com.example.project.root.RootComponent.Child
import com.example.project.tabs.presentation.component.DefaultTabsComponent
import com.example.project.tabs.presentation.component.TabsComponent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val dispatcher: DispatcherProvider,
    deepLinkUrl: Url? = null,
) : RootComponent, ComponentContext by componentContext {

    //    private val hasSeenOnboarding = runBlocking {
    //        onboardingRepository.resetOnboardingStatus()
    //        onboardingRepository.hasSeenOnboarding()
    //    }
    private val navigation = SlotNavigation<Configuration>()

    override val slot: Value<ChildSlot<*, Child>> =
        childSlot(
            source = navigation,
            serializer = Configuration.serializer(),
            childFactory = ::createChild,
            initialConfiguration = { getInitialStack(deepLinkUrl).first() },
        )

    init {
        //        // Start observing authentication state
        //        coroutineScope(dispatcher.default).launch {
        //            authRepository.isAuthenticated.collect { isAuthenticated ->
        //                withContext(dispatcher.main) { navigateBasedOnAuthState(isAuthenticated) }
        //            }
        //        }
        //
        //        // Initial navigation based on current state
        //        val isCurrentlyAuthenticated = runBlocking { authRepository.isUserAuthenticated()
        // }
        //        coroutineScope(dispatcher.main).launch {
        //            navigateBasedOnAuthState(isCurrentlyAuthenticated)
        //        }
    }

    override fun onNavigateToHome() {
        // When navigating to home, check current authentication and onboarding state
        coroutineScope(dispatcher.default).launch {
            val isAuthenticated = authRepository.isUserAuthenticated()
            withContext(dispatcher.main) { navigateBasedOnAuthState(isAuthenticated) }
        }
    }

    private suspend fun navigateBasedOnAuthState(isAuthenticated: Boolean) {
        when {
            !isAuthenticated -> {
                // User is not logged in, navigate to Auth
                navigation.activate(Auth)
            }
            onboardingRepository.hasSeenOnboarding() -> {
                // User is authenticated and has seen onboarding, navigate to Tabs
                navigation.activate(Tabs())
            }
            else -> {
                // User is authenticated but hasn't seen onboarding, navigate to Onboarding
                navigation.activate(Onboarding)
            }
        }
    }

    private fun createChild(configuration: Configuration, context: ComponentContext): Child =
        when (configuration) {
            is Auth -> {
                Child.Auth(authComponent(context))
            }
            is Onboarding -> {
                Child.Onboarding(onboardingComponent(context))
            }
            is Tabs -> {
                Child.Tabs(tabsComponent(context, configuration.deepLinkUrl))
            }
        }

    private fun authComponent(componentContext: ComponentContext): AuthComponent =
        DefaultAuthComponentFactory().create(componentContext)

    private fun onboardingComponent(componentContext: ComponentContext): OnboardingComponent =
        DefaultOnboardingComponent(
            componentContext = componentContext,
            onNavigateToHome = ::onNavigateToHome,
        )

    private fun tabsComponent(
        componentContext: ComponentContext,
        deepLinkUrl: Url? = null,
    ): TabsComponent =
        DefaultTabsComponent(componentContext = componentContext, deepLinkUrl = deepLinkUrl)

    private fun getInitialStack(deepLinkUrl: Url?): List<Configuration> {
        val (path, childUrl) = deepLinkUrl?.consumePathSegment() ?: return listOf(Tabs())

        return when (path) {
            pathSegmentOf<Auth>() -> listOf(Auth)
            pathSegmentOf<Onboarding>() -> listOf(Onboarding)
            pathSegmentOf<Tabs>() -> listOf(Tabs(deepLinkUrl = childUrl))
            else -> listOf(Tabs(deepLinkUrl = childUrl))
        }
    }

    @Serializable
    private sealed interface Configuration {

        @Serializable data object Auth : Configuration

        @Serializable data object Onboarding : Configuration

        @Serializable data class Tabs(val deepLinkUrl: Url? = null) : Configuration
    }
}
