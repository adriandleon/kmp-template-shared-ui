package com.example.project.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.home.presentation.view.HomeView
import com.example.project.onboarding.presentation.view.OnboardingView

/** Root content composable that displays the root component with navigation */
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val slot by component.slot.subscribeAsState()

            slot.child?.instance?.let { child ->
                when (child) {
                    is RootComponent.Child.Onboarding -> {
                        OnboardingView(
                            component = child.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is RootComponent.Child.Home -> {
                        HomeView(component = child.component, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
