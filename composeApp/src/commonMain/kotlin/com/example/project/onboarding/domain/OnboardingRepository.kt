package com.example.project.onboarding.domain

/** Repository for managing onboarding status and preferences */
interface OnboardingRepository {

    /** Check if the user has seen the onboarding */
    suspend fun hasSeenOnboarding(): Boolean

    /** Mark onboarding as completed */
    suspend fun markOnboardingCompleted()

    /** Reset onboarding status (for testing purposes) */
    suspend fun resetOnboardingStatus()
}
