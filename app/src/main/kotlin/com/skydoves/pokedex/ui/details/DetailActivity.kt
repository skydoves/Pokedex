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

package com.skydoves.pokedex.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.skydoves.bindables.BindingActivity
import com.skydoves.bundler.bundleNonNull
import com.skydoves.bundler.intentOf
import com.skydoves.pokedex.R
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.databinding.ActivityDetailBinding
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationCompat.onTransformationEndContainerApplyParams
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : BindingActivity<ActivityDetailBinding>(R.layout.activity_detail) {

  @Inject
  internal lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

  @get:VisibleForTesting
  internal val viewModel: DetailViewModel by viewModels {
    DetailViewModel.provideFactory(detailViewModelFactory, pokemon.name)
  }
  private val pokemon: Pokemon by bundleNonNull(EXTRA_POKEMON)
  override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationEndContainerApplyParams(this)
    super.onCreate(savedInstanceState)
    // I create a temporary Pokemon variable 'pokemonBis' for modifying the name before binding.
    // I needed for that to change the data class Pokemon here Pokemon.kt
    var pokemonBis = pokemon
    // I do a replace for the name only
    pokemonBis.name = pokemon.name.replace("-", " ")
    // And i change set the value of binding.pokemon
    binding.pokemon = pokemonBis
    binding.vm = viewModel
  }

  companion object {
    @VisibleForTesting
    internal const val EXTRA_POKEMON = "EXTRA_POKEMON"

    fun startActivity(transformationLayout: TransformationLayout, pokemon: Pokemon) =
      transformationLayout.context.intentOf<DetailActivity> {
        putExtra(EXTRA_POKEMON to pokemon)
        TransformationCompat.startActivity(transformationLayout, intent)
      }
  }
}
