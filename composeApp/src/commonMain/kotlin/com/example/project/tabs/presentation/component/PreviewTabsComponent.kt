package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.example.project.home.presentation.component.PreviewHomeComponent

/** Preview component for tabs */
class PreviewTabsComponent :
    TabsComponent,
    ComponentContext by DefaultComponentContext(
        lifecycle = LifecycleRegistry().apply { resume() }
    ) {

    override val stack: Value<ChildStack<*, TabsComponent.Child>> =
        MutableValue(
            ChildStack(
                configuration = Unit,
                instance = TabsComponent.Child.Home(component = PreviewHomeComponent()),
            )
        )

    override fun selectHomeTab() {
        // No-op for preview
    }

    override fun selectAboutTab() {
        // No-op for preview
    }

    override fun selectContactTab() {
        // No-op for preview
    }
}
