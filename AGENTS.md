# AGENTS.md

A Compose Multiplatform template project with shared UI targeting Android and iOS, following modern mobile development best practices.

## Project Overview

This is a production-ready Compose Multiplatform template featuring:
- **Shared UI**: Compose Multiplatform with Material3 design system
- **Architecture**: MVVM + MVI with MVIKotlin for state management
- **Navigation**: Decompose for type-safe navigation with deeplink support
- **Backend**: Supabase for authentication and database, Firebase for analytics
- **Dependency Injection**: Koin for cross-platform dependency management
- **Testing**: Kotest framework with 90%+ code coverage requirement

## Setup Commands

### Prerequisites
- **Android Studio** (latest stable version)
- **Xcode** 14+ (for iOS development)
- **JDK 17+**
- **Kotlin** 2.2.20+
- **Gradle** 8.13.0+

### Initial Setup
```bash
# Clone and navigate to project
git clone <repository-url>
cd kmp-template-shared-ui

# Configure your project (run setup script)
./setup_new_project.sh

# Set up environment variables
cp local.properties.example local.properties
# Edit local.properties with your API keys
```

### Development Commands
```bash
# Clean and build
./gradlew clean build

# Run all tests
./gradlew test

# Run tests with coverage
./gradlew koverHtmlReport

# Format code
./gradlew ktfmtFormat

# Lint code
./gradlew detektAll

# Android specific
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:assembleRelease

# iOS specific (from Xcode)
open iosApp/AppTemplate.xcodeproj
```

## Code Style

### Kotlin
- **Formatting**: ktfmt with official Kotlin style guide
- **Linting**: Detekt with Compose rules
- **Naming**: PascalCase for composables, camelCase for functions
- **Architecture**: MVVM + MVI pattern with sealed interfaces for state
- **Dependencies**: Use Koin for dependency injection

### Swift
- **Formatting**: SwiftFormat
- **Linting**: SwiftLint
- **Naming**: Follow Apple's Swift API Design Guidelines

### Compose UI
- **Composable Functions**: Use PascalCase, descriptive names
- **State Management**: Use `remember`, `mutableStateOf`, `StateFlow`
- **State Hoisting**: Move state as high as possible in the tree
- **Unidirectional Data Flow**: Data flows down, events flow up
- **Material3**: Use Material3 components and theming
- **Preview Annotations**: Every composable MUST have `@Preview`
- **Testing**: Use `Modifier.testTag()` for UI testing

### Code Organization
- **Package Structure**: Organize by feature, not by layer
- **Naming Conventions**:
  - Entities: `*Entity.kt`
  - Repositories: `*Repository.kt`
  - Use Cases: `*UseCase.kt`
  - Components: `*Component.kt`
  - Stores: `*Store.kt`
  - Composables: `*Screen.kt`, `*Component.kt`

### Platform Abstraction
- **Expect/Actual**: Use for platform-specific implementations
- **Common Interfaces**: Define in `commonMain`
- **Platform Implementations**: Implement in `androidMain`/`iosMain`
- **Dependencies**: Use KMP-compatible libraries only

## Testing Instructions

### Running Tests
```bash
# All tests
./gradlew test

# Specific platform tests
./gradlew :composeApp:testDebugUnitTest
./gradlew :composeApp:testReleaseUnitTest

# Coverage report
./gradlew koverHtmlReport
# Open: build/reports/kover/html/index.html

# UI tests (Android)
./gradlew :composeApp:connectedAndroidTest
```

### Test Structure
- **Unit Tests**: `commonTest/` and `androidTest/` directories
- **UI Tests**: `androidInstrumentedTest/` directory
- **Coverage**: Minimum 90% code coverage required
- **Framework**: Kotest for unit tests, ComposeTestRule for UI tests

### Test Organization
- Group tests by feature in `commonTest/kotlin/features/`
- Use page object pattern for UI tests
- Mock dependencies with Mokkery
- Test both success and error scenarios

### Internationalization
- **Languages**: English (default), Spanish (es-r419), Portuguese (pt-rBR)
- **String Resources**: Extract all text to `strings.xml`
- **Content Description**: Provide accessibility descriptions
- **Preview Locales**: Test UI in all supported languages

## Build Configuration

### Environment Setup
- **Debug**: Uses development Supabase instance
- **Release**: Uses production Supabase instance
- **Feature Flags**: ConfigCat integration for feature toggles
- **Secrets**: Store in `local.properties` (not committed)

### Required Environment Variables
```properties
# Supabase Configuration
SUPABASE_URL_DEV_AND=http://10.0.2.2:54321
SUPABASE_URL_DEV_IOS=http://127.0.0.1:54321
SUPABASE_KEY_DEV=your_supabase_anon_key
SUPABASE_URL_PROD=your_production_supabase_url
SUPABASE_KEY_PROD=your_production_supabase_key

# ConfigCat Configuration
CONFIGCAT_TEST_KEY=your_configcat_test_key
CONFIGCAT_LIVE_KEY=your_configcat_live_key
```

## Architecture Patterns

### MVVM + MVI with MVIKotlin
- **Store Interface**: Define `State`, `Intent`, `Message`, and `Action`
- **Pattern Flow**: `Intent` → `Store` → `Message` → `State`
- **State Management**: Use sealed interfaces for type-safe state
- **ViewModel**: Bridge between UI and business logic

### Clean Architecture Layers
```
presentation/     # UI layer (Compose + ViewModels)
├── component/    # Decompose components
├── mapper/       # UI state mappers
└── store/        # MVIKotlin stores

domain/          # Business logic layer
├── entity/       # Domain models
├── repository/   # Repository interfaces
└── usecase/      # Business use cases

data/            # Data layer
├── datasource/   # Data sources (Supabase, DataStore)
├── mapper/       # Data mappers
└── repository/   # Repository implementations
```

