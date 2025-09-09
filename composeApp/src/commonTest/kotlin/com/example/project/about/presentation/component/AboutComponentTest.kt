package com.example.project.about.presentation.component

import co.touchlab.kermit.ExperimentalKermitApi
import com.arkivanov.decompose.errorhandler.onDecomposeError
import com.example.project.common.util.createComponentForTest
import com.example.project.common.util.lastLogEntry
import com.example.project.common.util.testLogger
import dev.mokkery.MockMode
import dev.mokkery.mock
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalKermitApi::class)
class AboutComponentTest :
    FunSpec({
        beforeTest { onDecomposeError = {} }

        test("title should be \"About Screen\"") {
            val component = createComponent()
            component.title shouldBe "About Screen"
        }

        test("title should log debug message") {
            val component = createComponent()
            component.title

            lastLogEntry.message shouldBe "Message from about screen"
        }

        test("onBackClicked should be called when clicked") {
            var clicked = false
            val component = createComponent(onBackClicked = { clicked = true })

            component.onBackClicked()

            clicked shouldBe true
        }

        context("PreviewAboutComponent") {
            val component = PreviewAboutComponent()

            test("title should be \"About View\"") { component.title shouldBe "About View" }

            test("preview functions does not throw any exceptions") {
                shouldNotThrowAny { component.onBackClicked() }
            }
        }
    })

private fun createComponent(onBackClicked: () -> Unit = {}): AboutComponent =
    createComponentForTest { componentContext ->
        DefaultAboutComponent(
            componentContext = componentContext,
            logger = testLogger,
            analytics = mock(mode = MockMode.autoUnit),
            onBackClicked = onBackClicked,
        )
    }
