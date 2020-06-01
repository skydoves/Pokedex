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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.pokedex.R
import com.skydoves.pokedex.databinding.ItemPokemonBinding
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.ui.details.DetailActivity

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

  private val items: MutableList<Pokemon> = mutableListOf()
  private var onClickedTime = System.currentTimeMillis()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding =
      DataBindingUtil.inflate<ItemPokemonBinding>(inflater, R.layout.item_pokemon, parent, false)
    return PokemonViewHolder(binding)
  }

  fun addPokemonList(pokemonList: List<Pokemon>) {
    items.addAll(pokemonList)
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
    val item = items[position]
    holder.binding.apply {
      pokemon = item
      executePendingBindings()
      root.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - onClickedTime > 550) {
          onClickedTime = currentTime
          DetailActivity.startActivity(transformationLayout, item)
        }
      }
    }
  }

  override fun getItemCount() = items.size

  class PokemonViewHolder(val binding: ItemPokemonBinding) :
    RecyclerView.ViewHolder(binding.root)
}
