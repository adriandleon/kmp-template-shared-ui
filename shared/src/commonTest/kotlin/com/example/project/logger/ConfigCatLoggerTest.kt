package com.example.project.logger

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import com.example.project.common.util.kermitLogger
import com.example.project.common.util.lastLogEntry
import com.example.project.common.util.testLogWriter
import com.example.project.logger.data.ConfigCatLoggerImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

@OptIn(ExperimentalKermitApi::class)
internal class ConfigCatLoggerTest :
    FunSpec({
        val logger = ConfigCatLoggerImpl(kermitLogger)

        context("log error message should be equal to the one passed") {
            checkAll<String> { message ->
                logger.error(message)
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log error severity should be Error") {
            logger.error("irrelevant")
            lastLogEntry.severity shouldBe Severity.Error
        }

        context("log error message with Throwable should be equal to the one passed") {
            checkAll<String> { message ->
                val customException = Throwable("some custom exception")
                logger.error(message, customException)
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log error with Throwable severity should be Error") {
            val customException = Throwable("some custom exception")
            logger.error("irrelevant", customException)
            lastLogEntry.severity shouldBe Severity.Error
        }

        test("log error with Throwable should be equal to the one passed") {
            val customException = Throwable("some custom exception")
            logger.error("irrelevant", customException)

            lastLogEntry.throwable shouldBe customException
        }

        context("log debug message should be equal to the one passed") {
            checkAll<String> { message ->
                logger.debug(message)
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log debug severity should be Debug") {
            logger.debug("irrelevant")
            lastLogEntry.severity shouldBe Severity.Debug
        }

        context("log info message should be equal to the one passed l") {
            checkAll<String> { message ->
                logger.info(message)
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log info severity should be Info") {
            logger.info("irrelevant")
            lastLogEntry.severity shouldBe Severity.Info
        }

        context("log warning message should be equal to the one passed kj") {
            checkAll<String> { message ->
                logger.warning(message)
                lastLogEntry.message shouldBeEqual message
            }
        }

        test("log warning severity should be Warn kh") {
            logger.warning("irrelevant")
            lastLogEntry.severity shouldBe Severity.Warn
        }

        afterTest { testLogWriter.reset() }
    })
