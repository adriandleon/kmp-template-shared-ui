package com.example.project.onboarding

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.project.onboarding.data.OnboardingRepositoryImpl
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.presentation.store.OnboardingStoreFactory
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val onboardingModule = module {
    singleOf(::OnboardingRepositoryImpl) { bind<OnboardingRepository>() }
    factoryOf(::DefaultStoreFactory) { bind<StoreFactory>() } // TODO: Move to a common module
    factoryOf(::OnboardingStoreFactory)
}
