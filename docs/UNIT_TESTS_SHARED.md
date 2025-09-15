# Unit Tests in Compose Multiplatform Module

Table of Contents
-----------------

- [Tools](#tools)
- [Kotest Framework](#kotest-framework)
  - [Installing Kotest Dependencies](#installing-kotest-dependencies)
- [Konsist](#konsist)
  - [Add Konsist Dependencies](#add-konsist-dependencies)
  - [Creating Konsist Gradle Module](#creating-konsist-gradle-module)
- [Gradle Test Logger](#gradle-test-logger)
  - [Installing Gradle Test Logger](#installing-gradle-test-logger)
- [Running Tests Locally](#running-tests-locally)
- [Running Tests on CI](#running-tests-on-ci)

## Tools

This are the tools used for testing the multiplatform `composeApp` module:

- Kotest
- Kosist
- Gradle Test Logger

## Kotest Framework

[Kotest Framework](https://kotest.io/) is used for unit tests in the multiplatform composeApp module,
it's also used with the Assertions Library, the Property Testing library, and Data Driven Testing module

### Installing Kotest Dependencies

Kotest dependencies are already configured in the project. The current versions are defined in `gradle/libs.versions.toml`:

```toml
[versions]
kotest = "6.0.3"

[libraries]
kotest-assertions-core = { group = "io.kotest", name = "kotest-assertions-core", version.ref = "kotest" }
kotest-extensions-koin = { group = "io.kotest", name = "kotest-extensions-koin", version.ref = "kotest" }
kotest-framework-engine = { group = "io.kotest", name = "kotest-framework-engine", version.ref = "kotest" }
kotest-property = { group = "io.kotest", name = "kotest-property", version.ref = "kotest" }

[plugins]
kotest = { id = "io.kotest", version.ref = "kotest" }
```

The Kotest plugin is already included in the `composeApp` module's `build.gradle.kts` file:

```kotlin
plugins {
    alias(libs.plugins.kotest)
}
```

The Kotest dependencies are already configured in the `commonTest` source set:

```kotlin
commonTest.dependencies {
    implementation(libs.kotest.assertions.core)
    implementation(libs.kotest.extensions.koin)
    implementation(libs.kotest.framework.engine)
    implementation(libs.kotest.property)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.ktor.client.mock)
    implementation(kotlin("test"))
    implementation(compose.uiTest)
}
```

JUnit is already enabled for Android tests in the `composeApp` module's `build.gradle.kts`:

```kotlin
android {
    testOptions.unitTests.all { it.useJUnitPlatform() }
}
```

## Konsist

[Konsist](https://github.com/LemonAppDev/konsist) is used to ensure certain architectural constrains 
and project structure are consistent across the whole codebase.

Konsist tests are separated in a different module from the rest of the production code, as we think
this is the best approach to consolidate all Konsist tests in a unified location.

Follow this official guide to create a [Dedicated Gradle Module](https://docs.konsist.lemonappdev.com/advanced/isolate-konsist-tests#dedicated-gradle-module) for Kosist tests.

### Add Konsist Dependencies

Add the latest kosist version and dependency to the `libs.versions.toml` file

```toml
[versions]
konsist = "0.16.1"
[libraries]
konsist = { module = "com.lemonappdev:konsist", version.ref = "konsist" }
```

### Creating Konsist Gradle Module

At the root of the project add a new gradle `konsistTest` module. In Android Studio go to `File -> New -> New Module` and 
select `Java or Kotlin Library`, enter your project's package name and in name enter `konsistTest`

Create `konsistTest/src/test/kotlin` directory in the project root, and delete the main source folder `konsistTest/src/main/kotlin` or ``konsistTest/src/main/java`` 
because is not necessary for this module

And in the `build.grandle.kts` file of the `konsistTest` module, add the following dependencies:

```kotlin
dependencies {
    testImplementation(libs.konsist)
    testImplementation(libs.kotest.runner.junit5)
}
```

Then copy all the tests from other projects or add new tests. Change the base package name of your project in the `Scope.kt` file:

```kotlin
internal const val PackageName = "your.project.package"
```

## Gradle Test Logger

To print the tests results in the console, a tool called [Gradle Test Logger Plugin](https://github.com/radarsh/gradle-test-logger-plugin)
logs the tests on the console while running, it also is helpful to debug failing tests in CI.

### Installing Gradle Test Logger

Add the latest version and the gradle plugin in the gradle catalog:

```toml
[versions]
test-logger = "4.0.0"
[plugins]
testLogger = { id = "com.adarshr.test-logger", version.ref = "test-logger" }
```

Add the plugin in the composeApp module `build.gradle.kts` file:

```kotlin
plugins {
    alias(libs.plugins.testLogger)
}
```

At the end of the `build.gradle.kts` file, add configuration options for the logger

> Note: The themes and customisation options are presented working with kotest framework in local console, but in GitHub actions console it prints formatted output.

```kotlin
testlogger {
    theme = ThemeType.MOCHA
    showFullStackTraces = false
    slowThreshold = 2000
    showSummary = true
    showPassed = true
    showSkipped = true
}
```

## Running Tests Locally

There are some defined gradle run configurations stored in the folder `config/.run`. These are:

- `config/.run/unit_tests_shared.run.xml`: Runs all unit tests in composeApp module
- `config/.run/unit_tests_all.run.xml`: Runs all unit tests in composeApp module + all Konsist tests in konsistTest module

> Open these files and edit the package name with your project package name:

```xml
<option value="'your.project.package.*'" />
```

## Running Tests on CI

The unit tests in the multiplatform `composeApp` module runs on CI on every Pull Request push, these 
are define as two jobs in the workflow `.github/workflows/shared_test_lint.yml`

The first step runs the Konsist tests located in the konsistTest module:

```yaml
- name: Konsist Tests
  run: ./gradlew konsistTest:test --rerun-tasks
```

The second step runs all the unit tests inside the `composeApp` module:

```yaml
- name: Run Unit Tests
  run: ./gradlew :composeApp:cleanTestDebugUnitTest :composeApp:testDebugUnitTest --tests 'your.project.package.*'
```