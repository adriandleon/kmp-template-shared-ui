# Authentication Module

This module provides a comprehensive, provider-agnostic authentication system for the Compose Multiplatform application. It follows clean architecture principles and allows easy switching between different authentication providers.

## Architecture

The authentication module is structured using clean architecture with three main layers:

### Domain Layer
- **Entities**: `User`, `AuthResult`, `AuthError`
- **Repository Interface**: `AuthRepository`
- **Use Cases**: Business logic for authentication operations

### Data Layer
- **Data Source**: `SupabaseAuthDataSource` (Supabase implementation)
- **Mappers**: Convert between provider-specific and domain models
- **Repository Implementation**: Implements the domain repository interface

### Presentation Layer
- **Store**: `AuthStore` (MVI pattern)
- **Component**: `AuthComponent` (Decompose integration)
- **View**: `AuthScreen` (Compose UI)

## Features

### Authentication Methods
- **Email/Password**: Sign up and sign in with email and password
- **Phone/Password**: Sign up and sign in with phone number and password
- **OTP Authentication**: Sign in with one-time passwords via email or SMS
- **Password Reset**: Reset password via email
- **Account Management**: Update profile, change password, delete account

### Provider Agnostic
The system is designed to be provider-agnostic, allowing easy switching between:
- Supabase (current implementation)
- Firebase Authentication
- Custom authentication providers

## Usage

### Basic Setup

1. **Configure Supabase**: Set up your Supabase project and add credentials to `BuildKonfig`
2. **Inject Dependencies**: The `AuthModule` is already included in the main DI configuration
3. **Create Component**: Use `AuthComponentFactory` to create authentication components

### Example Usage

```kotlin
// Create authentication component
val authComponent = authComponentFactory.create(componentContext)

// Sign up with email
authComponent.signUpWithEmail("user@example.com", "password123", "John Doe")

// Sign in with email
authComponent.signInWithEmail("user@example.com", "password123")

// Listen to authentication state
authComponent.state.collect { state ->
    when {
        state.isAuthenticated -> {
            // User is authenticated
            val user = state.user
        }
        state.isLoading -> {
            // Authentication operation in progress
        }
        state.error != null -> {
            // Handle authentication error
        }
    }
}
```

### UI Integration

```kotlin
@Composable
fun MyAuthScreen(authComponent: AuthComponent) {
    val state by authComponent.state.collectAsState()
    
    AuthScreen(
        authComponent = authComponent,
        modifier = Modifier.fillMaxSize()
    )
}
```

## Error Handling

The system provides comprehensive error handling with specific error types:

- `NetworkError`: Network connectivity issues
- `InvalidCredentials`: Wrong email/password
- `UserNotFound`: User doesn't exist
- `EmailAlreadyExists`: Email already registered
- `PhoneAlreadyExists`: Phone already registered
- `EmailNotVerified`: Email needs verification
- `PhoneNotVerified`: Phone needs verification
- `WeakPassword`: Password doesn't meet requirements
- `InvalidEmail`: Invalid email format
- `InvalidPhone`: Invalid phone format
- `InvalidOtp`: Wrong OTP code
- `OtpExpired`: OTP has expired
- `TooManyAttempts`: Rate limiting
- `UserDisabled`: Account is disabled
- `GenericError`: General error
- `UnknownError`: Unexpected error

## Testing

The module includes comprehensive unit tests for:
- Domain entities
- Data mappers
- Error handling
- Business logic

Run tests with:
```bash
./gradlew :composeApp:testDebugUnitTest
```

## Future Enhancements

### Planned Features
- **Social Authentication**: Google, Apple, Facebook login
- **Multi-Factor Authentication**: TOTP, SMS verification
- **Biometric Authentication**: Fingerprint, face recognition
- **Session Management**: Token refresh, session persistence
- **Offline Support**: Local authentication state management

### Provider Migration
To switch to a different authentication provider:

1. **Create New Data Source**: Implement `AuthRepository` interface
2. **Update Mappers**: Create provider-specific mappers
3. **Update DI**: Replace Supabase implementation in `AuthModule`
4. **Test**: Ensure all functionality works with new provider

Example for Firebase:
```kotlin
class FirebaseAuthDataSource(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    // Implement all AuthRepository methods
}
```

## Dependencies

- **Supabase**: Authentication and database
- **Koin**: Dependency injection
- **Decompose**: Navigation and component lifecycle
- **MVIKotlin**: State management
- **Kotlinx Coroutines**: Asynchronous operations
- **Compose**: UI framework

## Configuration

### Supabase Setup
1. Create a Supabase project
2. Get your project URL and API key
3. Add them to `local.properties`:
   ```
   SUPABASE_URL_DEV_AND=your_supabase_url
   SUPABASE_KEY_DEV=your_supabase_key
   ```

### Environment Variables
The system supports different configurations for development and production:
- Development: Uses dev Supabase project
- Production: Uses production Supabase project
- iOS: Separate configuration for iOS targets

## Security Considerations

- **API Keys**: Never commit API keys to version control
- **Password Requirements**: Enforce strong password policies
- **Rate Limiting**: Implement rate limiting for authentication attempts
- **Session Management**: Proper session token handling
- **Data Validation**: Validate all user inputs
- **Error Messages**: Don't expose sensitive information in error messages

## Troubleshooting

### Common Issues

1. **Network Errors**: Check internet connectivity and Supabase service status
2. **Invalid Credentials**: Verify email/password combination
3. **Email Not Verified**: Check email for verification link
4. **Rate Limiting**: Wait before retrying authentication
5. **Configuration**: Verify Supabase URL and API key

### Debug Mode
Enable debug logging by setting `BuildKonfig.DEBUG = true` in development builds.

## Contributing

When adding new authentication features:
1. Follow clean architecture principles
2. Add comprehensive tests
3. Update documentation
4. Consider provider compatibility
5. Handle errors gracefully
