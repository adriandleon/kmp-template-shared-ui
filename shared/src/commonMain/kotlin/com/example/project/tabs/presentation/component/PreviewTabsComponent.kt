package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.project.common.util.PreviewComponentContext
import com.example.project.home.presentation.component.PreviewHomeComponent

/** Preview component for tabs */
class PreviewTabsComponent : TabsComponent, ComponentContext by PreviewComponentContext {

    var homeTabWasCalled: Boolean = false
    var aboutTabWasCalled: Boolean = false
    var contactTabWasCalled: Boolean = false

    override val stack: Value<ChildStack<*, TabsComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance = TabsComponent.Child.Home(component = PreviewHomeComponent()),
            )
        )

    override fun selectHomeTab() {
        homeTabWasCalled = true
    }

    override fun selectAboutTab() {
        aboutTabWasCalled = true
    }

    override fun selectContactTab() {
        contactTabWasCalled = true
    }
}
