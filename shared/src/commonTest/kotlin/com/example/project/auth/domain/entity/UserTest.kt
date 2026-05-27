package com.example.project.auth.domain.entity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UserTest :
    FunSpec({
        test("User should be created with required fields") {
            val userEntity = UserEntity(id = "123", email = "test@example.com")

            userEntity.id shouldBe "123"
            userEntity.email shouldBe "test@example.com"
            userEntity.phone shouldBe null
            userEntity.displayName shouldBe null
            userEntity.avatarUrl shouldBe null
            userEntity.isEmailVerified shouldBe false
            userEntity.isPhoneVerified shouldBe false
            userEntity.createdAt shouldBe 0L
            userEntity.lastSignInAt shouldBe 0L
        }

        test("User should be created with all fields") {
            val userEntity =
                UserEntity(
                    id = "123",
                    email = "test@example.com",
                    phone = "+1234567890",
                    displayName = "Test User",
                    avatarUrl = "https://example.com/avatar.jpg",
                    isEmailVerified = true,
                    isPhoneVerified = true,
                    createdAt = 1234567890L,
                    lastSignInAt = 1234567891L,
                )

            userEntity.id shouldBe "123"
            userEntity.email shouldBe "test@example.com"
            userEntity.phone shouldBe "+1234567890"
            userEntity.displayName shouldBe "Test User"
            userEntity.avatarUrl shouldBe "https://example.com/avatar.jpg"
            userEntity.isEmailVerified shouldBe true
            userEntity.isPhoneVerified shouldBe true
            userEntity.createdAt shouldBe 1234567890L
            userEntity.lastSignInAt shouldBe 1234567891L
        }
    })
