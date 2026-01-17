import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.google.services)
}

kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_21) } }

android {
    namespace = "com.example.project"
    compileSdk = libs.versions.android.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = libs.versions.android.min.sdk.get().toInt()
        targetSdk = libs.versions.android.target.sdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging.resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
        excludes += "/META-INF/licenses/ASM"
        excludes += "/META-INF/LICENSE.md"
        excludes += "/META-INF/LICENSE-notice.md"
        pickFirsts += "win32-x86-64/attach_hotspot_windows.dll"
        pickFirsts += "win32-x86/attach_hotspot_windows.dll"
    }

    buildTypes {
        getByName("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
            testProguardFiles("proguard-test-rules.pro")
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            ndk.debugSymbolLevel = "FULL"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions.unitTests.all { it.useJUnitPlatform() }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.splashscreen)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
