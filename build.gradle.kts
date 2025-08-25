///*
// * Designed and developed by 2022 skydoves (Jaewoong Eum)
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.test) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.kapt) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.hilt.plugin) apply false
  alias(libs.plugins.spotless)
}

private typealias AndroidExtension = com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>

private val Project.androidExtension: AndroidExtension
    get() = extensions.getByType(com.android.build.api.dsl.CommonExtension::class.java)

private fun Project.android(block: AndroidExtension.() -> Unit) {
  plugins.withType<com.android.build.gradle.BasePlugin>().configureEach {
    androidExtension.block()
  }
}

private val targetSdkVersion = libs.versions.targetSdk.get().toInt()
private val bytecodeVersion = JavaVersion.toVersion(libs.versions.jvmBytecode.get())

subprojects {
  apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

  // Common Android configurations
  android {
    defaultConfig {
      vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
      sourceCompatibility = bytecodeVersion
      targetCompatibility = bytecodeVersion
    }

    lint {
      abortOnError = false
    }
  }

  // Configurations for `com.android.application` plugin
  plugins.withType<com.android.build.gradle.AppPlugin>().configureEach {
    extensions.configure<com.android.build.api.dsl.ApplicationExtension> {
      defaultConfig {
        targetSdk = targetSdkVersion
      }
    }
  }

  // Configurations for `com.android.test` plugin
  plugins.withType<com.android.build.gradle.TestPlugin>().configureEach {
    extensions.configure<com.android.build.api.dsl.TestExtension> {
      defaultConfig {
        targetSdk = targetSdkVersion
      }
    }
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.fromTarget(bytecodeVersion.toString()))
      freeCompilerArgs.addAll(
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlin.time.ExperimentalTime",
      )
    }
  }

  extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    val buildDirectory = layout.buildDirectory.asFileTree
    kotlin {
      target("**/*.kt")
      targetExclude(buildDirectory)
      ktlint().editorConfigOverride(
        mapOf(
          "indent_size" to "2",
          "continuation_indent_size" to "2"
        )
      )
      licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"))
      trimTrailingWhitespace()
      endWithNewline()
    }
    format("kts") {
      target("**/*.kts")
      targetExclude(buildDirectory)
      licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"), "(^(?![\\/ ]\\*).*$)")
    }
    format("xml") {
      target("**/*.xml")
      targetExclude(buildDirectory)
      licenseHeaderFile(rootProject.file("spotless/spotless.license.xml"), "(<[^!?])")
    }
  }
}
