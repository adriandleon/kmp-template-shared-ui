package com.example.project.analytics.data

import com.example.project.analytics.domain.AnalyticsProvider
import com.example.project.analytics.domain.CommonAnalyticsEvent
import com.example.project.analytics.domain.CustomAnalyticsEvent
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import io.kotest.core.spec.style.FunSpec

class AnalyticsImplTest :
    FunSpec({
        context("with one provider") {
            val mockProvider = mock<AnalyticsProvider>(MockMode.autoUnit)
            val event = CustomAnalyticsEvent("test_event")
            val anotherEvent = CommonAnalyticsEvent.Error("error_message")

            test("should track one event") {
                val analytics = AnalyticsImpl(listOf(mockProvider))

                analytics.track(event)

                verify(mode = VerifyMode.Companion.exactly(1)) { mockProvider.track(event) }
            }

            test("should track two events") {
                val analytics = AnalyticsImpl(listOf(mockProvider))

                analytics.track(events = listOf(event, anotherEvent))

                verify(mode = VerifyMode.Companion.exactly(1)) {
                    mockProvider.track(event)
                    mockProvider.track(anotherEvent)
                }
            }
        }

        context("with two providers") {
            val firstMockProvider = mock<AnalyticsProvider>(MockMode.autoUnit)
            val secondMockProvider = mock<AnalyticsProvider>(MockMode.autoUnit)
            val event = CustomAnalyticsEvent("test_event")
            val anotherEvent = CommonAnalyticsEvent.Error("error_message")

            test("should track one event") {
                val analytics = AnalyticsImpl(listOf(firstMockProvider, secondMockProvider))

                analytics.track(event)

                verify(mode = VerifyMode.Companion.exactly(1)) {
                    firstMockProvider.track(event)
                    secondMockProvider.track(event)
                }
            }

            test("should track two events") {
                val analytics = AnalyticsImpl(listOf(firstMockProvider, secondMockProvider))

                analytics.track(events = listOf(event, anotherEvent))

                verify(mode = VerifyMode.Companion.exactly(1)) {
                    firstMockProvider.track(event)
                    secondMockProvider.track(event)
                    firstMockProvider.track(anotherEvent)
                    secondMockProvider.track(anotherEvent)
                }
            }
        }
    })
