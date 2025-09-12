package com.example.project.contact.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.analytics.domain.Analytics
import com.example.project.analytics.domain.CommonAnalyticsEvent
import com.example.project.logger.domain.Logger

/**
 * Represents a component for the Contact screen. Default implementation of [ContactComponent]
 *
 * Deeplink URL: "example://app/tabs/contact"
 */
internal class DefaultContactComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val analytics: Analytics,
) : ContactComponent, ComponentContext by componentContext {

    override val title: String
        get() {
            analytics.track(
                CommonAnalyticsEvent.ScreenView(
                    screenName = SCREEN_TITLE,
                    screenClass = "DefaultContactComponent",
                )
            )
            logger.debug { "Message from contact screen" }
            return SCREEN_TITLE
        }

    private companion object {
        private const val SCREEN_TITLE = "Contact Screen"
    }
}
