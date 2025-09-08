package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.example.project.common.util.DispatcherProvider
import com.example.project.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class OnboardingBootstrapper(
    private val repository: OnboardingRepository,
    private val dispatcher: DispatcherProvider,
) : CoroutineBootstrapper<OnboardingStore.Action>(mainContext = dispatcher.main) {

    override fun invoke() {
        scope.launch {
            val slides = withContext(dispatcher.default) { repository.allSlides() }
            dispatch(OnboardingStore.Action.LoadSlides(slides = slides))
        }
    }
}
