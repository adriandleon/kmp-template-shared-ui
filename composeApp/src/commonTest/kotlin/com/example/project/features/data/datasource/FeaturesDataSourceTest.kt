package com.example.project.features.data.datasource

import com.example.project.features.domain.FeatureFlag
import com.example.project.features.domain.asFeatures
import com.example.project.features.domain.provider.FeatureFlagProvider
import com.example.project.features.domain.repository.FeaturesRepository
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class FeaturesDataSourceTest :
    FunSpec({
        val mockProvider: FeatureFlagProvider = mock(MockMode.autofill)

        test("get should return the value from the provider") {
            checkAll<Boolean> { value ->
                everySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) } returns
                    value
                val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

                val result = repository.get(TestFlag)

                result shouldBe value
            }
        }

        test("get should call the provider with the correct parameters") {
            val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

            repository.get(TestFlag)

            verifySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) }
        }

        test("get with vararg flags should return map of all flags with their values") {
            checkAll(Arb.boolean(), Arb.boolean()) { value1, value2 ->
                everySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) } returns
                    value1
                everySuspend {
                    mockProvider.getBoolean(AnotherTestFlag.key, AnotherTestFlag.default)
                } returns value2
                val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

                val result = repository.get(TestFlag, AnotherTestFlag)

                result shouldContainAll mapOf(TestFlag to value1, AnotherTestFlag to value2)
            }
        }

        test("get with vararg flags should call provider for each flag") {
            val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

            repository.get(TestFlag, AnotherTestFlag)

            verifySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) }
            verifySuspend { mockProvider.getBoolean(AnotherTestFlag.key, AnotherTestFlag.default) }
        }

        test("get with list of flags should return Features object with all flags") {
            checkAll(Arb.boolean(), Arb.boolean()) { value1, value2 ->
                everySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) } returns
                    value1
                everySuspend {
                    mockProvider.getBoolean(AnotherTestFlag.key, AnotherTestFlag.default)
                } returns value2
                val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

                val result = repository.get(listOf(TestFlag, AnotherTestFlag))

                result shouldBe mapOf(TestFlag to value1, AnotherTestFlag to value2).asFeatures()
            }
        }

        test("get with list of flags should call provider for each flag") {
            val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

            repository.get(listOf(TestFlag, AnotherTestFlag))

            verifySuspend { mockProvider.getBoolean(TestFlag.key, TestFlag.default) }
            verifySuspend { mockProvider.getBoolean(AnotherTestFlag.key, AnotherTestFlag.default) }
        }

        test("setUserData should call provider with correct parameters") {
            checkAll(Arb.string(), Arb.string().orNull(), Arb.string().orNull()) {
                userId,
                email,
                country ->
                val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

                repository.setUserData(userId, email, country)

                verifySuspend { mockProvider.setUserProperties(userId, email, country) }
            }
        }

        test("setUserData should handle null values correctly") {
            val repository: FeaturesRepository = FeaturesDataSource(mockProvider)

            repository.setUserData("test-user")

            verifySuspend { mockProvider.setUserProperties("test-user", null, null) }
        }
    })

private object TestFlag : FeatureFlag {
    override val key: String = "test_flag"
    override val default: Boolean = true
}

private object AnotherTestFlag : FeatureFlag {
    override val key: String = "another_test_flag"
    override val default: Boolean = false
}
