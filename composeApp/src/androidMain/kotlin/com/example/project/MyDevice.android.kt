package com.example.project

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidMyDevice : MyDevice, KoinComponent {
    private val context: Context by inject()

    override val model: String
        get() = context.applicationInfo.packageName

    override val manufacturer: String
        get() {
            return context.packageName
        }
}

actual fun getMyDevice(): MyDevice = AndroidMyDevice()
