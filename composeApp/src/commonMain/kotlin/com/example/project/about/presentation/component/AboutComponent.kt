package com.example.project.about.presentation.component

/** Component to display the about screen. */
interface AboutComponent {
    /** Event to go back to the previous screen. */
    val onBackClicked: () -> Unit
    /** Title of the about screen. */
    val title: String
}