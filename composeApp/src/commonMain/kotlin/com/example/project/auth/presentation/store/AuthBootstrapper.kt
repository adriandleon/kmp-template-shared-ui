package com.example.project.auth.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.example.project.auth.domain.AuthRepository
import com.example.project.auth.presentation.store.AuthStore.Action
import com.example.project.common.util.DispatcherProvider
import kotlinx.coroutines.launch

internal class AuthBootstrapper(
    private val authRepository: AuthRepository,
    dispatcher: DispatcherProvider,
) : CoroutineBootstrapper<Action>(mainContext = dispatcher.main) {

    override fun invoke() {
        // Load initial authentication state
        scope.launch {
            authRepository.currentUser.collect { user -> dispatch(Action.UserChanged(user)) }
        }
    }
}
