package com.example.project.root

import com.example.project.common.di.testModule
import com.example.project.common.util.assertActiveInstance
import com.example.project.common.util.assertActiveSlotInstance
import com.example.project.common.util.createComponentForTest
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.root.RootComponent.Child
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import org.koin.test.KoinTest

class RootComponentTest :
    FunSpec({
        val onboardingRepository = mock<OnboardingRepository>()

        test("should create root component with home screen if user has seen onboarding") {
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns true
            val slot = createComponent(onboardingRepository).slot

            slot.assertActiveSlotInstance<Child.Home>()
        }

        test(
            "should create root component with onboarding screen if user has not seen onboarding"
        ) {
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns false
            val slot = createComponent(onboardingRepository).slot

            slot.assertActiveSlotInstance<Child.Onboarding>()
        }

        test("should navigate to home when onNavigateToHome is called") {
            val component = createComponent(onboardingRepository)

            component.onNavigateToHome()

            component.slot.assertActiveSlotInstance<Child.Home>()
        }
    }),
    KoinTest {
    override val extensions: List<Extension> = listOf(KoinExtension(testModule))
}

private fun createComponent(onboardingRepository: OnboardingRepository = mock()): RootComponent =
    createComponentForTest { componentContext ->
        DefaultRootComponent(
            componentContext = componentContext,
            onboardingRepository = onboardingRepository,
        )
    }
