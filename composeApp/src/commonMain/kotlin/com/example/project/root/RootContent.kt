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
import com.example.project.analytics.analyticsModule
import com.example.project.common.commonModule
import com.example.project.features.featureFlagModule
import com.example.project.home.HomeContent
import com.example.project.logger.loggerModule
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.koinConfiguration

/** Root content composable that displays the root component with navigation */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    KoinMultiplatformApplication(
        config =
            koinConfiguration {
                modules(commonModule, loggerModule, analyticsModule, featureFlagModule)
            },
        logLevel = Level.DEBUG,
    ) {
        MaterialTheme {
            Surface(
                modifier = modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Children(
                    stack = component.stack,
                    animation = stackAnimation(slide() + fade() + scale()),
                ) { child ->
                    when (val childComponent = child.instance) {
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
}
