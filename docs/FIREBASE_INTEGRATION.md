# Firebase Integration

Table of Contents
-----------------

- [Used Products](#used-products)
- [Installing Firebase](#installing-firebase)
  - [Create Firebase project](#create-firebase-project)
  - [Add configuration for Android](#add-configuration-for-android)
  - [Add configuration for iOS](#add-configuration-for-ios)
  - [Setting up the Firebase SDK](#setting-up-the-firebase-sdk)
  - [Testing the implementation](#testing-the-implementation)
- [Authorize Android App with Firebase in CI](#authorize-android-app-with-firebase-in-ci)
- [Authorize iOS App with Firebase in CI](#authorize-ios-app-with-firebase-in-ci)

## Used Products

[Firebase](https://firebase.google.com/) is a cloud backend-as-a-service platform that offers a suite of tools and services. The following are the services used in this project: 

- [Crashlytics](https://firebase.google.com/docs/crashlytics) is used for crash reporting in Android, iOS and ComposeApp module 
- [Test Lab](https://firebase.google.com/docs/test-lab) is used for running Android App UI and integration tests before release

## Installing Firebase

The integration with Firebase is made through the open-source project [firebase-kotlin-sdk](https://github.com/GitLiveApp/firebase-kotlin-sdk)

### Create Firebase project

1) Navigate to the [Firebase console](https://console.firebase.google.com/u/0/) and click `Add project`
2) Choose a name for your Firebase project and click `Continue`.
3) In the following step, you'll be prompted to enable analytics. This is optional; you can choose based on your preference.
4) Wait until the project is created.

### Add configuration for Android

Now, add the configuration parameters for your Android app.
1) After clicking on the Android icon, you'll be taken to a registration screen for your app. Ensure that you provide the correct package name and then click `Register`.
2) As a naming convention, add the suffix `Android` to your project nickname for clarity. This helps distinguish between different projects
3) On the next step, make sure to download the generated `google-services.json` file.
4) Proceed to add it to your `composeApp` module. Eg: `composeApp/google-services.json`.
5) Make sure to not commit the `google-services.json` file to git.

### Add configuration for iOS

1) The process for iOS configuration is similar to Android. Begin by adding your Apple target.
2) As a naming convention, add the suffix `iOS` to your project nickname for clarity. This helps distinguish between different projects
3) Ensure that your `BUNDLE_ID`, which can be found in your `iosApp/Configuration/Config.xconfig`, is correctly added in the configuration file.
4) In the next step, download the generated `GoogleService-Info.plist` file.
5) Copy the `GoogleService-Info.plist` to the iosApp folder. Eg: `iosApp/AppTemplate/GoogleService-Info.plist`.
6) Make sure to not commit the `GoogleService-Info.plist` file to git.
7) In Xcode, navigate to File > Add package dependencies.
8) Make sure to add the Firebase SPM dependency from [https://github.com/firebase/firebase-ios-sdk](https://github.com/firebase/firebase-ios-sdk)
9) Choose the Firebase components you want to use from the repository. In this example, we'll use analytics and crashlytics. Then, click `Add Packages`
10) Due to an [issue](https://github.com/JetBrains/compose-multiplatform/issues/4026) where the Frameworks, Libraries, and Embedded Content section is missing when creating a KMP project, you may need to add it manually from the Build phases tab in Xcode and restart Xcode a few times.
11) Go to `Targets > AppTemplate > Build Phases` tab and ensure to add all necessary components from Firebase that you'll be using in your project in the `Link Binary With Libraries` section.

### Setting up the Firebase SDK

The Firebase dependencies are already configured in the project. The current versions are defined in `gradle/libs.versions.toml`:

