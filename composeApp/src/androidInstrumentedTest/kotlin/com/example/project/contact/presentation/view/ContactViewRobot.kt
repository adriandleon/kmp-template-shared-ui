package com.example.project.contact.presentation.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.project.contact.presentation.component.PreviewContactComponent

fun ComposeContentTestRule.launchContactView(
    component: PreviewContactComponent,
    block: ContactViewRobot.() -> Unit = {},
): ContactViewRobot {
    setContent { ContactView(component) }
    return ContactViewRobot(this, component).apply(block)
}

class ContactViewRobot(
    private val rule: ComposeContentTestRule,
    private val component: PreviewContactComponent,
) {
    infix fun verify(block: ContactViewVerification.() -> Unit): ContactViewVerification {
        rule.waitForIdle()
        return ContactViewVerification(rule, component).apply(block)
    }
}

class ContactViewVerification(
    private val rule: ComposeContentTestRule,
    private val component: PreviewContactComponent,
) {

    fun contactViewTextIsDisplayed() {
        rule.onNodeWithText(component.title, ignoreCase = true).assertIsDisplayed()
    }

    fun descriptionTestIsDisplayed() {
        rule.onNodeWithTag("contact_description_text").assertIsDisplayed()
    }
}
