package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.example.project.auth.presentation.store.AuthStore
import com.example.project.common.util.PreviewComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewAuthComponent : AuthComponent, ComponentContext by PreviewComponentContext {
    override val state: StateFlow<AuthStore.State> = MutableStateFlow(AuthStore.State())

    override fun signUpWithEmail(email: String, password: String, displayName: String?) {
        // No-op for preview
    }

    override fun signUpWithPhone(phone: String, password: String, displayName: String?) {
        // No-op for preview
    }

    override fun signInWithEmail(email: String, password: String) {
        // No-op for preview
    }

    override fun signInWithPhone(phone: String, password: String) {
        // No-op for preview
    }

    override fun signInWithEmailOtp(email: String, otp: String) {
        // No-op for preview
    }

    override fun signInWithPhoneOtp(phone: String, otp: String) {
        // No-op for preview
    }

    override fun sendEmailOtp(email: String) {
        // No-op for preview
    }

    override fun sendPhoneOtp(phone: String) {
        // No-op for preview
    }

    override fun signOut() {
        // No-op for preview
    }

    override fun resetPassword(email: String) {
        // No-op for preview
    }

    override fun updatePassword(currentPassword: String, newPassword: String) {
        // No-op for preview
    }

    override fun updateEmail(newEmail: String, password: String) {
        // No-op for preview
    }

    override fun updatePhone(newPhone: String, password: String) {
        // No-op for preview
    }

    override fun updateDisplayName(displayName: String) {
        // No-op for preview
    }

    override fun updateAvatarUrl(avatarUrl: String) {
        // No-op for preview
    }

    override fun deleteAccount(password: String) {
        // No-op for preview
    }

    override fun refreshSession() {
        // No-op for preview
    }

    override fun clearError() {
        // No-op for preview
    }
}