```toml
[versions]
firebase-gitlive = "2.3.0"
firebase-bom = "34.2.0"
gradle-crashlytics = "3.0.6"
gradle-google-services = "4.4.3"

[libraries]
firebase-analytics = { group = "dev.gitlive", name = "firebase-analytics", version.ref = "firebase-gitlive" }
firebase-common = { group = "dev.gitlive", name = "firebase-common", version.ref = "firebase-gitlive" }
firebase-config = { group = "dev.gitlive", name = "firebase-config", version.ref = "firebase-gitlive" }
firebase-crashlytics = { group = "dev.gitlive", name = "firebase-crashlytics", version.ref = "firebase-gitlive" }

[plugins]
crashlytics = { id = "com.google.firebase.crashlytics", version.ref = "gradle-crashlytics" }
google-services = { id = "com.google.gms.google-services", version.ref = "gradle-google-services" }
```

The Firebase plugins are already configured in the project. In the root `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false    
}
```

In the `composeApp` module's `build.gradle.kts`:

```kotlin
plugins { 
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}
```

The Firebase dependencies are already included in the `commonMain` source set of the `composeApp` module's `build.gradle.kts` file:

```kotlin
commonMain.dependencies {
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.common)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
}
```

### Testing the implementation

Once your setup is complete, you can run the Android app to verify the implementation.
If everything is correct you should see a message in the Logcat similar to `FirebaseApp initialization successful`

On the iOS side, you can configure the collection settings as follows. Start by adding the initialization point to your `MainApplication.swift`:

```swift
import Shared
import SwiftUI

@main
struct MainApplication: App {

    init() {
        FirebaseHelperKt.startCrashKiOS()
        KoinAppKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
```

Then, within your `:shared` module inside the `iosMain` target, add the following code 
in the `FirebaseHelper.kt` file, and you can define your Kotlin logic to enable or disable collection in debug mode:

```kotlin
@Suppress("unused")
fun startCrashKiOS() {
    if (BuildKonfig.DEBUG.not()) {
        Firebase.initialize()    
        setCrashlyticsUnhandledExceptionHook()
    }
}
```

When you run the app through Xcode, you'll notice that Firebase has been successfully initialized 
checking the log messages in the Xcode debug area.

> Reference: https://github.com/FunkyMuse/funkymuse.github.io/blob/main/_posts/2024-04-18-kmp-firebase.md

## Authorize Android App with Firebase in CI

- Encode the content of the file `google-services.json` to base64 running the following command in the terminal:

```shell
base64 -i composeApp/google-services.json
```

- Log in to the GitHub repository of the project and [create secrets](https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions#creating-secrets-for-a-repository) for Github Actions.
- Create a secret with the name `GOOGLE_SERVICES_JSON` and paste the content of the file encoded in base64 format
- Add the following step after `checkout` in each workflow which runs `gradle` commands:

```yaml
env:
  GOOGLE_SERVICES_JSON: ${{ secrets.GOOGLE_SERVICES_JSON }}

jobs:
  build-android-app:
    steps:
      - name: Load Google Service JSON file
        run: echo $GOOGLE_SERVICES_JSON | base64 -di > composeApp/google-services.json
```

## Authorize iOS App with Firebase in CI

- Encode the content of the file `GoogleService-Info.plist` to base64 running the following command in the terminal:

```shell
base64 -i iosApp/AppTemplate/GoogleService-Info.plist
```

- Log in to the GitHub repository of the project and [create secrets](https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions#creating-secrets-for-a-repository) for Github Actions.
- Create a secret with the name `GOOGLE_SERVICES_PLIST` and paste the content of the file encoded in base64 format
- Add the following step after `checkout` in each workflow which needs to build the iOS app: 

```yaml
env:
  GOOGLE_SERVICES_PLIST: ${{ secrets.GOOGLE_SERVICES_PLIST }}

jobs:
  build-android-app:
    steps:
      - name: Load Google Service PLIST file
        run: echo $GOOGLE_SERVICES_PLIST | base64 -d > iosApp/AppTemplate/GoogleService-Info.plist
```
