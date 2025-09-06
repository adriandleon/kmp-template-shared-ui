package com.example.project.common.di

import com.example.project.analytics.analyticsModule
import com.example.project.features.featureFlagModule
import com.example.project.logger.loggerModule
import com.example.project.onboarding.onboardingModule
import org.koin.core.module.Module

/** Platform specific dependencies */
internal expect val platformModule: Module

/** Shared dependencies */
internal val sharedModules =
    listOf(
        analyticsModule,
        featureFlagModule,
        loggerModule,
        onboardingModule
    )
