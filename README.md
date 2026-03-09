# Compose Multiplatform Template - Shared UI

A modern, production-ready Compose Multiplatform template project targeting Android and iOS with shared UI components, following best practices for scalable mobile development.

## 🚀 Features

### Core Technologies
- **Compose Multiplatform**: Shared UI across Android and iOS
- **Material3**: Modern design system and UI components
- **Decompose**: Navigation and component lifecycle management
- **MVVM + MVI**: Clean architecture with MVIKotlin for state management
- **Dependency Injection**: Koin for dependency management

### Backend & Data
- **Supabase**: Backend-as-a-Service with authentication and database
- **Firebase**: Analytics, Crashlytics, and Remote Config
- **DataStore**: Local preferences and settings storage
- **Ktor**: Cross-platform networking with platform-specific engines

### Development Tools
- **Code Quality**: ktfmt formatting, Detekt linting with Compose rules
- **Testing**: Kotest framework with 90%+ code coverage requirement
- **CI/CD**: GitHub Actions with automated testing and deployment
- **Logging**: Kermit for cross-platform logging with Crashlytics integration

### Platform Support
- **Android**: API 26+ with modern Android features
- **iOS**: iOS 13.0+ with native integration
- **Shared Code**: Maximum code sharing between platforms

## 📁 Project Structure

