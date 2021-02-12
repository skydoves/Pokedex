/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
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

package com.skydoves.pokedex.initializer

import android.content.Context
import androidx.startup.Initializer
import com.skydoves.bindables.BindingManager
import com.skydoves.pokedex.BR
import com.skydoves.pokedex.BuildConfig
import timber.log.Timber

class BindingInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    if (BuildConfig.DEBUG) {
      BindingManager.bind(BR::class)
      Timber.d("BindingInitializer is initialized.")
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = listOf(TimberInitializer::class.java)
}
