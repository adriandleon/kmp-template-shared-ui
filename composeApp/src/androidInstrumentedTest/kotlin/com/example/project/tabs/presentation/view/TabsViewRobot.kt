package com.example.project.tabs.presentation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.project.tabs.presentation.component.PreviewTabsComponent
import kotlin.test.assertTrue

fun ComposeContentTestRule.launchTabsView(
    component: PreviewTabsComponent,
    block: TabsViewRobot.() -> Unit = {},
): TabsViewRobot {
    setContent { TabsView(component) }
    return TabsViewRobot(this, component).apply(block)
}

class TabsViewRobot(
    private val rule: ComposeContentTestRule,
    private val component: PreviewTabsComponent,
) {
    infix fun verify(block: TabsViewVerification.() -> Unit): TabsViewVerification {
        rule.waitForIdle()
        return TabsViewVerification(rule, component).apply(block)
    }

    fun clickOnHomeBottomNavigation() {
        rule.onNodeWithTag("tag_home_navigation_bar_item").performClick()
    }

    fun clickOnContactBottomNavigation() {
        rule.onNodeWithTag("tag_contact_navigation_bar_item").performClick()
    }

    fun clickOnAboutBottomNavigation() {
        rule.onNodeWithTag("tag_about_navigation_bar_item").performClick()
    }
}

class TabsViewVerification(
    private val rule: ComposeContentTestRule,
    private val component: PreviewTabsComponent,
) {

    fun homeNavigationItemWasClicked() {
        assertTrue(component.homeTabWasCalled)
    }

    fun contactNavigationItemWasClicked() {
        assertTrue(component.contactTabWasCalled)
    }

    fun aboutNavigationItemWasClicked() {
        assertTrue(component.aboutTabWasCalled)
    }

    fun homeNavigationIconIsDisplayed() {
        rule.onNodeWithText("Home").assertIsDisplayed()
        rule.onNodeWithTag("tag_home_navigation_bar_item").assertIsSelectable()
    }

    fun contactNavigationIconIsDisplayed() {
        rule.onNodeWithText("Contact").assertIsDisplayed()
        rule.onNodeWithTag("tag_contact_navigation_bar_item").assertIsSelectable()
    }

    fun aboutNavigationIconIsDisplayed() {
        rule.onNodeWithText("About").assertIsDisplayed()
        rule.onNodeWithTag("tag_about_navigation_bar_item").assertIsSelectable()
    }
}
