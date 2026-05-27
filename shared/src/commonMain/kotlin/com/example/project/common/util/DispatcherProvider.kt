package com.example.project.common.util

import kotlinx.coroutines.CoroutineDispatcher

internal interface DispatcherProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}

internal expect fun provideDispatcher(): DispatcherProvider
