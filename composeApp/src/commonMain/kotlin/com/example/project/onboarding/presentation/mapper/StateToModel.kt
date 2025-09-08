package com.example.project.onboarding.presentation.mapper

import com.example.project.onboarding.presentation.component.OnboardingComponent
import com.example.project.onboarding.presentation.store.OnboardingStore

internal val stateToModel: (OnboardingStore.State) -> OnboardingComponent.OnboardingState = {
    OnboardingComponent.OnboardingState(
        currentSlide = it.currentSlide,
        totalSlides = it.totalSlides,
        isLoading = it.isLoading,
        slides = it.slides,
    )
}
