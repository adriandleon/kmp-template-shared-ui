package com.example.project.contact.presentation.component

/**
 * Interface for the contact component.
 *
 * This interface defines the contract for the contact component, including the event to go back to
 * the previous screen and the title of the contact screen.
 */
interface ContactComponent {
    /** Event to go back to the previous screen. */
    val onBackClicked: () -> Unit
    /** Title of the contact screen. */
    val title: String
}