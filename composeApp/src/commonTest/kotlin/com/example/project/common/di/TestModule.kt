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
import com.example.project.onboarding.domain.OnboardingRepository
import dev.mokkery.MockMode.autofill
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val testSharedModules = module {
    singleOf(::TestDispatcherProvider) { bind<DispatcherProvider>() }
    factoryOf(::DefaultStoreFactory) { bind<StoreFactory>() }
    singleOf(::mockOnboardingRepository) { bind<OnboardingRepository>() }
    factory<Analytics> { testAnalytics }
    factory<Logger> { testLogger }
    factoryOf(::mockDataStore) { bind<DataStore<Preferences>>() }
}

internal val mockOnboardingRepository: OnboardingRepository =
    mock(autofill) {
        everySuspend { this@mock.allSlides() } returns emptyList()
        everySuspend { this@mock.markOnboardingCompleted() } returns Unit
    }

internal val mockDataStore: DataStore<Preferences> = mock(autofill)

internal val testModule = listOf(testSharedModules)
