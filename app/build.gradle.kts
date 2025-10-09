/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask
import com.skydoves.pokedex.Configuration
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.hilt.plugin)
}

android {
  namespace = "com.skydoves.pokedex"

  defaultConfig {
    applicationId = "com.skydoves.pokedex"
    versionCode = Configuration.versionCode
    versionName = Configuration.versionName
    testInstrumentationRunner = "com.skydoves.pokedex.AppTestRunner"
  }

  buildFeatures {
    dataBinding = true
    buildConfig = true
  }

  hilt {
    enableAggregatingTask = true
  }

  kotlin {
    sourceSets.configureEach {
      kotlin.srcDir(layout.buildDirectory.files("generated/ksp/$name/kotlin/"))
    }
    sourceSets.all {
      languageSettings {
        languageVersion = "2.0"
      }
    }
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
      isReturnDefaultValues = true
    }
  }

  buildTypes {
    create("benchmark") {
      isDebuggable = true
      signingConfig = getByName("debug").signingConfig
      matchingFallbacks += listOf("release")
    }
  }
}

androidComponents {
  onVariants(selector().all()) { variant ->
    afterEvaluate {
      val dataBindingTask =
        project.tasks.findByName("dataBindingGenBaseClasses" + variant.name.replaceFirstChar { it.uppercase() }) as? DataBindingGenBaseClassesTask
      if (dataBindingTask != null) {
        val kspTaskName = "ksp" + variant.name.replaceFirstChar { it.uppercase() } + "Kotlin"
        val kspTask = project.tasks.findByName(kspTaskName)
        if (kspTask is AbstractKotlinCompileTool<*>) {
          kspTask.setSource(dataBindingTask.sourceOutFolder)
        }
      }
    }
  }
}

dependencies {
  // modules
  implementation(projects.coreData)

  // modules for unit test
  testImplementation(projects.coreNetwork)
  testImplementation(projects.coreDatabase)
  testImplementation(projects.coreTest)
  androidTestImplementation(projects.coreTest)

  // androidx
  implementation(libs.material)
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.lifecycle)
  implementation(libs.androidx.startup)
  implementation(libs.androidx.palette)

  // data binding
  implementation(libs.bindables)

  // di
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)
  androidTestImplementation(libs.hilt.testing)
  kspAndroidTest(libs.hilt.compiler)

  // coroutines
  implementation(libs.coroutines)

  // whatIf
  implementation(libs.whatif)

  // image loading
  implementation(libs.glide)

  // bundler
  implementation(libs.bundler)

  // transformation animation
  implementation(libs.transformationLayout)

  // recyclerView
  implementation(libs.recyclerview)
  implementation(libs.baseAdapter)

  // custom views
  implementation(libs.rainbow)
  implementation(libs.androidRibbon)
  implementation(libs.progressView)

  // unit test
  testImplementation(libs.junit)
  testImplementation(libs.turbine)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.coroutines.test)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso)
  androidTestImplementation(libs.android.test.runner)
}
