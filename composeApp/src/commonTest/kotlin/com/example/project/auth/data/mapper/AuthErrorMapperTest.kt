package com.example.project.auth.data.mapper

import com.example.project.auth.domain.entity.AuthError
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class AuthErrorMapperTest :
    FunSpec({
        test("should map network error message to NetworkError") {
            val error = AuthErrorMapper.toDomain("Network error occurred")
            error.shouldBeInstanceOf<AuthError.NetworkError>()
            error.message shouldBe "Network error occurred"
        }

        test("should map invalid credentials message to InvalidCredentials") {
            val error = AuthErrorMapper.toDomain("Invalid credentials provided")
            error.shouldBeInstanceOf<AuthError.InvalidCredentials>()
            error.message shouldBe "Invalid credentials provided"
        }

        test("should map user not found message to UserNotFound") {
            val error = AuthErrorMapper.toDomain("User not found")
            error.shouldBeInstanceOf<AuthError.UserNotFound>()
            error.message shouldBe "User not found"
        }

        test("should map email already exists message to EmailAlreadyExists") {
            val error = AuthErrorMapper.toDomain("Email already registered")
            error.shouldBeInstanceOf<AuthError.EmailAlreadyExists>()
            error.message shouldBe "Email already registered"
        }

        test("should map phone already exists message to PhoneAlreadyExists") {
            val error = AuthErrorMapper.toDomain("Phone already registered")
            error.shouldBeInstanceOf<AuthError.PhoneAlreadyExists>()
            error.message shouldBe "Phone already registered"
        }

        test("should map email not verified message to EmailNotVerified") {
            val error = AuthErrorMapper.toDomain("Email not confirmed")
            error.shouldBeInstanceOf<AuthError.EmailNotVerified>()
            error.message shouldBe "Email not confirmed"
        }

        test("should map phone not verified message to PhoneNotVerified") {
            val error = AuthErrorMapper.toDomain("Phone not confirmed")
            error.shouldBeInstanceOf<AuthError.PhoneNotVerified>()
            error.message shouldBe "Phone not confirmed"
        }

        test("should map weak password message to WeakPassword") {
            val error = AuthErrorMapper.toDomain("Password too weak")
            error.shouldBeInstanceOf<AuthError.WeakPassword>()
            error.message shouldBe "Password too weak"
        }

        test("should map invalid email message to InvalidEmail") {
            val error = AuthErrorMapper.toDomain("Invalid email format")
            error.shouldBeInstanceOf<AuthError.InvalidEmail>()
            error.message shouldBe "Invalid email format"
        }

        test("should map invalid phone message to InvalidPhone") {
            val error = AuthErrorMapper.toDomain("Invalid phone format")
            error.shouldBeInstanceOf<AuthError.InvalidPhone>()
            error.message shouldBe "Invalid phone format"
        }

        test("should map invalid OTP message to InvalidOtp") {
            val error = AuthErrorMapper.toDomain("Invalid OTP provided")
            error.shouldBeInstanceOf<AuthError.InvalidOtp>()
            error.message shouldBe "Invalid OTP provided"
        }

        test("should map OTP expired message to OtpExpired") {
            val error = AuthErrorMapper.toDomain("OTP expired")
            error.shouldBeInstanceOf<AuthError.OtpExpired>()
            error.message shouldBe "OTP expired"
        }

        test("should map too many attempts message to TooManyAttempts") {
            val error = AuthErrorMapper.toDomain("Too many attempts")
            error.shouldBeInstanceOf<AuthError.TooManyAttempts>()
            error.message shouldBe "Too many attempts"
        }

        test("should map user disabled message to UserDisabled") {
            val error = AuthErrorMapper.toDomain("User disabled")
            error.shouldBeInstanceOf<AuthError.UserDisabled>()
            error.message shouldBe "User disabled"
        }

        test("should map unknown message to GenericError") {
            val error = AuthErrorMapper.toDomain("Some unknown error")
            error.shouldBeInstanceOf<AuthError.GenericError>()
            error.message shouldBe "Some unknown error"
        }

        test("should map unknown exception to UnknownError") {
            val exception = RuntimeException("Unknown error")
            val error = AuthErrorMapper.toDomain(exception)
            error.shouldBeInstanceOf<AuthError.UnknownError>()
            error.message shouldBe "Unknown error"
        }
    })
