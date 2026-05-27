package com.example.project.logger.data

import com.configcat.log.Logger

internal class ConfigCatLoggerImpl(private val kermitLogger: co.touchlab.kermit.Logger) : Logger {
    override fun debug(message: String) {
        kermitLogger.d { message }
    }

    override fun error(message: String) {
        kermitLogger.e { message }
    }

    override fun error(message: String, throwable: Throwable) {
        kermitLogger.e(throwable = throwable) { message }
    }

    override fun info(message: String) {
        kermitLogger.i { message }
    }

    override fun warning(message: String) {
        kermitLogger.w { message }
    }
}
