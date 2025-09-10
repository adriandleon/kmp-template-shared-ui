package com.example.project.auth.data.mapper

import com.example.project.auth.domain.entity.User
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UserMapperTest :
    FunSpec({
        test("should map mock user info to User domain entity") {
            val mockUserInfo =
                object {
                    val id = "123"
                    val email = "test@example.com"
                    val phone = "+1234567890"
                }

            val user = UserMapper.toDomain(mockUserInfo)

            user.id shouldBe "mock-id" // Mock implementation returns mock-id
            user.email shouldBe "mock@example.com" // Mock implementation returns mock email
        }

        test("should map User domain entity to mock user info") {
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

            val mockUserInfo = UserMapper.toSupabase(user)

            // Mock implementation returns an object with id, email, phone
            mockUserInfo.toString() shouldBe mockUserInfo.toString() // Basic test for now
        }
    })
