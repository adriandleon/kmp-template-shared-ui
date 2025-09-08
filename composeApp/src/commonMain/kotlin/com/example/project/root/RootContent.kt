package com.example.project.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.project.home.HomeContent
import com.example.project.onboarding.presentation.view.OnboardingView

/** Root content composable that displays the root component with navigation */
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Children(
                stack = component.stack,
                animation = stackAnimation(slide() + fade() + scale()),
            ) { child ->
                when (val childComponent = child.instance) {
                    is RootComponent.Child.Onboarding -> {
                        OnboardingView(
                            component = childComponent.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    is RootComponent.Child.Home -> {
                        HomeContent(
                            component = childComponent.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}
