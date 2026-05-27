package com.example.project.onboarding.presentation.view

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.project.onboarding.presentation.component.PreviewOnboardingComponent
import kotlin.test.assertTrue

fun ComposeContentTestRule.launchOnboardingView(
    component: PreviewOnboardingComponent,
    block: OnboardingViewRobot.() -> Unit = {},
): OnboardingViewRobot {
    setContent { OnboardingView(component) }
    return OnboardingViewRobot(this, component).apply(block)
}

class OnboardingViewRobot(
    private val rule: ComposeContentTestRule,
    private val component: PreviewOnboardingComponent,
) {
    infix fun verify(block: OnboardingViewVerification.() -> Unit): OnboardingViewVerification {
        rule.waitForIdle()
        return OnboardingViewVerification(rule, component).apply(block)
    }

    fun clickOnSkipOnboarding() {
        rule.onNodeWithTag("skip_onboarding_button").performClick()
    }
}

class OnboardingViewVerification(
    private val rule: ComposeContentTestRule,
    private val component: PreviewOnboardingComponent,
) {

    fun skipOnboardingWasClicked() {
        assertTrue(component.skipOnboardingWasCalled)
    }
}
