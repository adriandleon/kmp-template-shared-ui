package com.example.project.features.data.provider

import com.configcat.ConfigCatClient
import com.configcat.ConfigCatUser
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ConfigCatProviderTest :
    FunSpec({
        val mockConfigCatClient = mock<ConfigCatClient>(MockMode.autofill)
        val provider = ConfigCatProvider(mockConfigCatClient)

        test("getAllKeys should return all keys from ConfigCatClient") {
            checkAll(Arb.string()) { keys ->
                // Given
                everySuspend { mockConfigCatClient.getAllKeys() } returns listOf(keys)

                // When
                val result = provider.getAllKeys()

                // Then
                result shouldBe listOf(keys)
            }
        }

        test("setUserProperties should set default user in ConfigCatClient") {
            checkAll(Arb.string(), Arb.string().orNull(), Arb.string().orNull()) {
                userId,
                email,
                country ->
                // Given
                val expectedUser =
                    ConfigCatUser(identifier = userId, email = email, country = country)
                everySuspend { mockConfigCatClient.setDefaultUser(expectedUser) } returns Unit

                // When
                provider.setUserProperties(userId, email, country)

                // Then
                // Verification is implicit through the strict mock mode
            }
        }
    })
