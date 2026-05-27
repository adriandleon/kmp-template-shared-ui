package com.example.project.about.presentation.view

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.project.about.presentation.component.PreviewAboutComponent
import org.junit.Rule
import org.junit.Test

class AboutViewTest {
    @get:Rule val composeTestRule = createComposeRule()

    private val component = PreviewAboutComponent()

    @Test
    fun verifyTitleViewIsDisplayed() {
        composeTestRule.launchAboutView(component) {} verify { titleViewIsDisplayed() }
    }

    @Test
    fun verifyAboutViewTextIsDisplayed() {
        composeTestRule.launchAboutView(component) {} verify { aboutViewTextIsDisplayed() }
    }
}
