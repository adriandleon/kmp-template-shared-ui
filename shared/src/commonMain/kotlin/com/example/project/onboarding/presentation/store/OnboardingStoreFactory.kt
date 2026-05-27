package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import com.example.project.onboarding.presentation.store.OnboardingStore.Label
import com.example.project.onboarding.presentation.store.OnboardingStore.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class OnboardingStoreFactory(
    private val initialPage: Int = 0,
    private val storeFactory: StoreFactory,
) : KoinComponent {

    fun create(): OnboardingStore {
        return OnboardingStoreImpl()
    }

    private inner class OnboardingStoreImpl :
        OnboardingStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "OnboardingStore",
            initialState = State(currentSlide = initialPage),
            bootstrapper = OnboardingBootstrapper(get(), get()),
            executorFactory = { OnboardingExecutor(get(), get(), get()) },
            reducer = OnboardingReducer,
        )
}
