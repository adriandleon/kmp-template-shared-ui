package com.example.project.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.onboarding.presentation.view.OnboardingView
import com.example.project.root.RootComponent.Child
import com.example.project.tabs.presentation.view.TabsView

/** Root content composable that displays the root component with navigation */
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val slot by component.slot.subscribeAsState()

            slot.child?.instance?.let { child ->
                when (child) {
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
