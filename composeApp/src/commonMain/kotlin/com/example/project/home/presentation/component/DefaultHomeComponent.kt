package com.example.project.home.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.logger.domain.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Default implementation of [HomeComponent]
 *
 * Deeplink URL:
 * - "example://template/tabs"
 * - "example://template/tabs/home"
 *
 * @param componentContext Context of the component
 * @see HomeComponent
 */
internal class DefaultHomeComponent(componentContext: ComponentContext) :
    HomeComponent, ComponentContext by componentContext, KoinComponent {

    private val logger: Logger by inject()

    override val title: String
        get() {
            logger.info { "Message from home screen" }
            return "Home Screen"
        }
}
