package com.example.project.analytics.domain

import com.example.project.common.util.testAnalytics
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.maps.shouldContain
import io.kotest.property.checkAll

class AnalyticsEventTest :
    FunSpec({
        context("ScreenView event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.ScreenView(":irrelevant:")
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.SCREEN_VIEW
            }

            test("should have expected property screen name key and value") {
                checkAll<String> { screenName ->
                    val event = CommonAnalyticsEvent.ScreenView(screenName)
                    val expectedScreenName = AnalyticsParam.SCREEN_NAME to screenName

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedScreenName
                }
            }

            test("should have expected property screen class key and value") {
                checkAll<String> { screenClass ->
                    val event =
                        CommonAnalyticsEvent.ScreenView(
                            screenName = ":irrelevant:",
                            screenClass = screenClass,
                        )
                    val expectedScreenClass = AnalyticsParam.SCREEN_CLASS to screenClass

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedScreenClass
                }
            }

            test("should have empty screen class value when not provided") {
                val event = CommonAnalyticsEvent.ScreenView(":irrelevant:")
                testAnalytics.track(event)

                val expectedScreenClass = AnalyticsParam.SCREEN_CLASS to ""
                testAnalytics.lastTrackEvent.parameters shouldContain expectedScreenClass
            }
        }

        context("ButtonClick event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.ButtonClick(":irrelevant:")
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.BUTTON_CLICK
            }

            test("should have expected property button name key and value") {
                checkAll<String> { buttonName ->
                    val event = CommonAnalyticsEvent.ButtonClick(buttonName = buttonName)
                    val expectedButtonName = AnalyticsParam.BUTTON_NAME to buttonName

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedButtonName
                }
            }
        }

        context("ElementTap event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.ElementTap(":irrelevant:")
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.ELEMENT_TAP
            }

            test("should have expected property element name key and value") {
                checkAll<String> { elementName ->
                    val event = CommonAnalyticsEvent.ElementTap(elementName = elementName)
                    val expectedElementName = AnalyticsParam.ELEMENT_NAME to elementName

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedElementName
                }
            }
        }

        context("SelectItem event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.SelectItem(":irrelevant:", ":irrelevant:")
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.SELECT_ITEM
            }

            test("should have expected property item id key and value") {
                checkAll<String> { itemId ->
                    val event = CommonAnalyticsEvent.SelectItem(itemId = itemId, ":irrelevant:")
                    val expectedItemId = AnalyticsParam.ITEM_LIST_ID to itemId

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedItemId
                }
            }

            test("should have expected property item name key and value") {
                checkAll<String> { itemName ->
                    val event = CommonAnalyticsEvent.SelectItem(":irrelevant:", itemName = itemName)
                    val expectedItemName = AnalyticsParam.ITEM_LIST_NAME to itemName

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedItemName
                }
            }
        }

        context("SelectVideo event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.SelectVideo(videoId = 0L)
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.SELECT_CONTENT
            }

            test("should have expected content type value of 'video'") {
                val event = CommonAnalyticsEvent.SelectVideo(videoId = 15L)
                val expectedContentType = AnalyticsParam.CONTENT_TYPE to "video"

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedContentType
            }

            test("should have expected property item id key and value") {
                checkAll<Long> { videoId ->
                    val event = CommonAnalyticsEvent.SelectVideo(videoId = videoId)
                    val expectedVideoId = AnalyticsParam.ITEM_ID to videoId.toString()

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedVideoId
                }
            }
        }

        context("SelectCategory event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.SelectCategory(1)
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.SELECT_CONTENT
            }

            test("should have expected content type value of 'category'") {
                val event = CommonAnalyticsEvent.SelectCategory(categoryId = 2)
                val expectedContentType = AnalyticsParam.CONTENT_TYPE to "category"

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedContentType
            }

            test("should have expected property item id key and value") {
                checkAll<Int> { categoryId ->
                    val event = CommonAnalyticsEvent.SelectCategory(categoryId = categoryId)
                    val expectedCategoryId = AnalyticsParam.ITEM_ID to categoryId.toString()

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedCategoryId
                }
            }
        }

        context("SelectBanner event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.SelectBanner(bannerId = 0L)
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.SELECT_CONTENT
            }

            test("should have expected content type value of 'banner'") {
                val event = CommonAnalyticsEvent.SelectBanner(bannerId = 9L)
                val expectedContentType = AnalyticsParam.CONTENT_TYPE to "banner"

                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.parameters shouldContain expectedContentType
            }

            test("should have expected property item id key and value") {
                checkAll<Long> { videoId ->
                    val event = CommonAnalyticsEvent.SelectBanner(bannerId = videoId)
                    val expectedBannerId = AnalyticsParam.ITEM_ID to videoId.toString()

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedBannerId
                }
            }
        }

        context("Error event") {
            test("should have expected event name") {
                val event = CommonAnalyticsEvent.Error(message = ":irrelevant:")
                testAnalytics.track(event)
                testAnalytics.lastTrackEvent.name shouldBeEqual AnalyticsEvents.ERROR
            }

            test("should have expected property error message key and value") {
                checkAll<String> { message ->
                    val event = CommonAnalyticsEvent.Error(message = message)
                    val expectedErrorMessage = AnalyticsParam.ERROR_MESSAGE to message

                    testAnalytics.track(event)
                    testAnalytics.lastTrackEvent.parameters shouldContain expectedErrorMessage
                }
            }
        }

        afterEach { testAnalytics.reset() }
    })
