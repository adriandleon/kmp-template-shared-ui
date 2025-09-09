package com.example.project.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.project.home.presentation.view.HomeView
import com.example.project.onboarding.presentation.view.OnboardingView

/** Root content composable that displays the root component with navigation */
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Children(
                stack = component.stack,
                animation = stackAnimation(fade()),
            ) { child ->
                when (val childComponent = child.instance) {
                    is RootComponent.Child.Onboarding -> {
                        OnboardingView(
                            component = childComponent.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is RootComponent.Child.Home -> {
                        HomeView(
                            component = childComponent.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}
