package com.example.project.root

import com.example.project.common.util.assertActiveInstance
import com.example.project.common.util.createComponentForTest
import com.example.project.onboarding.domain.OnboardingRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.FunSpec

class RootComponentTest :
    FunSpec({
        val onboardingRepository = mock<OnboardingRepository>()

        test("should create root component with home screen if user has seen onboarding") {
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns true
            val stack = createComponent(onboardingRepository).stack

            stack.assertActiveInstance<RootComponent.Child.Home>()
        }

        test(
            "should create root component with onboarding screen if user has not seen onboarding"
        ) {
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns false
            val stack = createComponent(onboardingRepository).stack

            stack.assertActiveInstance<RootComponent.Child.Onboarding>()
        }
    })

private fun createComponent(onboardingRepository: OnboardingRepository = mock()): RootComponent =
    createComponentForTest { componentContext ->
        DefaultRootComponent(
            componentContext = componentContext,
            onboardingRepository = onboardingRepository,
        )
    }
