package com.example.project.common.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.project.analytics.domain.Analytics
import com.example.project.common.util.DispatcherProvider
import com.example.project.common.util.TestDispatcherProvider
import com.example.project.common.util.testAnalytics
import com.example.project.common.util.testLogger
import com.example.project.logger.domain.Logger
import com.example.project.onboarding.data.OnboardingRepositoryImpl
import com.example.project.onboarding.domain.OnboardingRepository
import dev.mokkery.MockMode.autofill
import dev.mokkery.mock
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val testPlatformModule = module {}

internal val testSharedModules = module {
    singleOf(::TestDispatcherProvider) { bind<DispatcherProvider>() }
    factoryOf(::DefaultStoreFactory) { bind<StoreFactory>() }
    singleOf(::OnboardingRepositoryImpl) { bind<OnboardingRepository>() }
    factory<Analytics> { testAnalytics }
    factory<Logger> { testLogger }
    factoryOf(::mockDataStore) { bind<DataStore<Preferences>>() }
}

internal val mockDataStore: DataStore<Preferences> = mock(autofill)

internal val testModule = listOf(testPlatformModule, testSharedModules)
