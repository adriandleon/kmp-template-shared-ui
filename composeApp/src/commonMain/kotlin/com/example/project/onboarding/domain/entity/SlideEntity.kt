package com.example.project.onboarding.domain.entity

/**
 * Represents a single onboarding slide with a title, description, icon, and optional background
 * image.
 *
 * @property title The headline or main text for the slide.
 * @property description The detailed explanation or supporting text for the slide.
 * @property icon The resource name or identifier for the slide's icon.
 * @property backgroundImage The optional background image resource name for the slide.
 */
data class SlideEntity(
    /** The headline or main text for the slide. */
    val title: String,
    /** The detailed explanation or supporting text for the slide. */
    val description: String,
    /** The resource name or identifier for the slide's icon. */
    val icon: String,
    /** The optional background image resource name for the slide. */
    val backgroundImage: String? = null,
)
