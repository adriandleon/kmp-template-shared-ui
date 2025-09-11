package com.example.project.auth.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.example.project.auth.domain.entity.AuthResult
import com.example.project.auth.presentation.store.AuthStore.Message
import com.example.project.auth.presentation.store.AuthStore.Message.SetLoading

/** Reducer implementation for handling messages and updating state. */
internal object AuthReducer : Reducer<AuthStore.State, Message> {

    override fun AuthStore.State.reduce(msg: Message): AuthStore.State {
        return when (msg) {
            is SetLoading -> copy(isLoading = msg.isLoading, error = null)
            is Message.UserChanged ->
                copy(
                    userEntity = msg.userEntity,
                    isAuthenticated = msg.userEntity != null,
                    isLoading = false,
                    error = null,
                )

            is Message.AuthResultReceived -> handleAuthResult(msg.result)

            is Message.ClearError -> copy(error = null)
        }
    }

    /** Handle authentication results and update state accordingly. */
    private fun AuthStore.State.handleAuthResult(result: AuthResult): AuthStore.State =
        when (result) {
            is AuthResult.Success -> {
                copy(isLoading = false, userEntity = result.userEntity, isAuthenticated = true, error = null)
            }
            is AuthResult.Error -> {
                copy(isLoading = false, error = result.error)
            }
            is AuthResult.Loading -> {
                copy(isLoading = true)
            }
        }
}
