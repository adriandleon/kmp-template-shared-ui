# Logging Multiplatform

Table of Contents
-----------------

- [Installing Kermit](#installing-kermit)
- [Logging Messages](#logging-messages)
- [Crashlytics Integration](#crashlytics-integration)
- [Testing Logger](#testing-logger)

[Kermit](https://kermit.touchlab.co/) is Kotlin Multiplatform Logging library, which allows 
Shared-code logging with platform-specific log writers. Also allows crash reporting with integration
with Firebase Crashlytics service.

## Installing Kermit

Define the following versions in the `libs.versions.toml` catalog file:

```toml
[versions]
kermit = "2.0.5"
[libraries]
kermit = { group = "co.touchlab", name = "kermit", version.ref = "kermit" }
kermit-crashlytics = { group = "co.touchlab", name = "kermit-crashlytics", version.ref = "kermit" }
kermit-koin = { group = "co.touchlab", name = "kermit-koin", version.ref = "kermit" }
kermit-test = { group = "co.touchlab", name = "kermit-test", version.ref = "kermit" }
```

Add the dependencies in the `commonMain` source set of the `:shared` module's `build.gradle.kts` file:

```kotlin
commonMain.dependencies { 
    implementation(libs.kermit)
    implementation(libs.kermit.koin)
    implementation(libs.kermit.crashlytics)
}
```

And create an instance of KermitLogger which you can use Koin to inject 

```kotlin
private fun kermitLogger(): KermitLogger {
    val logWriter =
        if (BuildKonfig.DEBUG) {
            platformLogWriter()
        } else {
            CrashlyticsLogWriter(minSeverity = Severity.Verbose)
        }

    KermitLogger.setLogWriters(logWriter)
    KermitLogger.setTag("ComposeApp")
    return KermitLogger
}
```

In the code above, the platform log writers (eg: LogCat in Android) are used in debug mode, but when 
the application is not in debug, the Crashlytics log writer is used.

## Logging Messages

Inject the Logger interface in your class, Koin will provide an instance of KermitLoggerImpl

```kotlin
internal class ExampleClass(
    private val logger: Logger // Use constructor injection
)
```

- Log a debug message 

```kotlin
logger.debug { "Message with debug severity" }
```

- Log an error message:

```kotlin
logger.error { "Message with error severity" }
```

- Log a debug message with Throwable exception:

```kotlin
logger.debug(throwable) { "Debug message with Throwable" }
```

- Log an error message with Throwable exception:

```kotlin
logger.error(throwable) { "Error message with Throwable" }
```

## Crashlytics Integration

Kermit has an extension to integrate with Crashlytics and uses [CrashKiOS](https://crashkios.touchlab.co/) 
for iOS symbolicated crash reporting

Adding custom values for Crashlytics:

```kotlin
CrashlyticsKotlin.setCustomValue("someKey", "someValue")
```

## Testing Logger

`Logger` is just an interface, but to test the logs messages that are actually sent, we use a Kermit 
extension. Add the dependency in the `:shared` build gradle file:

```kotlin
commonTest.dependencies {
    implementation(libs.kermit.test)
}
```

Create your object under test using the `testLogger` as a constructor param:

```kotlin
val sut = ExampleComponent(logger = testLogger)
```

Then execute your tests when a call to log a message is executed, and finally assert the message 
logged using `lastLogEntry.message`:

```kotlin
lastLogEntry.message shouldBe "Your expected logged message"
```
