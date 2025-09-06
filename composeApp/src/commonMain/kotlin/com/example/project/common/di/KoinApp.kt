package com.example.project.common.di

import co.touchlab.crashkios.crashlytics.CrashlyticsKotlin
import co.touchlab.kermit.koin.KermitKoinLogger
import com.example.project.BuildKonfig
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config)
        modules(platformModule + sharedModules)
        logger(KermitKoinLogger(koin.get()))
        CrashlyticsKotlin.setCustomValue("flavor", if (BuildKonfig.DEBUG) "debug" else "release")
    }
}
