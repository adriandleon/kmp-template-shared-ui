# Code Coverage Reports

Table of Contents
-----------------

- [Code Coverage in Shared module](#code-coverage-in-shared-module)
  - [Failing Coverage Threshold](#failing-coverage-threshold)
  - [Generating local reports](#generating-local-reports)
  - [Generating CI coverage reports](#generating-ci-coverage-reports)
  - [Codecov Configuration file](#codecov-configuration-file)
- [Code Coverage in Android module](#code-coverage-in-android-module)
- [Code Coverage in iOS module](#code-coverage-in-ios-module)

## Code Coverage in Shared module

For the shared module, we use the [kotlinx-kover](https://github.com/Kotlin/kotlinx-kover) plugin to generate code coverage reports. 
We add the following block in the root project's `build.gradle.kts` file.

```kotlin
plugins {
    alias(libs.plugins.kover) apply false
}
```

And to integrate the Kover plugin into the shared module, we add the following block in the shared module's `build.gradle.kts` file.

```kotlin 
plugins {
    alias(libs.plugins.kover)
}
```

### Failing Coverage Threshold

We set the failing coverage threshold to 90% of lines covered in the shared module's `build.gradle.kts` file.

```kotlin
kover.reports.verify.rule {
    minBound(90)
}
```

To verify the coverage threshold, run the following command:

```shell
./gradlew koverVerify
```

### Excluding files and classes from coverage

To exclude non-testable code from the coverage report (ex. dependency injection setup files, auto-generated classes)
this block should be added in `build.gradle.kts` file. 

```kotlin
kover.filters.excludes {
  classes("uy.jorgemarquez.misionvida.common.util.AndroidDispatcher")
  files("*.*Module.*", "*.di.*Module*", "*.di.*")
}
```

### Generating local reports

To generate the code coverage report in HTML format, run the following command:

```shell
./gradlew koverHtmlReport
```

The report is generated in the file `shared/build/reports/kover/html/index.html` which you can open in any web browser.

To generate the code coverage report in XML format, run the following command:

```shell
./gradlew koverXmlReport
```

The report is generated in the file `shared/build/reports/kover/report.xml` which you can import in Android Studio/IntelliJ coverage tool tab.

### Generating CI coverage reports

To generate the code coverage report in CI (GitHub Actions), we include the following action step in 
the file `.github/workflows/shared_test_lint.yml` in job `tests` for Pull Request coverage reports, 
and in job `coverage_report` in the file `.github/workflows/android_deploy.yml` for Merge Pull Request 
coverage reports in main branch:

```yaml
- name: Check coverage metrics
  run: ./gradlew koverVerify koverXmlReport
```

After the reports are generated, we include the following action step in the file `.github/workflows/shared_test_lint.yml` 
in job `tests`, and also in job `coverage_report` in the file `.github/workflows/android_deploy.yml` for Merge Pull Request
coverage reports in main branch:

```yaml
- name: Upload coverage reports
  uses: codecov/codecov-action@v5
  with:
    token: ${{ secrets.CODECOV_TOKEN }}
    files: shared/build/reports/kover/report.xml
    disable_search: true
    fail_ci_if_error: true
```

This step uploads the coverage report to [Codecov](https://about.codecov.io/) where you can see the coverage reports.
It is important to set the `CODECOV_TOKEN` secret in the repository settings in GitHub.

### Codecov Configuration file

Codecov uses a YAML-style configuration file, it is located in `.github/codecov.yml`

Check the official [documentation](https://docs.codecov.com/docs/codecov-yaml) for more information.

## Code Coverage in Android module

TODO

## Code Coverage in iOS module

TODO