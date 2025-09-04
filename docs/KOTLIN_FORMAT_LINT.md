# Kotlin Code Quality & Formatting

This document covers the Kotlin code quality tools used in this project: **Ktfmt** for code formatting and **Detekt** for static code analysis.

## 📋 Table of Contents

- [🎯 Overview](#-overview)
- [✨ Code Formatting with Ktfmt](#-code-formatting-with-ktfmt)
- [🔍 Static Analysis with Detekt](#-static-analysis-with-detekt)
- [🪝 Pre-commit Hooks](#-pre-commit-hooks)
- [🔄 CI/CD Integration](#-cicd-integration)
- [⚙️ Configuration](#-configuration)
- [🐛 Troubleshooting](#-troubleshooting)

## 🎯 Overview

The project uses two main tools to maintain code quality:

- **Ktfmt** - Facebook's Kotlin code formatter that follows the official Kotlin style guide
- **Detekt** - Static code analysis tool that detects code smells and enforces coding standards
- **Detekt Compose Rules** - Additional rules specifically for Compose Multiplatform code

## ✨ Code Formatting with Ktfmt

### What is Ktfmt?

[Ktfmt](https://github.com/facebook/ktfmt) is a Kotlin code formatter that automatically formats your code according to the official Kotlin style guide. It's designed to be opinionated and require minimal configuration.

### Installation

#### Local Installation (Optional)
```bash
# macOS
brew install ktfmt

# Linux
# Download from https://github.com/facebook/ktfmt/releases
```

#### Gradle Plugin
The project uses the [ktfmt-gradle](https://github.com/cortinico/ktfmt-gradle) plugin for seamless integration.

**Add to each module's `build.gradle.kts`:**
```kotlin
plugins {
    alias(libs.plugins.ktfmt.gradle)
}
```

### Configuration

**Configure in each module's `build.gradle.kts`:**
```kotlin
ktfmt {
    kotlinLangStyle()           // Use official Kotlin style guide
    removeUnusedImports = true  // Remove unused imports automatically
    manageTrailingCommas = true // Handle trailing commas properly
}
```

### Usage

#### Manual Formatting
```bash
# Format all code
./gradlew ktfmtFormat

# Check formatting without changes
./gradlew ktfmtCheck
```

#### IDE Integration
- **Android Studio**: Install the Ktfmt plugin for real-time formatting
- **VS Code**: Install the Ktfmt extension

## 🔍 Static Analysis with Detekt

### What is Detekt?

[Detekt](https://detekt.dev/) is a static code analysis tool for Kotlin that helps identify code smells, enforce coding standards, and maintain code quality.

### Installation

#### Gradle Plugin
**Add to root `build.gradle.kts`:**
```kotlin
plugins { 
    alias(libs.plugins.detekt) apply false
}

subprojects {
    tasks.register("detektAll") {
        allprojects { this@register.dependsOn(tasks.withType<Detekt>()) }
    }
}
```

**Add to each module's `build.gradle.kts`:**
```kotlin
plugins {
    alias(libs.plugins.detekt)
}

dependencies {
    detektPlugins(libs.detekt.compose)  // Compose-specific rules
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
}
```

#### Compose Rules
The project includes [Detekt Compose Rules](https://github.com/mrmans0n/compose-rules) for Compose Multiplatform specific analysis.

### Configuration

The main configuration is in `config/detekt.yml`. Key configurations include:

- **Rule exclusions** for platform-specific files
- **Custom rule sets** for project-specific needs
- **Thresholds** for code complexity and size

### Usage

#### Manual Analysis
```bash
# Run on all modules
./gradlew detektAll

# Run on specific module
./gradlew :shared:detekt
./gradlew :composeApp:detekt
```

#### IDE Integration
- **Android Studio**: Install the Detekt plugin for real-time analysis
- **VS Code**: Install the Detekt extension

## 🪝 Pre-commit Hooks

### Automatic Code Quality

Pre-commit hooks automatically run code quality checks before each commit, ensuring code meets quality standards.

### Setup
```bash
# Install pre-commit
pip install pre-commit

# Install hooks
pre-commit install
```

### What Runs Automatically
- **Ktfmt formatting** - Ensures consistent code style
- **Detekt analysis** - Catches code quality issues
- **Other quality checks** - As configured in `.pre-commit-config.yaml`

### Manual Hook Execution
```bash
# Run all hooks on all files
pre-commit run --all-files

# Run specific hook
pre-commit run ktfmt --all-files
pre-commit run detekt --all-files
```

## 🚀 CI/CD Integration

### Automated Quality Checks

The project includes GitHub Actions workflows that automatically run code quality checks on every Pull Request.

#### Workflow: `shared_test_lint.yml`
- **Trigger**: Pull Requests
- **Jobs**: 
  - Kotlin formatting check (`ktfmtCheck`)
  - Detekt analysis (`detektAll`)
  - Unit tests
  - Danger checks

#### Configuration
The workflow is fully configurable with variables at the top. See [GitHub Actions Workflows](GITHUB_ACTIONS.md) for customization details.

### Local vs CI
- **Local**: Use pre-commit hooks for immediate feedback
- **CI**: Automated checks ensure quality on all PRs

## ⚙️ Configuration

### Ktfmt Options

| Option | Description | Default |
|--------|-------------|---------|
| `kotlinLangStyle()` | Use official Kotlin style guide | `true` |
| `removeUnusedImports` | Remove unused imports | `true` |
| `manageTrailingCommas` | Handle trailing commas | `true` |

### Detekt Options

| Option | Description | Default |
|--------|-------------|---------|
| `parallel` | Enable parallel processing | `true` |
| `buildUponDefaultConfig` | Extend default configuration | `true` |
| `config.setFrom()` | Custom configuration file | `config/detekt.yml` |

### Custom Rules

Add custom Detekt rules in `config/detekt.yml`:

```yaml
complexity:
  LongParameterList:
    active: true
    threshold: 6
    functionThreshold: 8
    constructorThreshold: 7
    ignoreDefaultParameters: true
    ignoreDataClasses: true
    ignoreAnnotated: []
```

## 🐛 Troubleshooting

### Common Issues

#### Ktfmt Issues
1. **Formatting conflicts**
   ```bash
   # Reset formatting and reformat
   ./gradlew ktfmtFormat
   ```

2. **Plugin not found**
   - Ensure plugin is added to `libs.versions.toml`
   - Sync Gradle project

#### Detekt Issues
1. **Configuration not found**
   - Verify `config/detekt.yml` exists
   - Check path in `build.gradle.kts`

2. **Rule conflicts**
   - Review `config/detekt.yml` for conflicting rules
   - Check rule exclusions for platform-specific files

3. **Performance issues**
   - Enable parallel processing
   - Exclude generated files from analysis

### Debug Mode

#### Enable Verbose Logging
```bash
# Gradle debug
./gradlew detektAll --debug

# Pre-commit debug
pre-commit run --all-files --verbose
```

#### Check Configuration
```bash
# Verify Detekt configuration
./gradlew :shared:detekt --dry-run

# Check Ktfmt configuration
./gradlew :shared:ktfmtCheck --info
```

## 📚 Best Practices

### 1. **Consistent Formatting**
- Run `ktfmtFormat` before committing
- Use pre-commit hooks for automation
- Configure IDE to format on save

### 2. **Quality Maintenance**
- Address Detekt warnings promptly
- Use custom rules for project-specific needs
- Regular code quality reviews

### 3. **Team Collaboration**
- Share configuration files
- Document custom rules
- Regular quality check reviews

### 4. **Performance**
- Exclude generated files from analysis
- Use parallel processing when possible
- Cache analysis results

## 🔗 Related Documentation

- [Pre-Commit Hooks](PRE_COMMIT_HOOKS.md) - Local automation setup
- [GitHub Actions Workflows](GITHUB_ACTIONS.md) - CI/CD configuration
- [Swift Format & Lint](SWIFT_FORMAT_LINT.md) - iOS code quality tools
- [Code Coverage Reports](CODE_COVERAGE_REPORTS.md) - Testing coverage

## 📖 Resources

- [Ktfmt Documentation](https://github.com/facebook/ktfmt)
- [Detekt Documentation](https://detekt.dev/)
- [Detekt Compose Rules](https://github.com/mrmans0n/compose-rules)
- [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)

---

**Maintain high code quality with automated tools! 🚀**
