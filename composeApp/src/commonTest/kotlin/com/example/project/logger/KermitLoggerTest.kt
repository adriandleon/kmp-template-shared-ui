package com.example.project.logger

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import com.example.project.common.util.kermitLogger
import com.example.project.common.util.lastLogEntry
import com.example.project.logger.data.KermitLoggerImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

@OptIn(ExperimentalKermitApi::class)
class KermitLoggerTest :
    FunSpec({
        val logger = KermitLoggerImpl(kermitLogger)

        context("log error message should be equal to the one passed") {
            checkAll<String> { message ->
                logger.error { message }
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log error severity should be Error") {
            logger.error { "irrelevant" }
            lastLogEntry.severity shouldBe Severity.Error
        }

        test("log error with throwable should be equal to the one passed ") {
            val customException = Throwable("some custom exception")
            logger.error(customException) { "irrelevant" }

            lastLogEntry.throwable shouldBe customException
        }

        context("log debug message should be equal to the one passed") {
            checkAll<String> { message ->
                logger.debug { message }
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log debug severity should be Debug") {
            logger.debug { "irrelevant" }
            lastLogEntry.severity shouldBe Severity.Debug
        }

        test("log debug with throwable should be equal to the one passed ") {
            val customException = Throwable("some custom exception")
            logger.debug(customException) { "irrelevant" }

            lastLogEntry.throwable shouldBe customException
        }

        context("log info message should be equal to the one passed") {
            checkAll<String> { message ->
                logger.info { message }
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log info severity should be Info") {
            logger.info { "irrelevant" }
            lastLogEntry.severity shouldBe Severity.Info
        }

        test("log info without throwable should be null") {
            logger.info { "irrelevant" }
            lastLogEntry.throwable shouldBe null
        }
    })
