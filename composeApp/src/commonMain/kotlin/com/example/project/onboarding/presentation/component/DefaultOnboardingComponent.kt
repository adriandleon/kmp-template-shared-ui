package com.example.project.onboarding.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.example.project.common.util.DispatcherProvider
import com.example.project.common.util.asValue
import com.example.project.onboarding.presentation.component.OnboardingComponent.OnboardingState
import com.example.project.onboarding.presentation.mapper.stateToModel
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import com.example.project.onboarding.presentation.store.OnboardingStore.Label
import com.example.project.onboarding.presentation.store.OnboardingStoreFactory
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * This is the default implementation of the OnboardingComponent interface. It handles the
 * onboarding process, including navigation between slides, skipping the onboarding, and completing
 * the onboarding to navigate to the home screen.
 *
 * Default implementation of [OnboardingComponent]
 *
 * Deeplink URL: "example://app/onboarding?page={page}"
 */
internal class DefaultOnboardingComponent(
    private val componentContext: ComponentContext,
    private val onNavigateToHome: () -> Unit,
    initialPage: Int,
) : OnboardingComponent, ComponentContext by componentContext, KoinComponent {

    private val dispatcher = get<DispatcherProvider>()
    private val store =
        instanceKeeper.getStore { OnboardingStoreFactory(initialPage, get()).create() }

    override val state: Value<OnboardingState> = store.asValue().map(stateToModel)

    init {
        coroutineScope(dispatcher.main).launch { store.labels.collect(::observeLabels) }
    }

    override fun nextSlide() {
        store.accept(Intent.NextSlide)
    }

    override fun previousSlide() {
        store.accept(Intent.PreviousSlide)
    }

    override fun skipOnboarding() {
        store.accept(Intent.SkipOnboarding)
    }

    override fun completeOnboarding() {
        store.accept(Intent.CompleteOnboarding)
    }

    private fun observeLabels(label: Label) {
        when (label) {
            Label.NavigateToHome -> onNavigateToHome()
        }
    }
}
