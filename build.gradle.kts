@file:OptIn(KotlinNativeCacheApi::class)

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCacheApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "me.devadri.odw"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    macosX64() // Deprecated. Get an Apple Silicon Mac please
    macosArm64()
    linuxArm64()
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.getByName("main") {
            @Suppress("unused")
            cinterops {
                val libcurl by creating {
                    definitionFile.set(project.file("src/nativeInterop/cinterop/libcurl.def"))
                    packageName("libcurl")
                    compilerOpts("-I/usr/include")
                    includeDirs.allHeaders("/usr/include")
                }
            }
        }
        binaries {
            executable {
                entryPoint = "me.devadri.odw.main"

                linkerOpts += listOf("-L/usr/lib", "-lcurl")

                when (buildType) {
                    NativeBuildType.DEBUG -> {
                        freeCompilerArgs += listOf(
                            "-Xbinary=sourceInfoType=libbacktrace",
                        )
                    }
                    NativeBuildType.RELEASE -> {
                        freeCompilerArgs += "-Xruntime-logs=none"
                        linkerOpts += listOf(
                            "--gc-sections",
                            "-s",
                        )
                    }
                }
            }
        }
    }

    sourceSets {
        nativeMain {
            kotlin.srcDirs(layout.buildDirectory.dir("generated/secrets"))
        }
    }
}

val localProps = Properties().apply {
    file("secrets.properties").takeIf { it.exists() }
        ?.inputStream()?.use { load(it) }
}

fun secret(key: String): String =
    System.getenv(key)
        ?: localProps.getProperty(key)
        ?: (project.findProperty(key) as? String)
        ?: error("Missing secret: $key")

val generateConstants by tasks.registering {
    val outDir = layout.buildDirectory.dir("generated/secrets")

    outputs.dir(outDir)

    doLast {
        val dir = outDir.get().asFile.also { it.mkdirs() }
        File(dir, "Constants.kt").writeText(
            """
            package me.devadri.odw

            internal object Constants {
                const val DISCORD_TOKEN       = "${secret("DISCORD_TOKEN")}"
                const val DISCORD_APP_ID      = "${secret("DISCORD_APP_ID")}"
                const val DISCORD_USER_ID     = "${secret("DISCORD_USER_ID")}"
                const val OSU_CLIENT_ID       = "${secret("OSU_CLIENT_ID")}"
                const val OSU_CLIENT_SECRET   = "${secret("OSU_CLIENT_SECRET")}"
                const val OSU_USERNAME        = "${secret("OSU_USERNAME")}"
                const val OSU_MODE            = "${secret("OSU_MODE")}"
            }
            """.trimIndent()
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile> {
    dependsOn(generateConstants)
}