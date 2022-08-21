import com.skydoves.pokedex.Configuration

plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
}

android {
  compileSdk = Configuration.compileSdk

  defaultConfig {
    minSdk = Configuration.minSdk
    targetSdk = Configuration.targetSdk
  }
}

dependencies {
  implementation(project(":core-model"))
  implementation(libs.coroutines)
  implementation(libs.coroutines.test)
  implementation(libs.junit)
}