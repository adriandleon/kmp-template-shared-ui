package com.example.project.common.util

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

internal object PreviewComponentContext :
    ComponentContext by DefaultComponentContext(lifecycle = LifecycleRegistry())
