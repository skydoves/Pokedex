plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.kotlin.kapt.get().pluginId)
  id(libs.plugins.ksp.get().pluginId) version libs.versions.ksp.get()
}

android {
  compileSdk = com.skydoves.pokedex.Configuration.compileSdk

  defaultConfig {
    minSdk = com.skydoves.pokedex.Configuration.minSdk
    targetSdk = com.skydoves.pokedex.Configuration.targetSdk
  }
}

dependencies {
  implementation(project(":core-model"))
  testImplementation(project(":core-test"))

  // coroutines
  implementation(libs.coroutines)
  testImplementation(libs.coroutines)
  testImplementation(libs.coroutines.test)

  // network
  implementation(libs.sandwich)
  implementation(libs.retrofit)
  implementation(libs.retrofit.moshi)
  implementation(libs.okhttp.interceptor)
  testImplementation(libs.okhttp.mockserver)
  testImplementation(libs.androidx.arch.core)

  // json parsing
  implementation(libs.moshi)
  ksp(libs.moshi.codegen)

  // di
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}
