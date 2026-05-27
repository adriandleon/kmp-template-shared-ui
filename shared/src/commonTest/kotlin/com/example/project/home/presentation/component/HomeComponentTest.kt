package com.example.project.home.presentation.component

import co.touchlab.kermit.ExperimentalKermitApi
import com.example.project.common.di.testModule
import com.example.project.common.util.createComponentForTest
import com.example.project.common.util.lastLogEntry
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import org.koin.test.KoinTest

@OptIn(ExperimentalKermitApi::class)
class HomeComponentTest :
    FunSpec({
        test("title should be \"Home Screen\"") {
            val component = createComponent()
            component.title shouldBe "Home Screen"
        }

        test("title should log debug message") {
            val component = createComponent()
            component.title

            lastLogEntry.message shouldBe "Message from home screen"
        }

        context("PreviewHomeComponent") {
            val component = PreviewHomeComponent()

            test("title should be \"Home View\"") { component.title shouldBe "Home View" }
        }
    }),
    KoinTest {
    override val extensions: List<Extension> = listOf(KoinExtension(testModule))
}

private fun createComponent(): HomeComponent = createComponentForTest { componentContext ->
    DefaultHomeComponent(componentContext = componentContext)
}
