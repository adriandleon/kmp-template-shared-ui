package com.example.project.onboarding.presentation

import com.arkivanov.decompose.value.Value

/**
 * Component for onboarding screen. This is shown to user the first time they open the app.
 *
 * @property state The state model for the onboarding screen.
 */
interface OnboardingComponent {
    val state: Value<OnboardingState>

    /**
     * Represents the state of the onboarding screen.
     *
     * @property currentSlide The index of the currently visible slide (starting from 0).
     * @property totalSlides The total number of slides in the onboarding flow.
     * @property isLoading True if the onboarding is performing a loading operation (e.g., saving
     *   status).
     * @property slides List of slides to be displayed in the onboarding flow.
     */
    data class OnboardingState(
        val currentSlide: Int = 0,
        val totalSlides: Int = 0,
        val isLoading: Boolean = false,
    )
}
