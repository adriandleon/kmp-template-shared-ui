package com.example.project

import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.crashkios.crashlytics.setCrashlyticsUnhandledExceptionHook
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.example.project.common.di.createRootComponent
import com.example.project.common.di.initKoin
import com.example.project.root.view.RootContent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.crashlytics.crashlytics
import dev.gitlive.firebase.initialize

/**
 * This is the main view controller for the iOS app. It creates the root component and starts the
 * Compose UI. It uses the DefaultComponentContext for the root component. It uses the
 * LifecycleRegistry for the lifecycle management. It uses the RootContent for the root content.
 */
@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController {
    startCrashKiOS()
    initKoin()

    val lifecycle = LifecycleRegistry()
    val root = createRootComponent(componentContext = DefaultComponentContext(lifecycle))

    lifecycle.resume()

    RootContent(component = root)
}

private fun startCrashKiOS() {
    Firebase.initialize()
    if (BuildKonfig.DEBUG.not()) {
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        setCrashlyticsUnhandledExceptionHook()
    }
}
