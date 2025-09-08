package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.project.common.util.DispatcherProvider
import com.example.project.logger.domain.Logger
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.presentation.store.OnboardingStore.Action
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import com.example.project.onboarding.presentation.store.OnboardingStore.Label
import com.example.project.onboarding.presentation.store.OnboardingStore.Message
import com.example.project.onboarding.presentation.store.OnboardingStore.State
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class OnboardingExecutor(
    private val repository: OnboardingRepository,
    private val logger: Logger,
    private val dispatcher: DispatcherProvider,
    private val onNavigateToHome: (() -> Unit)? = null,
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext = dispatcher.default) {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.SkipOnboarding -> {
                logger.info { "Skip onboarding clicked" }

                scope.launch {
                    repository.markOnboardingCompleted()
                    withContext(dispatcher.main) { onNavigateToHome?.invoke() }
                }
            }

            Intent.CompleteOnboarding -> {
                logger.info { "Completing onboarding clicked" }

                scope.launch {
                    repository.markOnboardingCompleted()
                    withContext(dispatcher.main) { onNavigateToHome?.invoke() }
                }
            }

            Intent.NextSlide -> dispatch(Message.OnNextClicked)
            Intent.PreviousSlide -> dispatch(Message.OnPreviousClicked)
        }
    }

    override fun executeAction(action: Action) {
        when (action) {
            is Action.LoadSlides -> dispatch(Message.LoadSlides(action.slides))
        }
    }
}
