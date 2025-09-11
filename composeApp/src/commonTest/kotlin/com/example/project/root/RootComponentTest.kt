package com.example.project.root

import com.example.project.auth.domain.repository.AuthRepository
import com.example.project.common.di.testModule
import com.example.project.common.util.DispatcherProvider
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
import kotlinx.coroutines.Dispatchers
import org.koin.test.KoinTest

class RootComponentTest :
    FunSpec({
        val onboardingRepository = mock<OnboardingRepository>()
        val authRepository = mock<AuthRepository>()

        test("should create root component with tabs container if user has seen onboarding") {
            everySuspend { onboardingRepository.resetOnboardingStatus() } returns Unit
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns true
            everySuspend { authRepository.isUserAuthenticated() } returns true
            val slot = createComponent(onboardingRepository, authRepository).slot

            slot.assertActiveSlotInstance<Child.Tabs>()
        }

        test(
            "should create root component with onboarding screen if user has not seen onboarding"
        ) {
            everySuspend { onboardingRepository.resetOnboardingStatus() } returns Unit
            everySuspend { onboardingRepository.hasSeenOnboarding() } returns false
            everySuspend { authRepository.isUserAuthenticated() } returns true
            val slot = createComponent(onboardingRepository, authRepository).slot

            slot.assertActiveSlotInstance<Child.Onboarding>()
        }

        test("should navigate to tabs container when onNavigateToHome is called") {
            everySuspend { onboardingRepository.resetOnboardingStatus() } returns Unit
            everySuspend { authRepository.isUserAuthenticated() } returns true
            val component = createComponent(onboardingRepository, authRepository)

            component.onNavigateToHome()

            component.slot.assertActiveSlotInstance<Child.Tabs>()
        }
    }),
    KoinTest {
    override val extensions: List<Extension> = listOf(KoinExtension(testModule))
}

private fun createComponent(
    onboardingRepository: OnboardingRepository = mock(),
    authRepository: AuthRepository = mock(),
): RootComponent = createComponentForTest { componentContext ->
    DefaultRootComponent(
        componentContext = componentContext,
        onboardingRepository = onboardingRepository,
        authRepository = authRepository,
        dispatcher =
            object : DispatcherProvider {
                override val main = Dispatchers.Main
                override val default = Dispatchers.Default
            },
    )
}
