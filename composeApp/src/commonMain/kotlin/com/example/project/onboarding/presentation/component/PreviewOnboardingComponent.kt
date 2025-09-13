package com.example.project.onboarding.presentation.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

/**
 * A preview implementation of the [OnboardingComponent] interface. This class is used for preview
 * purposes and does not perform any actual operations.
 */
class PreviewOnboardingComponent : OnboardingComponent {

    override val state: Value<OnboardingComponent.OnboardingState> =
        MutableValue(OnboardingComponent.OnboardingState())

    override fun nextSlide() {
        // No-op for preview
    }

    override fun previousSlide() {
        // No-op for preview
    }

    override fun skipOnboarding() {
        // No-op for preview
    }

    override fun completeOnboarding() {
        // No-op for preview
    }
}
