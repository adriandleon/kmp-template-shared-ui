package com.example.project.about.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.analytics.domain.Analytics
import com.example.project.analytics.domain.CommonAnalyticsEvent
import com.example.project.logger.domain.Logger

/**
 * This component is responsible for the "About" screen.
 *
 * Deeplink URL: "example://template/tabs/about"
 */
internal class DefaultAboutComponent(
    componentContext: ComponentContext,
    private val logger: Logger,
    private val analytics: Analytics,
) : AboutComponent, ComponentContext by componentContext {

    override val title: String
        get() {
            analytics.track(
                CommonAnalyticsEvent.ScreenView(
                    screenName = SCREEN_TITLE,
                    screenClass = "DefaultAboutComponent",
                )
            )
            logger.debug { "Message from about screen" }
            return SCREEN_TITLE
        }

    private companion object {
        private const val SCREEN_TITLE = "About Screen"
    }
}
