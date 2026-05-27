package com.example.project.onboarding.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.project.common.di.mockOnboardingRepository
import com.example.project.common.di.testModule
import com.example.project.onboarding.domain.entity.SlideEntity
import com.example.project.onboarding.presentation.store.OnboardingStore.Intent
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.koin.test.KoinTest

class OnboardingStoreTest :
    FunSpec({
        beforeSpec { isAssertOnMainThreadEnabled = false }
        afterSpec { isAssertOnMainThreadEnabled = true }

        context("Initial state") {
            beforeEach { everySuspend { mockOnboardingRepository.allSlides() } returns emptyList() }

            test("should have default values") {
                val store = createStore()

                store.state.currentSlide shouldBeExactly 0
                store.state.totalSlides shouldBeExactly 0
                store.state.isLoading.shouldBeFalse()
                store.state.slides.shouldBeEmpty()
            }
        }

        context("Store creation and bootstrapper") {
            val mockSlides =
                listOf(
                    SlideEntity("Slide 1", "Description 1", "icon_1"),
                    SlideEntity("Slide 2", "Description 2", "icon_2"),
                )

            beforeEach { everySuspend { mockOnboardingRepository.allSlides() } returns mockSlides }

            test("should create store successfully") {
                val store = createStore()

                // Verify store is not null and has expected properties
                store.state.currentSlide shouldBeExactly 0
                store.state.totalSlides shouldBeExactly 2 // Bootstrapper loaded 2 slides
            }

            test("should load slides via bootstrapper") {
                val store = createStore()

                // Verify that slides were loaded by the bootstrapper
                store.state.slides shouldHaveSize 2
                store.state.slides shouldBe mockSlides
                store.state.totalSlides shouldBeExactly 2
                store.state.isLoading.shouldBeFalse()
            }

            test("should handle empty slides from repository") {
                everySuspend { mockOnboardingRepository.allSlides() } returns emptyList()

                val store = createStore()

                // Verify that empty slides were loaded
                store.state.slides.shouldBeEmpty()
                store.state.totalSlides shouldBeExactly 0
                store.state.isLoading.shouldBeFalse()
            }
        }

        context("State properties") {
            test("should have correct state structure") {
                val store = createStore()
                val state = store.state

                state.currentSlide shouldBeExactly 0
                state.totalSlides shouldBeExactly 0
                state.isLoading.shouldBeFalse()
                state.slides.shouldBeEmpty()
            }
        }

        context("Intent handling") {
            beforeEach {
                everySuspend { mockOnboardingRepository.allSlides() } returns
                    listOf(
                        SlideEntity("Slide 1", "Description 1", "icon_1"),
                        SlideEntity("Slide 2", "Description 2", "icon_2"),
                    )
            }

            test("should accept NextSlide intent") {
                val store = createStore()

                // This test verifies that the store can accept the intent without throwing
                store.accept(Intent.NextSlide)

                // The store should still be in a valid state
                store.state.currentSlide shouldBeExactly 1
            }

            test("should accept PreviousSlide intent") {
                val store = createStore()

                // This test verifies that the store can accept the intent without throwing
                store.accept(Intent.PreviousSlide)

                // The store should still be in a valid state
                store.state.currentSlide shouldBeExactly 0
            }

            test("should accept SkipOnboarding intent") {
                val store = createStore()

                // This test verifies that the store can accept the intent without throwing
                store.accept(Intent.SkipOnboarding)

                // The store should still be in a valid state
                store.state.currentSlide shouldBeExactly 0
            }

            test("should accept CompleteOnboarding intent") {
                val store = createStore()

                // This test verifies that the store can accept the intent without throwing
                store.accept(Intent.CompleteOnboarding)

                // The store should still be in a valid state
                store.state.currentSlide shouldBeExactly 0
            }
        }

        context("Bootstrapper behavior") {
            test("should load slides from repository on store creation") {
                val expectedSlides =
                    listOf(
                        SlideEntity("Welcome", "Welcome to the app", "welcome_icon"),
                        SlideEntity("Features", "Discover features", "features_icon"),
                        SlideEntity("Get Started", "Ready to begin", "start_icon"),
                    )

                everySuspend { mockOnboardingRepository.allSlides() } returns expectedSlides

                val store = createStore()

                // Verify that the bootstrapper loaded the slides
                store.state.slides shouldBe expectedSlides
                store.state.totalSlides shouldBeExactly 3
                store.state.isLoading.shouldBeFalse()
            }

            test("should handle repository errors gracefully") {
                everySuspend { mockOnboardingRepository.allSlides() } throws
                    RuntimeException("Repository error")

                val store = createStore()

                // Store should still be in a valid state even if repository fails
                store.state.currentSlide shouldBeExactly 0
                store.state.totalSlides shouldBeExactly 0
                store.state.slides.shouldBeEmpty()
            }
        }

        context("Test data creation") {
            test("should create test slides correctly") {
                val slides = createTestSlides(3)

                slides.size shouldBeExactly 3
                slides[0].title shouldBe "Slide 1"
                slides[0].description shouldBe "Description for slide 1"
                slides[0].icon shouldBe "icon_1"
                slides[0].backgroundImage shouldBe null

                slides[1].title shouldBe "Slide 2"
                slides[1].backgroundImage shouldBe "bg_2"

                slides[2].title shouldBe "Slide 3"
                slides[2].backgroundImage shouldBe null
            }

            test("should create empty slides list") {
                val slides = createTestSlides(0)

                slides.shouldBeEmpty()
            }

            test("should create single slide") {
                val slides = createTestSlides(1)

                slides.size shouldBeExactly 1
                slides[0].title shouldBe "Slide 1"
            }
        }
    }),
    KoinTest {
    override val extensions: List<Extension> = listOf(KoinExtension(testModule))
}

private fun createStore(): Store<Intent, OnboardingStore.State, OnboardingStore.Label> {
    return OnboardingStoreFactory(storeFactory = DefaultStoreFactory()).create()
}

private fun createTestSlides(count: Int): List<SlideEntity> {
    return (1..count).map { index ->
        SlideEntity(
            title = "Slide $index",
            description = "Description for slide $index",
            icon = "icon_$index",
            backgroundImage = if (index % 2 == 0) "bg_$index" else null,
        )
    }
}
