import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.29.0"
}

kotlin {
    jvm("desktop")
    androidTarget {
//        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ContextKt"
            isStatic = true
        }
    }

    macosX64()
    macosArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.startup.runtime)
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "io.github.leeway.kmp.context"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.leeway-kmp",
        artifactId = "kmp-context",
        version = "1.0.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Kotlin Multiplatform Context")
        description.set("Library used to ApplicationContext for multiplatform applications.")
        inceptionYear.set("2024")
        url.set("https://github.com/leeway-kmp/kmp-context")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("leeway1307")
                name.set("Lee Way")
                email.set("leeway1307@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/leeway-kmp/kmp-context")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

task("testClasses") {}