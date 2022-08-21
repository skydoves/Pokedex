import com.skydoves.pokedex.Configuration

plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.kotlin.parcelize.get().pluginId)
  id(libs.plugins.ksp.get().pluginId) version libs.versions.ksp.get()
}

android {
  compileSdk = Configuration.compileSdk

  defaultConfig {
    minSdk = Configuration.minSdk
    targetSdk = Configuration.targetSdk
  }
}

dependencies {
  api(libs.moshi)
  ksp(libs.moshi.codegen)
}