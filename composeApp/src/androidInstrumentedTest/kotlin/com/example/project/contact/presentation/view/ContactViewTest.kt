package com.example.project.contact.presentation.view

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.project.contact.presentation.component.PreviewContactComponent
import org.junit.Rule
import org.junit.Test

class ContactViewTest {

    @get:Rule val composeTestRule = createComposeRule()

    private val component = PreviewContactComponent()

    @Test
    fun verifyContactViewTextIsDisplayed() {
        composeTestRule.launchContactView(component) {} verify { contactViewTextIsDisplayed() }
    }

    @Test
    fun verifyDescriptionTextIsDisplayed() {
        composeTestRule.launchContactView(component) {} verify { descriptionTestIsDisplayed() }
    }
}
