package com.example.project.common.di

import com.arkivanov.decompose.ComponentContext
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.root.DefaultRootComponent
import com.example.project.root.RootComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/** Koin setup utilities for creating components with dependency injection */
object KoinSetup : KoinComponent {

    /** Create a RootComponent with proper dependency injection */
    fun createRootComponent(componentContext: ComponentContext): RootComponent {
        // Get the OnboardingRepository from Koin
        val onboardingRepository: OnboardingRepository by inject()

        return DefaultRootComponent(
            componentContext = componentContext,
            onboardingRepository = onboardingRepository,
        )
    }
}
