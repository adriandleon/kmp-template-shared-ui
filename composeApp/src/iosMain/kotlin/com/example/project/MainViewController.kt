package com.example.project

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.project.root.DefaultRootComponent
import com.example.project.root.RootContent

@Suppress("FunctionName")
fun MainViewController() = ComposeUIViewController {
    // Create the root component before starting Compose
    val lifecycle = LifecycleRegistry()
    val root =
        DefaultRootComponent(componentContext = DefaultComponentContext(lifecycle = lifecycle))

    RootContent(component = root)
}
