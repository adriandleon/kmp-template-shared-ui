package com.example.project.auth.domain.entity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UserTest :
    FunSpec({
        test("User should be created with required fields") {
            val user = User(id = "123", email = "test@example.com")

            user.id shouldBe "123"
            user.email shouldBe "test@example.com"
            user.phone shouldBe null
            user.displayName shouldBe null
            user.avatarUrl shouldBe null
            user.isEmailVerified shouldBe false
            user.isPhoneVerified shouldBe false
            user.createdAt shouldBe 0L
            user.lastSignInAt shouldBe 0L
        }

        test("User should be created with all fields") {
            val user =
                User(
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

            user.id shouldBe "123"
            user.email shouldBe "test@example.com"
            user.phone shouldBe "+1234567890"
            user.displayName shouldBe "Test User"
            user.avatarUrl shouldBe "https://example.com/avatar.jpg"
            user.isEmailVerified shouldBe true
            user.isPhoneVerified shouldBe true
            user.createdAt shouldBe 1234567890L
            user.lastSignInAt shouldBe 1234567891L
        }
    })
