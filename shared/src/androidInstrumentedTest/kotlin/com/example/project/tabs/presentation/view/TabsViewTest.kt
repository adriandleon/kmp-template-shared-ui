package com.example.project.tabs.presentation.view

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.project.tabs.presentation.component.PreviewTabsComponent
import org.junit.Rule
import org.junit.Test

class TabsViewTest {
    @get:Rule val composeTestRule = createComposeRule()

    private val component = PreviewTabsComponent()

    @Test
    fun verifyHomeNavigationIconIsDisplayed() {
        composeTestRule.launchTabsView(component) verify { homeNavigationIconIsDisplayed() }
    }

    @Test
    fun verifyContactNavigationIconIsDisplayed() {
        composeTestRule.launchTabsView(component) verify { contactNavigationIconIsDisplayed() }
    }

    @Test
    fun verifyAboutNavigationIconIsDisplayed() {
        composeTestRule.launchTabsView(component) verify { aboutNavigationIconIsDisplayed() }
    }

    @Test
    fun verifyClickActionHomeNavigationBar() {
        composeTestRule.launchTabsView(component) { clickOnHomeBottomNavigation() } verify
            {
                homeNavigationItemWasClicked()
            }
    }

    @Test
    fun verifyClickActionContactNavigationBar() {
        composeTestRule.launchTabsView(component) { clickOnContactBottomNavigation() } verify
            {
                contactNavigationItemWasClicked()
            }
    }

    @Test
    fun verifyClickActionAboutNavigationBar() {
        composeTestRule.launchTabsView(component) { clickOnAboutBottomNavigation() } verify
            {
                aboutNavigationItemWasClicked()
            }
    }
}
