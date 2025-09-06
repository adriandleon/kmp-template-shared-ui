package com.example.project.common

import com.example.project.common.util.dataStore
import com.example.project.common.util.provideDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val commonModule = module {
    factoryOf(::provideDispatcher)
    factoryOf(::dataStore)
}
