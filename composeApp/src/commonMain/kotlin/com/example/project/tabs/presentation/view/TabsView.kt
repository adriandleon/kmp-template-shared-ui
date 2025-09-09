package com.example.project.tabs.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.example.project.about.presentation.view.AboutView
import com.example.project.contact.presentation.view.ContactView
import com.example.project.home.presentation.view.HomeView
import com.example.project.tabs.presentation.component.TabsComponent
import com.example.project.tabs.presentation.component.TabsComponent.Child

/** Tabs content composable that displays the tabs container with bottom navigation */
@Composable
fun TabsView(component: TabsComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                TabsBottomBar(component = component)
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                ChildPages(
                    pages = component.pages,
                    onPageSelected = { index -> component.selectTab(index) },
                    scrollAnimation = PagesScrollAnimation.Default,
                ) { _, child ->
                    when (child) {
                        is Child.Home -> {
                            HomeView(
                                component = child.component,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        is Child.About -> {
                            AboutView(
                                component = child.component,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        is Child.Contact -> {
                            ContactView(
                                component = child.component,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}
