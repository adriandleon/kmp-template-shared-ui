package com.example.project.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.project.onboarding.domain.OnboardingRepository
import com.example.project.onboarding.domain.entity.SlideEntity
import kotlinx.coroutines.flow.first

private val HAS_SEEN_ONBOARDING_KEY = booleanPreferencesKey("has_seen_onboarding")

internal class OnboardingRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    OnboardingRepository {

    override suspend fun hasSeenOnboarding(): Boolean {
        return dataStore.data.first()[HAS_SEEN_ONBOARDING_KEY] ?: false
    }

    override suspend fun markOnboardingCompleted() {
        dataStore.edit { preferences -> preferences[HAS_SEEN_ONBOARDING_KEY] = true }
    }

    override suspend fun resetOnboardingStatus() {
        dataStore.edit { preferences -> preferences[HAS_SEEN_ONBOARDING_KEY] = false }
    }

    override suspend fun allSlides(): List<SlideEntity> {
        return listOf(
            SlideEntity(
                title = "Mission",
                description =
                    "Bringing hope and faith to our community through love, service, and the " +
                        "message of Jesus Christ.",
                icon = "🎯",
            ),
            SlideEntity(
                title = "Vision",
                description =
                    "To be a beacon of light in our city, transforming lives and building a " +
                        "stronger, more connected community.",
                icon = "🌟",
            ),
            SlideEntity(
                title = "How to Engage",
                description =
                    "Join us in worship, connect with ministries, give back to causes, and grow " +
                        "in your faith journey.",
                icon = "🤝",
            ),
        )
    }
}
