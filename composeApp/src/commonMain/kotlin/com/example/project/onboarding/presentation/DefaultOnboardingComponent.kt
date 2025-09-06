package com.example.project.onboarding.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class DefaultOnboardingComponent(private val componentContext: ComponentContext) :
    OnboardingComponent, ComponentContext by componentContext {

    override val state: Value<OnboardingComponent.OnboardingState> =
        MutableValue(OnboardingComponent.OnboardingState())
}
