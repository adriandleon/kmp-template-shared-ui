package com.example.project.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import apptemplate.composeapp.generated.resources.localized_text
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Home content composable that displays the home screen */
@Composable
fun HomeContent(component: HomeComponent, modifier: Modifier = Modifier) {
    var showContent by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = component.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Button(onClick = { showContent = !showContent }) {
            Text(stringResource(Res.string.localized_text))
        }

        AnimatedVisibility(showContent) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: Multiplatform UI")
            }
        }

        AsyncImage(
            model = "https://www.pacificflying.com/wp-content/uploads/airbus-boeing-1200.webp",
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    MaterialTheme {
        HomeContent(
            component =
                object : HomeComponent {
                    override val title: String = "Home Screen"
                }
        )
    }
}
