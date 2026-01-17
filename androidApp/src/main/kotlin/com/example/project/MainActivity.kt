package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.handleDeepLink
import com.arkivanov.decompose.retainedComponent
import com.example.project.common.di.createRootComponent
import com.example.project.common.util.Url
import com.example.project.root.view.RootContent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val root =
            handleDeepLink { uri ->
                val deppLinkUrl = Url(uri.toString())
                retainedComponent(discardSavedState = uri != null) {
                    createRootComponent(it, deppLinkUrl)
                }
            } ?: return
        setContent { RootContent(component = root) }
    }
}
