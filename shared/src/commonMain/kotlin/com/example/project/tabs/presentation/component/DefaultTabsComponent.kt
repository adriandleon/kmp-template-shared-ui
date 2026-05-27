package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.project.about.presentation.component.AboutComponent
import com.example.project.about.presentation.component.DefaultAboutComponent
import com.example.project.common.util.Url
import com.example.project.common.util.consumePathSegment
import com.example.project.common.util.pathSegmentOf
import com.example.project.contact.presentation.component.ContactComponent
import com.example.project.contact.presentation.component.DefaultContactComponent
import com.example.project.home.presentation.component.DefaultHomeComponent
import com.example.project.home.presentation.component.HomeComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultTabsComponent(componentContext: ComponentContext, deepLinkUrl: Url? = null) :
    TabsComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Configuration>()

    private val listOfTabs = listOf(Configuration.Home, Configuration.About, Configuration.Contact)

    override val stack: Value<ChildStack<Configuration, TabsComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = getInitialConfig(deepLinkUrl),
            childFactory = ::createChild,
        )

    override fun selectHomeTab() {
        navigation.bringToFront(Configuration.Home)
    }

    override fun selectAboutTab() {
        navigation.bringToFront(Configuration.About)
    }

    override fun selectContactTab() {
        navigation.bringToFront(Configuration.Contact)
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext,
    ): TabsComponent.Child =
        when (configuration) {
            is Configuration.Home -> {
                TabsComponent.Child.Home(homeComponent(context))
            }
            is Configuration.About -> {
                TabsComponent.Child.About(aboutComponent(context))
            }
            is Configuration.Contact -> {
                TabsComponent.Child.Contact(contactComponent(context))
            }
        }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(componentContext = componentContext)

    private fun aboutComponent(componentContext: ComponentContext): AboutComponent =
        DefaultAboutComponent(
            componentContext = componentContext,
            logger = get(),
            analytics = get(),
        )

    private fun contactComponent(componentContext: ComponentContext): ContactComponent =
        DefaultContactComponent(
            componentContext = componentContext,
            logger = get(),
            analytics = get(),
        )

    private fun getInitialConfig(deepLinkUrl: Url?): Configuration {
        // TODO: path childUrl
        val (path, childUrl) = deepLinkUrl?.consumePathSegment() ?: return Configuration.Home

        return when (path) {
            pathSegmentOf<Configuration.Home>() -> Configuration.Home
            pathSegmentOf<Configuration.About>() -> Configuration.About
            pathSegmentOf<Configuration.Contact>() ->
                Configuration.Contact // (deepLinkUrl = childUrl)
            else -> Configuration.Home
        }
    }

    private fun getInitialPageIndex(deepLinkUrl: Url?): Int {
        val (path) = deepLinkUrl?.consumePathSegment() ?: return 0

        return listOfTabs.indexOfFirst { it.toString() == path }.takeIf { it >= 0 } ?: 0
    }

    @Serializable
    sealed interface Configuration {
        @Serializable data object Home : Configuration

        @Serializable data object About : Configuration

        @Serializable data object Contact : Configuration
    }
}
