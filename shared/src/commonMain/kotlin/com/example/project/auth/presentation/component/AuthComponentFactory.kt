package com.example.project.auth.presentation.component

import com.arkivanov.decompose.ComponentContext

/**
 * Factory for creating AuthComponent instances.
 *
 * This factory provides a way to create AuthComponent instances with proper dependency injection
 * and lifecycle management.
 */
interface AuthComponentFactory {
    fun create(componentContext: ComponentContext): AuthComponent
}

/**
 * Default implementation of AuthComponentFactory.
 *
 * This factory creates DefaultAuthComponent instances with the provided ComponentContext for
 * lifecycle management.
 */
class DefaultAuthComponentFactory : AuthComponentFactory {
    override fun create(componentContext: ComponentContext): AuthComponent {
        return DefaultAuthComponent(componentContext)
    }
}
