package com.example.project.about.presentation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.project.about.presentation.component.PreviewAboutComponent

fun ComposeContentTestRule.launchAboutView(
    component: PreviewAboutComponent,
    block: AboutViewRobot.() -> Unit = {},
): AboutViewRobot {
    setContent { AboutView(component) }
    return AboutViewRobot(this, component).apply(block)
}

class AboutViewRobot(
    private val rule: ComposeContentTestRule,
    private val component: PreviewAboutComponent,
) {
    infix fun verify(block: AboutViewVerification.() -> Unit): AboutViewVerification {
        rule.waitForIdle()
        return AboutViewVerification(rule, component).apply(block)
    }
}

class AboutViewVerification(
    private val rule: ComposeContentTestRule,
    private val component: PreviewAboutComponent,
) {
    fun titleViewIsDisplayed() {
        rule.onNodeWithTag("title_view_tag").assertIsDisplayed()
    }

    fun aboutViewTextIsDisplayed() {
        rule.onNodeWithText(component.title, ignoreCase = true).assertIsDisplayed()
    }
}
