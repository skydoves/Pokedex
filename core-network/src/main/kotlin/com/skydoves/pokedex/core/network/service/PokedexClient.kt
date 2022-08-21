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

package com.skydoves.pokedex.core.network.service

import com.skydoves.pokedex.core.model.PokemonInfo
import com.skydoves.pokedex.core.network.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class PokedexClient @Inject constructor(
  private val pokedexService: PokedexService
) {

  suspend fun fetchPokemonList(
    page: Int
  ): ApiResponse<PokemonResponse> =
    pokedexService.fetchPokemonList(
      limit = PAGING_SIZE,
      offset = page * PAGING_SIZE
    )

  suspend fun fetchPokemonInfo(
    name: String
  ): ApiResponse<PokemonInfo> =
    pokedexService.fetchPokemonInfo(
      name = name
    )

  companion object {
    private const val PAGING_SIZE = 20
  }
}
