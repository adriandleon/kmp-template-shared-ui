package com.example.project.tabs.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import apptemplate.composeapp.generated.resources.Res
import apptemplate.composeapp.generated.resources.about_label
import apptemplate.composeapp.generated.resources.contact_label
import apptemplate.composeapp.generated.resources.home_label
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.tabs.presentation.component.TabsComponent
import org.jetbrains.compose.resources.stringResource

/** Bottom navigation bar for tabs */
@Composable
fun TabsBottomBar(component: TabsComponent) {
    val pages by component.pages.subscribeAsState()
    val selectedIndex = pages.selectedIndex

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "") },
            label = { Text(stringResource(Res.string.home_label)) },
            selected = selectedIndex == 0,
            onClick = component::selectHomeTab,
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "") },
            label = { Text(stringResource(Res.string.about_label)) },
            selected = selectedIndex == 1,
            onClick = component::selectAboutTab,
        )

        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "") },
            label = { Text(stringResource(Res.string.contact_label)) },
            selected = selectedIndex == 2,
            onClick = component::selectContactTab,
        )
    }
}
