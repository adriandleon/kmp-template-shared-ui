package com.example.project.home.presentation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.project.home.presentation.component.PreviewHomeComponent

fun ComposeContentTestRule.launchHomeView(
    component: PreviewHomeComponent,
    block: HomeViewRobot.() -> Unit = {},
): HomeViewRobot {
    setContent { HomeView(component) }
    return HomeViewRobot(this, component).apply(block)
}

class HomeViewRobot(
    private val rule: ComposeContentTestRule,
    private val component: PreviewHomeComponent,
) {
    infix fun verify(block: HomeViewVerification.() -> Unit): HomeViewVerification {
        rule.waitForIdle()
        return HomeViewVerification(rule, component).apply(block)
    }
}

class HomeViewVerification(
    private val rule: ComposeContentTestRule,
    private val component: PreviewHomeComponent,
) {
    fun titleViewIsDisplayed() {
        rule.onNodeWithTag("title_view_tag").assertIsDisplayed()
    }

    fun homeViewTextIsDisplayed() {
        rule.onNodeWithText(component.title, ignoreCase = true).assertIsDisplayed()
    }
}
