package com.example.project.tabs.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.project.tabs.presentation.component.TabsComponent

/** Bottom navigation bar for tabs */
@Composable
fun TabsBottomBar(component: TabsComponent) {
    val pages by component.pages.subscribeAsState()
    val selectedIndex = pages.selectedIndex

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = component::selectHomeTab,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About"
                )
            },
            label = { Text("About") },
            selected = selectedIndex == 1,
            onClick = component::selectAboutTab,
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ContactMail,
                    contentDescription = "Contact"
                )
            },
            label = { Text("Contact") },
            selected = selectedIndex == 2,
            onClick = component::selectContactTab,
        )
    }
}
