package com.example.project.auth

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.project.BuildKonfig
import com.example.project.auth.data.SupabaseAuthDataSource
import com.example.project.auth.domain.repository.AuthRepository
import com.example.project.auth.domain.DefaultSessionManager
import com.example.project.auth.domain.SessionManager
import com.example.project.auth.presentation.component.AuthComponentFactory
import com.example.project.auth.presentation.component.DefaultAuthComponentFactory
import com.example.project.auth.presentation.store.AuthStore
import com.example.project.auth.presentation.store.AuthStoreFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Authentication module for dependency injection.
 *
 * This module provides all authentication-related dependencies including:
 * - Supabase client configuration
 * - Authentication repository implementation
 * - Data source implementation
 */
internal val authModule = module {
    // Supabase client
    singleOf(::supabaseClient)

    // Store factory for MVIKotlin
    single<StoreFactory> { DefaultStoreFactory() }

    // Authentication repository
    single<AuthRepository> { SupabaseAuthDataSource(get()) }

    // Authentication store factory
    single<AuthStoreFactory> { AuthStoreFactory(get(), get(), get()) }

    // Authentication store
    single<AuthStore> { get<AuthStoreFactory>().create() }

    // Authentication component factory
    single<AuthComponentFactory> { DefaultAuthComponentFactory() }

    // Session Manager
    singleOf(::DefaultSessionManager) { bind<SessionManager>() }

    // Auth Repository (assuming it exists)
    // singleOf(::DefaultAuthRepository) { bind<AuthRepository>() }
}

/**
 * Creates and configures the Supabase client with authentication and database support.
 *
 * @return Configured SupabaseClient instance
 */
private fun supabaseClient(): SupabaseClient =
    createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_KEY,
    ) {
        defaultLogLevel = if (BuildKonfig.DEBUG) LogLevel.DEBUG else LogLevel.NONE
        install(Auth)
        install(Postgrest)
    }
