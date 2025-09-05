# Kotlin Lint & Format

Table of Contents
-----------------

- [Formatting](#formatting)
  - [Ktfmt Installation](#ktfmt-installation)
  - [Ktfmt-gradle plugin installation](#ktfmt-gradle-plugin-installation)
  - [Ktfmt Configuration Options](#ktfmt-configuration-option)
  - [Running ktfmtFormat](#running-ktfmtformat)
- [Static code analysis](#static-code-analysis)
  - [Detekt Installation](#detekt-installation)
  - [Detekt Gradle Plugin Installation](#detekt-gradle-plugin-installation)
  - [Running Detekt with Compose Rules](#running-detekt-with-compose-rules)

## Formatting

### Ktfmt Installation

Install [ktfmt](https://github.com/facebook/ktfmt#installation) in your local machine using homebrew

```shell
brew install ktfmt
```

### Ktfmt-gradle plugin installation

We use the [ktfmt-gradle](https://github.com/cortinico/ktfmt-gradle) gradle plugin to integrate with the build system. And we add the plugin to each module's `build.gradle.kts` file containing kotlin code.

```kotlin
plugins {
    alias(libs.plugins.ktfmtGradle)
}
```

### Ktfmt Configuration Option

To configure options for `ktfmt` we add a block in every module's `build.gradle.kts` file containing ktfmt-gradle plugin.

```kotlin
ktfmt {
    kotlinLangStyle()
    removeUnusedImports = true
    manageTrailingCommas = true
}
```

### Running ktfmtFormat

- Runs locally manually when you want to format the code

After you've applied the plugin and synced your project with Gradle plugins you can run the ktfmtFormat Gradle task. That can be done from:

- The Gradle tool window:
1. Open the Gradle tool window
2. Navigate to Tasks/formatting
3. Double click on the ktfmtFormat task (or right-click and click on Run)

- In the terminal:
1. Open the Terminal window inside Android Studio
2. Run the following command: `./gradlew ktfmtFormat`

- Runs locally automatically on every commit

To run the ktfmtFormat task automatically on every commit, you need to set up the pre-commit hooks.
Make sure to install the [pre-commit hooks](#pre-commit-hooks). This will install the pre-commit hooks in your .git/hooks folder. The pre-commit hooks are in `.pre-commit-config.yaml` and contains the following entry for Ktfmt:

```yaml
- repo: https://github.com/jguttman94/pre-commit-gradle
  rev: v0.3.0
  hooks:
  - id: gradle-task
    name: Ktfmt
    args: [--wrapper, ktfmtFormat]
```

- Runs on CI on every Pull Request

In the workflow file [.github/workflows/shared_test_lint.yml](https://github.com/adriandleon/MisionVida/blob/main/.github/workflows/shared_test_lint.yml) there is a job `swift-check` with a step named `Check Kotlin Formatting` that runs ktfmtCheck on every Pull Request.

```yaml
- name: Check Kotlin Formatting
  run: ./gradlew ktfmtCheck
```

## Static code analysis

[detekt](https://github.com/detekt/detekt) is a static code smell analysis tool for Kotlin

### Detekt Installation

Install [detekt](Install [ktfmt](https://github.com/facebook/ktfmt#installation) in your local machine using homebrew) in your local machine using homebrew

```shell
brew install detekt
```

### Detekt Gradle Plugin Installation

We use the [detekt-gradle](https://github.com/cortinico/ktfmt-gradle) gradle plugin to integrate with the build system. And we add the plugin to each module's `build.gradle.kts` file containing kotlin code.) gradle plugin to integrate with the build system.

Add the plugin to root `build.gradle.kts` file.

```kotlin
plugins { 
    alias(libs.plugins.detekt) apply false
}
```

In the same root `build.gradle.kts` file, register the gradle task `detektAll` for all the subprojects.

```kotlin
subprojects {
    tasks.register("detektAll") {
        allprojects { this@register.dependsOn(tasks.withType<Detekt>()) }
    }
}
```

Then, add the detekt [compose-rules](https://github.com/mrmans0n/compose-rules) plugin in each module containing Kotlin Compose code and configure the options in the detekt block:

```kotlin
plugins {
  alias(libs.plugins.detekt)
}

dependencies {
    detektPlugins(libs.detekt.compose)
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
}
```
> **Important:** Set the correct path for the config file detekt.yml.

And in Android projects with Compose:

```kotlin
plugins {
  alias(libs.plugins.detekt)
}

android {
    dependencies { 
        detektPlugins(libs.detekt.compose)
    }

    detekt {
        parallel = true
        buildUponDefaultConfig = true
        config.setFrom("$rootDir/config/detekt.yml")
    }
}
```

### Running Detekt with Compose Rules

- Runs locally manually when you want to analysis your code

After you've applied the plugin and synced your project with Gradle plugins you can run the detekt Gradle task. That can be done from:

```shell
./gradlew detektAll
```

- Runs locally automatically on every commit

To run the detekt task automatically on every commit, you need to set up the pre-commit hooks.
Make sure to install the [pre-commit hooks](#pre-commit-hooks). This will install the pre-commit hooks in your .git/hooks folder. The pre-commit hooks are in `.pre-commit-config.yaml` and contains the following entry for detekt:

```yaml
- repo: https://github.com/jguttman94/pre-commit-gradle
  rev: v0.3.0
  hooks:
  - id: gradle-task
    name: Detekt
    args: [--wrapper, detekt]
```

- Runs on CI on every Pull Request

In the workflow file [.github/workflows/shared_test_lint.yml](https://github.com/adriandleon/MisionVida/blob/main/.github/workflows/shared_test_lint.yml) there is a job `kotlin-check` with a step named `Detekt Checks` that runs detektAll on every Pull Request.

```yaml
- name: Detekt Checks
  run: ./gradlew detektAll
```