> **Note**: This project uses **AGP 9.0** with the new module structure. The Android entry point is in a separate `androidApp` module, while shared code is in `composeApp` using the `android.multiplatform.library` plugin. See the [AGP 9.0 migration guide](https://kotlinlang.org/docs/multiplatform/multiplatform-project-agp-9-migration.html) for details.

```
kmp-template-shared-ui/
├── androidApp/                   # Android app entry point (AGP 9.0)
│   ├── src/
│   │   └── main/                 # Android app code
│   │       ├── kotlin/com/example/project/
│   │       │   ├── MainActivity.kt
│   │       │   └── MainApplication.kt
│   │       ├── res/              # Android resources
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── composeApp/                   # Shared Compose Multiplatform module
│   ├── src/
│   │   ├── commonMain/           # Shared code (UI, business logic, data)
│   │   │   ├── kotlin/com/example/project/
│   │   │   │   ├── auth/         # Authentication module
│   │   │   │   ├── common/       # Common utilities and DI
│   │   │   │   ├── features/     # Feature modules
│   │   │   │   ├── root/         # Root navigation
│   │   │   │   └── tabs/         # Tab navigation
│   │   │   └── composeResources/ # Shared resources
│   │   ├── iosMain/              # iOS-specific implementations
│   │   ├── commonTest/           # Shared unit tests
│   │   └── androidInstrumentedTest/ # Android UI tests
│   └── build.gradle.kts
├── iosApp/                       # iOS app wrapper
│   ├── CMP-Template/              # iOS app target
│   └── CMP-Template.xcodeproj/    # Xcode project
├── docs/                         # Comprehensive documentation
├── gradle/                       # Gradle configuration
│   └── libs.versions.toml        # Dependency versions
└── config/                       # Configuration files
    ├── detekt.yml               # Linting rules
    └── .swiftformat             # Swift formatting rules
```

## 🛠️ Quick Start

### Prerequisites
- **Android Studio** or **IntelliJ IDEA** (latest stable version)
- **Xcode** 16+ (for iOS development)
- **JDK 21+**
- **Kotlin** 2.3.0+
- **Gradle** 9.3.0+
- **Android Gradle Plugin (AGP)** 9.0.0+

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd kmp-template-shared-ui
   ```

2. **Configure your project**
   - Update package names in `androidApp/build.gradle.kts` and `composeApp/build.gradle.kts`
   - Update app identifiers in `iosApp/Configuration/Config.xcconfig`
   - Configure your Firebase project (see [Firebase Integration](docs/FIREBASE_INTEGRATION.md))

3. **Set up environment variables**
   Create `local.properties` in the root directory:
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

4. **Run the project**
   ```bash
   # Android
   ./gradlew :androidApp:assembleDebug
   
   # iOS (from Xcode)
   open iosApp/CMP-Template.xcodeproj
   ```

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│           Presentation              │
│  ┌─────────────┐  ┌─────────────┐  │
│  │   Compose   │  │  ViewModels │  │
│  │     UI      │  │   (MVI)     │  │
│  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│             Domain                  │
│  ┌─────────────┐  ┌─────────────┐  │
│  │  Use Cases  │  │  Entities   │  │
│  │             │  │             │  │
│  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│              Data                   │
│  ┌─────────────┐  ┌─────────────┐  │
│  │ Repositories│  │ Data Sources│  │
│  │             │  │ (Supabase,  │  │
│  │             │  │  DataStore) │  │
│  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────┘
```

### Key Patterns

- **MVVM + MVI**: ViewModels with MVIKotlin for predictable state management
- **Repository Pattern**: Abstract data access with platform-specific implementations
- **Dependency Injection**: Koin for managing dependencies across platforms
- **Navigation**: Decompose for type-safe navigation with deeplink support

## 📚 Documentation

### Core Guides
- [Dependency Injection Guide](docs/DEPENDENCY_INJECTION_GUIDE.md) - Complete Koin setup and usage
- [Deeplinking Guide](docs/DEEPLINKING_GUIDE.md) - Cross-platform deeplink implementation
- [Analytics Integration](docs/ANALYTICS_INTEGRATION.md) - Firebase Analytics setup and usage

### Backend Integration
- [Supabase Integration](docs/SUPABASE_INTEGRATION.md) - Database, auth, and real-time features
- [Firebase Integration](docs/FIREBASE_INTEGRATION.md) - Analytics, Crashlytics, and Remote Config

### Development Workflow
- [Code Coverage Reports](docs/CODE_COVERAGE_REPORTS.md) - Testing and coverage setup
- [Unit Tests Guide](docs/UNIT_TESTS_SHARED.md) - Testing strategy with Kotest
- [Kotlin Format & Lint](docs/KOTLIN_FORMAT_LINT.md) - Code quality tools
- [Swift Format & Lint](docs/SWIFT_FORMAT_LINT.md) - iOS code quality tools
- [Pre-commit Hooks](docs/PRE_COMMIT_HOOKS.md) - Automated code quality checks

### Deployment
- [Android Deployment](docs/DEPLOY_ANDROID.md) - Google Play Store release process
- [iOS Deployment](docs/DEPLOY_IOS.md) - TestFlight and App Store release process

### Additional Features
- [Logging Multiplatform](docs/LOGGING_MULTIPLATFORM.md) - Cross-platform logging with Kermit
- [PR Danger Checks](docs/PR_DANGER_CHECKS.md) - Automated PR quality checks

## 🧪 Testing

The project includes comprehensive testing setup:

- **Unit Tests**: Kotest framework with 90%+ coverage requirement
- **UI Tests**: Compose UI testing for Android
- **Architecture Tests**: Konsist for architectural consistency
- **Integration Tests**: End-to-end testing capabilities

Run tests:
```bash
# All tests
./gradlew test

# Coverage report
./gradlew koverHtmlReport

# Specific platform
./gradlew :composeApp:testDebugUnitTest
./gradlew :androidApp:testDebugUnitTest
```

## 🚀 Deployment

### Android
1. Configure signing keys in `local.properties`
2. Update version in `androidApp/build.gradle.kts`
3. Run: `./gradlew :androidApp:bundleRelease`
4. Upload to Google Play Console

### iOS
1. Configure certificates and provisioning profiles
2. Update version in Xcode project
3. Archive and upload to App Store Connect
4. Distribute via TestFlight or App Store

## 🔧 Configuration

### Environment Setup
- **Debug**: Uses development Supabase instance
- **Release**: Uses production Supabase instance
- **Feature Flags**: ConfigCat integration for feature toggles

### Code Quality
- **Formatting**: ktfmt for Kotlin, SwiftFormat for Swift
- **Linting**: Detekt for Kotlin, SwiftLint for Swift
- **Coverage**: 90% minimum code coverage requirement

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and linting: `./gradlew detektAll ktfmtFormat test`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [JetBrains Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Decompose](https://github.com/arkivanov/Decompose) for navigation
- [Koin](https://insert-koin.io/) for dependency injection
- [Supabase](https://supabase.com/) for backend services
- [Firebase](https://firebase.google.com/) for analytics and crash reporting

---

**Ready to build amazing cross-platform apps?** Start with this template and follow the documentation to create your next mobile application! 🚀