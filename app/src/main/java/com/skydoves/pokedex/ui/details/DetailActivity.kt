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

package com.skydoves.pokedex.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.skydoves.pokedex.R
import com.skydoves.pokedex.base.DataBindingActivity
import com.skydoves.pokedex.databinding.ActivityDetailBinding
import com.skydoves.pokedex.extensions.onTransformationEndContainerApplyParams
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : DataBindingActivity() {

  @Inject
  lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

  @VisibleForTesting
  val viewModel: DetailViewModel by viewModels {
    DetailViewModel.provideFactory(
      detailViewModelFactory,
      pokemonItem.name
    )
  }

  private val binding: ActivityDetailBinding by binding(R.layout.activity_detail)

  private val pokemonItem: Pokemon
    get() = requireNotNull(intent.getParcelableExtra(EXTRA_POKEMON))

  override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationEndContainerApplyParams()
    super.onCreate(savedInstanceState)
    binding.apply {
      pokemon = pokemonItem
      lifecycleOwner = this@DetailActivity
      vm = viewModel
    }
  }

  companion object {
    @VisibleForTesting const val EXTRA_POKEMON = "EXTRA_POKEMON"

    fun startActivity(transformationLayout: TransformationLayout, pokemon: Pokemon) {
      val context = transformationLayout.context
      if (context is Activity) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(EXTRA_POKEMON, pokemon)
        TransformationCompat.startActivity(transformationLayout, intent)
      }
    }
  }
}
