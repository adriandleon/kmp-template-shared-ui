package com.example.project.home

import com.arkivanov.decompose.ComponentContext

/**
 * Default implementation of [HomeComponent]
 *
 * @param componentContext Context of the component
 * @see HomeComponent
 */
internal class DefaultHomeComponent(componentContext: ComponentContext) :
    HomeComponent, ComponentContext by componentContext {

    override val title: String = "Home Screen"
}
