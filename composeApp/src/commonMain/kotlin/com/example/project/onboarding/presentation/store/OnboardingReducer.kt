package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.example.project.onboarding.presentation.store.OnboardingStore.Message
import com.example.project.onboarding.presentation.store.OnboardingStore.State

internal object OnboardingReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.OnPreviousClicked ->
                copy(currentSlide = (currentSlide - 1).coerceIn(0, totalSlides - 1))
            is Message.OnNextClicked ->
                copy(currentSlide = (currentSlide + 1).coerceIn(0, totalSlides - 1))
            is Message.LoadSlides ->
                copy(slides = msg.slides, totalSlides = msg.slides.size, isLoading = false)
        }
}
