package com.example.project.common.di

import com.example.project.common.util.createDataStore
import com.example.project.common.util.provideDispatcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factoryOf(::provideDispatcher)
    single { createDataStore() }
}
