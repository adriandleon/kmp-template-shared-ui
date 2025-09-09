package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.example.project.about.presentation.component.AboutComponent
import com.example.project.contact.presentation.component.ContactComponent
import com.example.project.home.presentation.component.HomeComponent

/**
 * Component for bottom tab navigation container.
 *
 * This component manages three main pages: Home, About, and Contact.
 * It uses PagesNavigation for tab-based navigation.
 *
 * @see DefaultTabsComponent
 */
interface TabsComponent : BackHandlerOwner {

    /** The current pages state with selected tab */
    val pages: Value<ChildPages<*, Child>>

    /** Navigate to a specific tab by index */
    fun selectTab(index: Int)

    /** Navigate to Home tab */
    fun selectHomeTab()

    /** Navigate to About tab */
    fun selectAboutTab()

    /** Navigate to Contact tab */
    fun selectContactTab()

    sealed interface Child {
        /** @param component Child component for the home screen */
        data class Home(val component: HomeComponent) : Child

        /** @param component Child component for the about screen */
        data class About(val component: AboutComponent) : Child

        /** @param component Child component for the contact screen */
        data class Contact(val component: ContactComponent) : Child
    }
}