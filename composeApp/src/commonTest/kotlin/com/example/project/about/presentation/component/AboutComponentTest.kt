package com.example.project.about.presentation.component

import co.touchlab.kermit.ExperimentalKermitApi
import com.arkivanov.decompose.errorhandler.onDecomposeError
import com.example.project.common.util.createComponentForTest
import com.example.project.common.util.lastLogEntry
import com.example.project.common.util.testLogger
import dev.mokkery.MockMode
import dev.mokkery.mock
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

        context("PreviewAboutComponent") {
            val component = PreviewAboutComponent()

            test("title should be \"About View\"") { component.title shouldBe "About View" }
        }
    })

private fun createComponent(): AboutComponent = createComponentForTest { componentContext ->
    DefaultAboutComponent(
        componentContext = componentContext,
        logger = testLogger,
        analytics = mock(mode = MockMode.autoUnit),
    )
}
