package com.example.project.contact.presentation.component

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
class ContactComponentTest :
    FunSpec({
        beforeTest { onDecomposeError = {} }

        test("title should be \"Contact Screen\"") {
            val component = createComponent()
            component.title shouldBe "Contact Screen"
        }

        test("title should log debug message") {
            val component = createComponent()
            component.title

            lastLogEntry.message shouldBe "Message from contact screen"
        }

        context("PreviewContactComponent") {
            val component = PreviewContactComponent()

            test("title should be \"Contact View\"") { component.title shouldBe "Contact View" }
        }
    })

private fun createComponent(): ContactComponent = createComponentForTest { componentContext ->
    DefaultContactComponent(
        componentContext = componentContext,
        logger = testLogger,
        analytics = mock(mode = MockMode.autoUnit),
    )
}
