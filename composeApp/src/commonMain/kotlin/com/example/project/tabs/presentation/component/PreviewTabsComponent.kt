package com.example.project.tabs.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume

/** Preview component for tabs */
class PreviewTabsComponent :
    TabsComponent,
    ComponentContext by DefaultComponentContext(
        lifecycle = LifecycleRegistry().apply { resume() }
    ) {

    override val pages =
        MutableValue(ChildPages<Any, TabsComponent.Child>(items = emptyList(), selectedIndex = 0))

    override fun selectTab(index: Int) {
        // No-op for preview
    }

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
