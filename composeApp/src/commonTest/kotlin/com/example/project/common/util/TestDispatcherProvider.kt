package com.example.project.common.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = StandardTestDispatcher()

    override val default: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
