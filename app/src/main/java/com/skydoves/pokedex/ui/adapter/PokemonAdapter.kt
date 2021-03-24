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

package com.skydoves.pokedex.ui.adapter

import android.os.SystemClock
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding
import com.skydoves.pokedex.R
import com.skydoves.pokedex.databinding.ItemPokemonBinding
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.ui.details.DetailActivity

class PokemonAdapter : BindingListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(diffUtil) {

  private var onClickedAt = 0L

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
    val binding = parent.binding<ItemPokemonBinding>(R.layout.item_pokemon)
    return PokemonViewHolder(binding).apply {
      binding.root.setOnClickListener {
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
          ?: return@setOnClickListener
        val currentClickedAt = SystemClock.elapsedRealtime()
        if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
          DetailActivity.startActivity(binding.transformationLayout, getItem(position))
          onClickedAt = currentClickedAt
        }
      }
    }
  }

  override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
    holder.binding.apply {
      pokemon = getItem(position)
      executePendingBindings()
    }
  }

  class PokemonViewHolder(val binding: ItemPokemonBinding) :
    RecyclerView.ViewHolder(binding.root)

  companion object {
    private val diffUtil = object : DiffUtil.ItemCallback<Pokemon>() {

      override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem.name == newItem.name

      override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem == newItem
    }
  }
}
