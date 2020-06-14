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

package com.skydoves.pokedex.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.ui.adapter.PokemonAdapter
import com.skydoves.pokedex.ui.main.MainViewModel
import com.skydoves.whatif.whatIfNotNullOrEmpty

@BindingAdapter("adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
  view.adapter = adapter
}

@BindingAdapter("paginationPokemonList")
fun paginationPokemonList(view: RecyclerView, viewModel: MainViewModel) {
  RecyclerViewPaginator(
    recyclerView = view,
    isLoading = { viewModel.isLoading.get() },
    loadMore = { viewModel.fetchPokemonList(it) },
    onLast = { false }
  ).run {
    threshold = 8
  }
}

@BindingAdapter("adapterPokemonList")
fun bindAdapterPokemonList(view: RecyclerView, pokemonList: List<Pokemon>?) {
  pokemonList.whatIfNotNullOrEmpty {
    (view.adapter as? PokemonAdapter)?.addPokemonList(it)
  }
}
