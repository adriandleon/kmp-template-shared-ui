package com.example.project.onboarding

import com.example.project.onboarding.data.OnboardingRepositoryImpl
import com.example.project.onboarding.domain.OnboardingRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val onboardingModule = module {
    singleOf(::OnboardingRepositoryImpl) { bind<OnboardingRepository>() }
}
