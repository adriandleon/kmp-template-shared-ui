package com.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import apptemplate.composeapp.generated.resources.Res
import apptemplate.composeapp.generated.resources.compose_multiplatform
import coil3.compose.AsyncImage
import com.example.project.analytics.analyticsModule
import com.example.project.common.commonModule
import com.example.project.features.featureFlagModule
import com.example.project.logger.loggerModule
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
internal fun App() {
    KoinMultiplatformApplication(
        config =
            koinConfiguration {
                modules(commonModule, loggerModule, analyticsModule, featureFlagModule)
            },
        logLevel = Level.DEBUG,
    ) {
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier =
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                        .safeContentPadding()
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) { Text("Click Me!") }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }

                AsyncImage(
                    model =
                        "https://www.pacificflying.com/wp-content/uploads/airbus-boeing-1200.webp",
                    contentDescription = null,
                )
            }
        }
    }
}
