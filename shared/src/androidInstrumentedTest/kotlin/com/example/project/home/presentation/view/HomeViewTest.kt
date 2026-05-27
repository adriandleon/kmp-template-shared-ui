package com.example.project.home.presentation.view

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.project.home.presentation.component.PreviewHomeComponent
import org.junit.Rule
import org.junit.Test

class HomeViewTest {

    @get:Rule val composeTestRule = createComposeRule()

    private val component = PreviewHomeComponent()

    @Test
    fun verifyHomeViewTextIsDisplayed() {
        composeTestRule.launchHomeView(component) {} verify { homeViewTextIsDisplayed() }
    }
}
