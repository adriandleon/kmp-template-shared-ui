package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import com.example.project.onboarding.presentation.store.OnboardingStore.Label
import com.example.project.onboarding.presentation.store.OnboardingStore.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class OnboardingStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    fun create(onNavigateToHome: (() -> Unit)): OnboardingStore {
        return OnboardingStoreImpl(onNavigateToHome)
    }

    private inner class OnboardingStoreImpl(
        private val onNavigateToHome: (() -> Unit)?
    ) :
        OnboardingStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "OnboardingStore",
            initialState = State(),
            bootstrapper = OnboardingBootstrapper(get(), get()),
            executorFactory = { OnboardingExecutor(get(), get(), get(), onNavigateToHome) },
            reducer = OnboardingReducer,
        )
}
