import com.skydoves.pokedex.Configuration

///*
// * Designed and developed by 2020 skydoves (Jaewoong Eum)
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
plugins {
  id(libs.plugins.android.application.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.kotlin.kapt.get().pluginId)
  id(libs.plugins.kotlin.parcelize.get().pluginId)
  id(libs.plugins.hilt.plugin.get().pluginId)
  id(libs.plugins.ksp.get().pluginId) version libs.versions.ksp.get()
}

android {
  compileSdk = Configuration.compileSdk
  defaultConfig {
    applicationId = "com.skydoves.pokedex"
    minSdk = Configuration.minSdk
    targetSdk = Configuration.targetSdk
    versionCode = Configuration.versionCode
    versionName = Configuration.versionName
    vectorDrawables.useSupportLibrary = true
    testInstrumentationRunner = "com.skydoves.pokedex.AppTestRunner"
    // The schemas directory contains a schema file for each version of the Room database.
    // This is required to enable Room auto migrations.
    // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildFeatures {
    dataBinding = true
  }

  hilt {
    enableAggregatingTask = true
  }

  kotlin {
    sourceSets.configureEach {
      kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
    }
  }

  sourceSets.getByName("androidTest") {
    kotlin.srcDir("src/test-common/kotlin")
  }

  sourceSets.getByName("test") {
    kotlin.srcDir("src/test-common/kotlin")
    assets.srcDir(files("$projectDir/schemas"))
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

dependencies {
  // android supports
  implementation(libs.material)

  // architecture components
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.lifecycle)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  ksp(libs.androidx.room.compiler)
  testImplementation(libs.androidx.arch.core)

  // binding
  implementation(libs.bindables)

  // startup
  implementation(libs.androidx.startup)

  // hilt
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  androidTestImplementation(libs.hilt.testing)
  kaptAndroidTest(libs.hilt.compiler)

  // network
  implementation(libs.sandwich)
  implementation(libs.retrofit)
  implementation(libs.retrofit.moshi)
  implementation(libs.okhttp.interceptor)
  testImplementation(libs.okhttp.mockserver)

  // moshi
  implementation(libs.moshi)
  ksp(libs.moshi.codegen)

  // coroutines
  implementation(libs.coroutines)
  testImplementation(libs.coroutines)
  testImplementation(libs.coroutines.test)

  // whatIf
  implementation(libs.whatif)

  // glide
  implementation(libs.glide)
  implementation(libs.glide.palette)

  // bundler
  implementation(libs.bundler)

  // transformation
  implementation(libs.transformationLayout)

  // recyclerView
  implementation(libs.recyclerview)
  implementation(libs.baseAdapter)

  // gradation
  implementation(libs.rainbow)

  // custom views
  implementation(libs.androidRibbon)
  implementation(libs.progressView)

  // logging
  implementation(libs.timber)

  // unit test
  testImplementation(libs.junit)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.mockito.inline)
  testImplementation(libs.turbine)
  testImplementation(libs.robolectric)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso)
  androidTestImplementation(libs.android.test.runner)
}
