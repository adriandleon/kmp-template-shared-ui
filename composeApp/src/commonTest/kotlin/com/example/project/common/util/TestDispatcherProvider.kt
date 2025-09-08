package com.example.project.common.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val default: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
