package com.example.project.auth.domain.entity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class AuthResultTest :
    FunSpec({
        test("AuthResult.Success should contain user") {
            val user = User(id = "123", email = "test@example.com")
            val result = AuthResult.Success(user)

            result.shouldBeInstanceOf<AuthResult.Success>()
            result.user shouldBe user
        }

        test("AuthResult.Error should contain error") {
            val error = AuthError.GenericError("Test error")
            val result = AuthResult.Error(error)

            result.shouldBeInstanceOf<AuthResult.Error>()
            result.error shouldBe error
        }

        test("AuthResult.Loading should be loading state") {
            val result = AuthResult.Loading

            result.shouldBeInstanceOf<AuthResult.Loading>()
        }
    })
