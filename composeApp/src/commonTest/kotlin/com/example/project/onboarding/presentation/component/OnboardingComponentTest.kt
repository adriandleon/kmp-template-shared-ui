package com.example.project.onboarding.presentation.component

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.example.project.common.di.testModule
import com.example.project.common.util.createComponentForTest
import dev.mokkery.mock
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.koin.test.KoinTest

class OnboardingComponentTest :
    FunSpec({
        beforeSpec { isAssertOnMainThreadEnabled = false }

        afterSpec {
            isAssertOnMainThreadEnabled = true
        }

        test("should create component successfully") {
            val component = createComponent()

            component.shouldBeInstanceOf<OnboardingComponent>()
            component.state.value shouldBe OnboardingComponent.OnboardingState()
        }

        test("should have correct initial state") {
            val component = createComponent()

            val initialState = component.state.value
            initialState.currentSlide shouldBe 0
            initialState.totalSlides shouldBe 0
            initialState.isLoading shouldBe false
            initialState.slides shouldBe emptyList()
        }

        test("should call navigation methods without errors") {
            val component = createComponent()

            // These should not throw exceptions
            component.nextSlide()
            component.previousSlide()
            component.skipOnboarding()
            component.completeOnboarding()
        }

        test("should handle multiple method calls") {
            val component = createComponent()

            // Call multiple methods in sequence
            component.nextSlide()
            component.previousSlide()
            component.skipOnboarding()
            component.completeOnboarding()
            component.nextSlide()
            component.previousSlide()

            // Should not throw exceptions
        }

        test("should handle rapid successive calls") {
            val component = createComponent()

            // Make rapid successive calls
            repeat(10) { component.nextSlide() }

            // Should not throw exceptions
        }

        test("should maintain component state") {
            val component = createComponent()

            // Initial state should be consistent
            val state1 = component.state.value
            val state2 = component.state.value

            state1 shouldBe state2
        }

        test("should handle empty slides gracefully") {
            val component = createComponent()

            val state = component.state.value
            state.slides shouldBe emptyList()
            state.totalSlides shouldBe 0
        }
    }),
    KoinTest {
    override val extensions: List<Extension> = listOf(KoinExtension(testModule))
}

private fun createComponent(onNavigateToHome: () -> Unit = mock()): OnboardingComponent =
    createComponentForTest { componentContext ->
        DefaultOnboardingComponent(
            componentContext = componentContext,
            onNavigateToHome = onNavigateToHome,
        )
    }
