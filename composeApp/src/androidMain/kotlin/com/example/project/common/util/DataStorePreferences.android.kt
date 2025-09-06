package com.example.project.common.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AndroidDataStore : KoinComponent {
    val context: Context by inject()

    fun getDataStore(): DataStore<Preferences> {
        return createDataStore(
            producePath = { context.filesDir.resolve(DataStoreFileName).absolutePath }
        )
    }
}

internal actual fun dataStore(): DataStore<Preferences> = AndroidDataStore().getDataStore()
