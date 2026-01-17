import com.adarshr.gradle.testlogger.theme.ThemeType
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.ncorti.ktfmt.gradle.TrailingCommaManagementStrategy.COMPLETE
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotest)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktfmt.gradle)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.test.logger)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(libs.essenty.lifecycle)
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.decompose)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.decompose.extensions)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.configcat)
            implementation(libs.datastore.preferences)
            implementation(libs.crashkios.crashlytics)
            implementation(libs.essenty.lifecycle.coroutines)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.common)
            implementation(libs.firebase.config)
            implementation(libs.firebase.crashlytics)
            implementation(libs.kermit)
            implementation(libs.kermit.crashlytics)
            implementation(libs.kermit.koin)
            implementation(libs.mvikotlin.coroutines)
            implementation(libs.mvikotlin.core)
            implementation(libs.mvikotlin.logging)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.timetravel)
            implementation(libs.slf4j.nop)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        iosMain.dependencies { implementation(libs.ktor.client.darwin) }

        commonTest.dependencies {
            implementation(libs.kermit.test)
            implementation(libs.koin.test)
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.extensions.koin)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.property)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
        }
    }

    androidLibrary {
        namespace = "com.example.project.shared"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()

        compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }

        androidResources { enable = true }

        packaging.resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/licenses/ASM"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
            pickFirsts += "win32-x86-64/attach_hotspot_windows.dll"
            pickFirsts += "win32-x86/attach_hotspot_windows.dll"
        }
    }
}

dependencies {
    //    implementation(libs.ui.test.junit4.android)
    detektPlugins(libs.detekt.compose)
    //    debugImplementation(libs.ui.tooling)
    //    debugImplementation(libs.ui.test.manifest)
}

compose.resources { packageOfResClass = "com.example.project.resources" }

ktfmt {
    kotlinLangStyle()
    removeUnusedImports = true
    trailingCommaManagementStrategy = COMPLETE
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
}

kover.reports {
    verify.rule { minBound(90) }

    filters.excludes {
        classes("*.*.generated.resources.*")
        classes("*Module*")
        files("*Module*", "*di*", "**/generated/resources/**")
    }
}

testlogger {
    theme = ThemeType.MOCHA
    showFullStackTraces = false
    slowThreshold = 2000
    showSummary = true
    showPassed = true
    showSkipped = true
}

buildkonfig {
    packageName = "com.example.project"

    // DefaultConfig
    defaultConfigs {
        buildConfigField(
            type = BOOLEAN,
            name = "DEBUG",
            value = "true",
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "SUPABASE_KEY",
            value = getSecret("SUPABASE_KEY_DEV"),
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "SUPABASE_URL",
            value = getSecret("SUPABASE_URL_DEV_AND"),
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "CONFIGCAT_KEY",
            value = getSecret("CONFIGCAT_TEST_KEY"),
            nullable = false,
            const = true,
        )
    }

    // Flavored DefaultConfig
    defaultConfigs("release") {
        buildConfigField(
            type = BOOLEAN,
            name = "DEBUG",
            value = "false",
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "SUPABASE_KEY",
            value = getSecret("SUPABASE_KEY_PROD"),
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "SUPABASE_URL",
            value = getSecret("SUPABASE_URL_PROD"),
            nullable = false,
            const = true,
        )

        buildConfigField(
            type = STRING,
            name = "CONFIGCAT_KEY",
            value = getSecret("CONFIGCAT_LIVE_KEY"),
            nullable = false,
            const = true,
        )
    }

    // TargetConfig
    targetConfigs {
        create("iosArm64") {
            buildConfigField(
                type = STRING,
                name = "SUPABASE_URL",
                value = getSecret("SUPABASE_URL_DEV_IOS"),
                nullable = false,
                const = true,
            )

            buildConfigField(
                type = STRING,
                name = "CONFIGCAT_KEY",
                value = getSecret("CONFIGCAT_TEST_KEY"),
                nullable = false,
                const = true,
            )
        }

        create("iosSimulatorArm64") {
            buildConfigField(
                type = STRING,
                name = "SUPABASE_URL",
                value = getSecret("SUPABASE_URL_DEV_IOS"),
                nullable = false,
                const = true,
            )

            buildConfigField(
                type = STRING,
                name = "CONFIGCAT_KEY",
                value = getSecret("CONFIGCAT_TEST_KEY"),
                nullable = false,
                const = true,
            )
        }
    }

    // Flavored TargetConfig
    targetConfigs("release") {
        create("iosArm64") {
            buildConfigField(
                type = STRING,
                name = "SUPABASE_URL",
                value = getSecret("SUPABASE_URL_PROD"),
                nullable = false,
                const = true,
            )

            buildConfigField(
                type = STRING,
                name = "CONFIGCAT_KEY",
                value = getSecret("CONFIGCAT_LIVE_KEY"),
                nullable = false,
                const = true,
            )
        }

        create("iosSimulatorArm64") {
            buildConfigField(
                type = STRING,
                name = "SUPABASE_URL",
                value = getSecret("SUPABASE_URL_PROD"),
                nullable = false,
                const = true,
            )

            buildConfigField(
                type = STRING,
                name = "CONFIGCAT_KEY",
                value = getSecret("CONFIGCAT_LIVE_KEY"),
                nullable = false,
                const = true,
            )
        }
    }
}

fun getSecret(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
        ?: System.getenv(key)
        ?: throw InvalidUserDataException(
            "Missing secret $key in local.properties or environment variables"
        )
}
