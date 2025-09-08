package com.example.project.onboarding.presentation.mapper

import com.example.project.onboarding.domain.entity.SlideEntity
import com.example.project.onboarding.presentation.store.OnboardingStore
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class StateToModelTest :
    FunSpec({
        test("default state should be mapped to default model") {
            val state = OnboardingStore.State()
            val model = stateToModel(state)

            model.currentSlide shouldBeExactly 0
            model.totalSlides shouldBeExactly 0
            model.isLoading.shouldBeFalse()
            model.slides.shouldBeEmpty()
        }

        test("should map currentSlide to model") {
            val currentSlide = Arb.positiveInt().next()
            val state = OnboardingStore.State(currentSlide = currentSlide)
            val model = stateToModel(state)
            model.currentSlide shouldBeExactly currentSlide
        }

        test("should map totalSlides to model") {
            val totalSlides = Arb.positiveInt().next()
            val state = OnboardingStore.State(totalSlides = totalSlides)
            val model = stateToModel(state)
            model.totalSlides shouldBeExactly totalSlides
        }

        test("should map isLoading to model") {
            checkAll<Boolean> { isLoading ->
                val state = OnboardingStore.State(isLoading = isLoading)
                val model = stateToModel(state)
                model.isLoading shouldBe isLoading
            }
        }

        context("should map slides to model") {
            // Create a custom generator for SlideEntity
            val slideEntityArb = arbitrary { rs ->
                SlideEntity(
                    title = Arb.string().next(rs),
                    description = Arb.string().next(rs),
                    icon = Arb.string().next(rs),
                    backgroundImage = Arb.string().next(rs).takeIf { it.isNotEmpty() },
                )
            }

            test("should map slides to model with empty list") {
                val emptyState = OnboardingStore.State(slides = emptyList())
                val emptyModel = stateToModel(emptyState)
                emptyModel.slides.shouldBeEmpty()
            }

            test("should map slides to model with a single slide") {
                val singleSlide = slideEntityArb.next()
                val singleSlideState = OnboardingStore.State(slides = listOf(singleSlide))
                val singleSlideModel = stateToModel(singleSlideState)
                singleSlideModel.slides shouldHaveSize 1
                singleSlideModel.slides.first() shouldBe singleSlide
            }

            test("should map slides to model with multiple slides") {
                checkAll(Arb.list(slideEntityArb, 1..10)) { slides ->
                    val state = OnboardingStore.State(slides = slides)
                    val model = stateToModel(state)

                    model.slides shouldHaveSize slides.size
                    model.slides shouldBe slides
                }
            }
        }
    })
