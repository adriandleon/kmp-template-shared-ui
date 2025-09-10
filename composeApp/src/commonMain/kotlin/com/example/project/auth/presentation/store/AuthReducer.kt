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
                    user = msg.user,
                    isAuthenticated = msg.user != null,
                    isLoading = false,
                    error = null,
                )

            is Message.AuthResultReceived -> {
                when (msg.result) {
                    is AuthResult.Success -> {
                        copy(
                            isLoading = false,
                            user = msg.result.user,
                            isAuthenticated = true,
                            error = null,
                        )
                    }

                    is AuthResult.Error -> copy(isLoading = false, error = msg.result.error)

                    is AuthResult.Loading -> copy(isLoading = true)
                }
            }

            is Message.ClearError -> copy(error = null)
        }
    }
}
