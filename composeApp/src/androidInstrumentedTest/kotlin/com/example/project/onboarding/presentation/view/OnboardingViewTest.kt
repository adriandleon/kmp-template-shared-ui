package com.example.project.onboarding.presentation.view

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.project.onboarding.presentation.component.PreviewOnboardingComponent
import org.junit.Rule
import org.junit.Test

class OnboardingViewTest {

    @get:Rule val composeTestRule = createComposeRule()

    private val component = PreviewOnboardingComponent()

    @Test
    fun testOnboardingView() {
        composeTestRule.launchOnboardingView(component) { clickOnSkipOnboarding() } verify
            {
                skipOnboardingWasClicked()
            }
    }
}
