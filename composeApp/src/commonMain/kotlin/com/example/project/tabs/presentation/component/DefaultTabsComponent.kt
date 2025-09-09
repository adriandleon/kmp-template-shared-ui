package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.example.project.about.presentation.component.AboutComponent
import com.example.project.about.presentation.component.DefaultAboutComponent
import com.example.project.contact.presentation.component.ContactComponent
import com.example.project.contact.presentation.component.DefaultContactComponent
import com.example.project.home.presentation.component.DefaultHomeComponent
import com.example.project.home.presentation.component.HomeComponent
import com.example.project.tabs.presentation.component.TabsComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultTabsComponent(componentContext: ComponentContext) :
    TabsComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = PagesNavigation<Configuration>()

    override val pages: Value<ChildPages<*, TabsComponent.Child>> =
        childPages(
            source = navigation,
            serializer = Configuration.serializer(),
            initialPages = {
                Pages(
                    items = listOf(
                        Configuration.Home,
                        Configuration.About,
                        Configuration.Contact
                    ), selectedIndex = 0
                )
            },
            childFactory = ::createChild,
        )

    override fun selectTab(index: Int) {
        navigation.select(index)
    }

    override fun selectHomeTab() {
        navigation.select(0)
    }

    override fun selectAboutTab() {
        navigation.select(1)
    }

    override fun selectContactTab() {
        navigation.select(2)
    }

    private fun createChild(configuration: Configuration, context: ComponentContext): TabsComponent.Child =
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

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object Home : Configuration

        @Serializable
        data object About : Configuration

        @Serializable
        data object Contact : Configuration
    }
}