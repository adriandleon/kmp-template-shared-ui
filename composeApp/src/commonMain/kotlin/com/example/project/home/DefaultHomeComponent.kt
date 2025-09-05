package com.example.project.home

import com.arkivanov.decompose.ComponentContext
import com.example.project.logger.domain.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Default implementation of [HomeComponent]
 *
 * @param componentContext Context of the component
 * @see HomeComponent
 */
internal class DefaultHomeComponent(componentContext: ComponentContext) :
    HomeComponent, ComponentContext by componentContext, KoinComponent {

    private val logger: Logger by inject()

    override val title: String
        get() {
            logger.info { "Fetching title for Home Screen" }
            return "Home Screen"
        }
}
