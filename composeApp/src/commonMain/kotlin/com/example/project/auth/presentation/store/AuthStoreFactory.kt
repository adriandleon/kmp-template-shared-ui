package com.example.project.auth.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.project.auth.domain.AuthRepository
import com.example.project.auth.presentation.store.AuthStore.Intent
import com.example.project.auth.presentation.store.AuthStore.Label
import com.example.project.auth.presentation.store.AuthStore.State
import com.example.project.common.util.DispatcherProvider

/**
 * Factory for creating AuthStore instances using MVIKotlin.
 *
 * This factory creates a complete MVIKotlin store with proper separation of concerns:
 * - Actions for internal store communication
 * - Messages for state updates
 * - CoroutineExecutor for async operations
 * - Reducer for state management
 * - Bootstrapper for initial state loading
 */
internal class AuthStoreFactory(
    private val storeFactory: StoreFactory,
    private val authRepository: AuthRepository,
    private val dispatcherProvider: DispatcherProvider,
) {

    fun create(): AuthStore {
        return OnboardingStoreImpl()
    }

    private inner class OnboardingStoreImpl :
        AuthStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "AuthStore",
            initialState = State(),
            bootstrapper = AuthBootstrapper(authRepository, dispatcherProvider),
            executorFactory = { AuthExecutor(authRepository, dispatcherProvider) },
            reducer = AuthReducer,
        )
}
