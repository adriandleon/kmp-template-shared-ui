package com.example.project

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.example.project.root.DefaultRootComponent
import com.example.project.root.RootContent

/**
 * This is the main view controller for the iOS app. It creates the root component and starts the
 * Compose UI. It uses the DefaultComponentContext for the root component. It uses the
 * LifecycleRegistry for the lifecycle management. It uses the RootContent for the root content.
 */
@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController {
    val lifecycle = LifecycleRegistry()
    val root = DefaultRootComponent(componentContext = DefaultComponentContext(lifecycle))

    lifecycle.resume()

    RootContent(component = root)
}
