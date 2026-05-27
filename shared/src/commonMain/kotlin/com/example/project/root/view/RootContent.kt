package com.example.project.root.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.auth.presentation.view.AuthScreen
import com.example.project.common.util.ExternalUriHandler
import com.example.project.common.util.Url
import com.example.project.onboarding.presentation.view.OnboardingView
import com.example.project.root.component.RootComponent
import com.example.project.root.component.RootComponent.Child
import com.example.project.tabs.presentation.view.TabsView

/** Root content composable that displays the root component with navigation */
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri -> component.handleDeepLink(deepLinkUrl = Url(uri)) }

        onDispose { ExternalUriHandler.listener = null }
    }

    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val slot by component.slot.subscribeAsState()

            slot.child?.instance?.let { child ->
                when (child) {
                    is Child.Auth -> {
                        AuthScreen(component = child.component, modifier = Modifier.fillMaxSize())
                    }
                    is Child.Onboarding -> {
                        OnboardingView(
                            component = child.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is Child.Tabs -> {
                        TabsView(component = child.component, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
