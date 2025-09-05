package com.example.project.analytics.domain

import com.example.project.common.util.testAnalytics
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.maps.shouldContain
import io.kotest.property.checkAll

class CustomAnalyticsEventTest :
    FunSpec({
        test("should have expected event name") {
            checkAll<String> { eventName ->
                val event = CustomAnalyticsEvent(name = eventName)
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual eventName
            }
        }

        test("should have expected parameters key value String") {
            checkAll<String, String> { paramKey, paramValue ->
                val event =
                    CustomAnalyticsEvent(
                        name = ":irrelevant:",
                        parameters = mapOf(paramKey to paramValue),
                    )
                val expectedParam = paramKey to paramValue

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedParam
            }
        }

        test("should have expected parameters key value Integer") {
            checkAll<String, Int> { paramKey, paramValue ->
                val event =
                    CustomAnalyticsEvent(
                        name = ":irrelevant:",
                        parameters = mapOf(paramKey to paramValue),
                    )
                val expectedParam = paramKey to paramValue

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedParam
            }
        }

        test("should have expected parameters key value Boolean") {
            checkAll<String, Boolean> { paramKey, paramValue ->
                val event =
                    CustomAnalyticsEvent(
                        name = ":irrelevant:",
                        parameters = mapOf(paramKey to paramValue),
                    )
                val expectedParam = paramKey to paramValue

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedParam
            }
        }

        test("should have expected parameters key value Long") {
            checkAll<String, Long> { paramKey, paramValue ->
                val event =
                    CustomAnalyticsEvent(
                        name = ":irrelevant:",
                        parameters = mapOf(paramKey to paramValue),
                    )
                val expectedParam = paramKey to paramValue

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedParam
            }
        }

        test("should have expected parameters key value Double") {
            checkAll<String, Double> { paramKey, paramValue ->
                val event =
                    CustomAnalyticsEvent(
                        name = ":irrelevant:",
                        parameters = mapOf(paramKey to paramValue),
                    )
                val expectedParam = paramKey to paramValue

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedParam
            }
        }

        afterEach { testAnalytics.reset() }
    })
