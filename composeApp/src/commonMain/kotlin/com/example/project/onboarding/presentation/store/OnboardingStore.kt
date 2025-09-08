package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.example.project.onboarding.domain.entity.SlideEntity
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import com.example.project.onboarding.presentation.store.OnboardingStore.Label
import com.example.project.onboarding.presentation.store.OnboardingStore.State

internal interface OnboardingStore : Store<Intent, State, Label> {

    data class State(
        val currentSlide: Int = 0,
        val totalSlides: Int = 0,
        val isLoading: Boolean = false,
        val slides: List<SlideEntity> = emptyList(),
    )

    sealed interface Intent {
        data object SkipOnboarding : Intent

        data object CompleteOnboarding : Intent

        data object PreviousSlide : Intent

        data object NextSlide : Intent
    }

    sealed interface Label

    sealed interface Message {
        data object OnPreviousClicked : Message

        data object OnNextClicked : Message

        data class LoadSlides(val slides: List<SlideEntity>) : Message
    }

    sealed interface Action {
        data class LoadSlides(val slides: List<SlideEntity>) : Action
    }
}
