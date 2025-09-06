package com.example.project.common.di

import co.touchlab.kermit.koin.KermitKoinLogger
import com.example.project.analytics.analyticsModule
import com.example.project.common.commonModule
import com.example.project.features.featureFlagModule
import com.example.project.logger.loggerModule
import com.example.project.onboarding.onboardingModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

val allModules =
    listOf(commonModule, loggerModule, analyticsModule, featureFlagModule, onboardingModule)

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config)
        modules(allModules)
        logger(KermitKoinLogger(koin.get()))
    }
}