### Dependency Injection with Koin
- **Module Definition**: Define modules in `commonMain/kotlin/common/di/`
- **Component Integration**: Use `@KoinComponent` for dependency injection
- **Factory Dependencies**: Use `factoryOf(::Constructor)`
- **Singleton Dependencies**: Use `singleOf(::Constructor)`

### Data Management

#### Supabase Integration
- **Client**: Use PostgREST client for database operations
- **Authentication**: Handle auth with Supabase Auth
- **Real-time**: Use Supabase real-time subscriptions
- **Error Handling**: Implement proper error handling

#### DataStore Preferences
- **Preferences**: Use for app settings and user preferences
- **Type Safety**: Use typed preferences with DataStore
- **Migration**: Handle preference migration from SharedPreferences

#### Ktor Networking
- **Client**: Configure with platform-specific engines (OkHttp/Darwin)
- **Serialization**: Use Kotlinx Serialization for JSON
- **Interceptors**: Add logging and error handling interceptors
- **SSL Pinning**: Implement certificate pinning for security

## Platform-Specific Guidelines

### Android
- **Target SDK**: API 36
- **Min SDK**: API 26
- **Build Variants**: Debug and Release
- **Signing**: Configure in `local.properties`
- **ProGuard**: Enabled for release builds

### iOS
- **Target**: iOS 13+
- **Xcode Project**: `iosApp/AppTemplate.xcodeproj`
- **Bundle ID**: Configure in `iosApp/Configuration/Config.xcconfig`
- **Certificates**: Configure in Xcode

## Code Quality

### Pre-commit Hooks
```bash
# Install pre-commit hooks
./gradlew installGitHooks

# Run manually
./gradlew detektAll ktfmtFormat test
```

### Linting Rules
- **Detekt**: Configured in `config/detekt.yml`
- **Compose Rules**: Detekt Compose rules enabled
- **Coverage**: 90% minimum coverage requirement
- **Formatting**: ktfmt enforces official Kotlin style

## Documentation

### Key Documentation Files
- [Dependency Injection Guide](docs/DEPENDENCY_INJECTION_GUIDE.md)
- [Deeplinking Guide](docs/DEEPLINKING_GUIDE.md)
- [Analytics Integration](docs/ANALYTICS_INTEGRATION.md)
- [Supabase Integration](docs/SUPABASE_INTEGRATION.md)
- [Firebase Integration](docs/FIREBASE_INTEGRATION.md)
- [Unit Tests Guide](docs/UNIT_TESTS_SHARED.md)

### Code Documentation
- Use KDoc for public APIs
- Document complex business logic
- Include usage examples in documentation
- Keep README.md updated with setup instructions

## Deployment

### Android Deployment
1. Update version in `composeApp/build.gradle.kts`
2. Configure signing keys in `local.properties`
3. Run: `./gradlew :composeApp:bundleRelease`
4. Upload to Google Play Console

### iOS Deployment
1. Configure certificates and provisioning profiles in Xcode
2. Update version in Xcode project
3. Archive and upload to App Store Connect
4. Distribute via TestFlight or App Store

## Troubleshooting

### Common Issues
- **Build Failures**: Check `local.properties` for missing API keys
- **iOS Build Issues**: Ensure Xcode project is properly configured
- **Test Failures**: Run `./gradlew clean test` to clear cache
- **Coverage Issues**: Check `kover` configuration in `build.gradle.kts`

### Debug Commands
```bash
# Clean everything
./gradlew clean

# Check dependencies
./gradlew :composeApp:dependencies

# Run specific test
./gradlew :composeApp:testDebugUnitTest --tests "com.example.project.features.*"

# Check linting issues
./gradlew detektAll --continue
```

## Security Considerations

- **API Keys**: Never commit `local.properties` or API keys
- **Secrets**: Use BuildKonfig for build-time configuration
- **Network**: Use HTTPS for all API calls
- **Authentication**: Implement proper auth flows with Supabase
- **Data**: Encrypt sensitive data at rest

## Performance Optimization

- **Compose Performance**: Use `remember`, `derivedStateOf`, `LazyColumn`
- **Memory Management**: Implement proper lifecycle management
- **Image Loading**: Use Coil for efficient image loading
- **Database Queries**: Optimize Supabase queries and use indices
- **Build Performance**: Use Gradle build cache and configuration cache

## Contributing

### Pull Request Guidelines
- **Title Format**: `[Feature] Brief description` or `[Fix] Brief description`
- **Testing**: All tests must pass before merging
- **Coverage**: Maintain 90%+ code coverage
- **Documentation**: Update relevant documentation
- **Code Quality**: Pass all linting and formatting checks

### Development Workflow
1. Create feature branch from `main`
2. Make changes following code style guidelines
3. Add/update tests as needed
4. Run `./gradlew detektAll ktfmtFormat test`
5. Create pull request with detailed description
6. Address review feedback
7. Merge after approval and CI passes

## Common Patterns

- **Repository Pattern**: Abstract data access
- **Use Case Pattern**: Encapsulate business logic
- **Observer Pattern**: Use StateFlow/SharedFlow for reactive streams
- **Factory Pattern**: Use for object creation
- **Builder Pattern**: Use for complex object construction

## Additional Resources

- [Compose Multiplatform Documentation](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Decompose Navigation](https://github.com/arkivanov/Decompose)
- [Koin Dependency Injection](https://insert-koin.io/)
- [Supabase Documentation](https://supabase.com/docs)
- [Firebase Documentation](https://firebase.google.com/docs)
