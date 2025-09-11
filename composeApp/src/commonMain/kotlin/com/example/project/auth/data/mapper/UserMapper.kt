package com.example.project.auth.data.mapper

import com.example.project.auth.domain.entity.UserEntity

/**
 * Mapper for converting between Supabase UserInfo and domain User entity.
 *
 * This mapper handles the conversion between provider-specific user data and the domain layer's
 * User entity, ensuring clean separation of concerns.
 */
object UserMapper {
    /**
     * Converts Supabase UserInfo to domain User entity.
     *
     * @param userInfo Supabase user information
     * @return Domain User entity
     */
    fun toDomain(userInfo: Any): UserEntity {
        // TODO: Implement actual mapping when Supabase API is properly integrated
        // For now, return a mock user
        return UserEntity(
            id = "mock-id",
            email = "mock@example.com",
            phone = null,
            displayName = null,
            avatarUrl = null,
            isEmailVerified = false,
            isPhoneVerified = false,
            createdAt = 0L,
            lastSignInAt = 0L,
        )
    }

    /**
     * Converts domain User entity to Supabase UserInfo. This is primarily used for testing or when
     * we need to create UserInfo from domain data.
     *
     * @param userEntity Domain User entity
     * @return Supabase UserInfo
     */
    fun toSupabase(userEntity: UserEntity): Any {
        // TODO: Implement actual mapping when Supabase API is properly integrated
        // For now, return a mock object
        return object {
            val id = userEntity.id
            val email = userEntity.email
            val phone = userEntity.phone
        }
    }
}
