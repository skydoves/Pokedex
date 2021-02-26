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

package com.skydoves.pokedex.repository

import androidx.annotation.WorkerThread
import com.skydoves.pokedex.mapper.ErrorResponseMapper
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.model.PokemonErrorResponse
import com.skydoves.pokedex.network.PokedexClient
import com.skydoves.pokedex.persistence.PokemonDao
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.map
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepository @Inject constructor(
  private val pokedexClient: PokedexClient,
  private val pokemonDao: PokemonDao
) : Repository {

  @WorkerThread
  fun fetchPokemonList(
    page: Int,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onError: (String?) -> Unit
  ) = flow {
    var pokemons = pokemonDao.getPokemonList(page)
    if (pokemons.isEmpty()) {
      /**
       * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
       * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#suspendonsuccess-suspendonerror-suspendonexception)
       */
      val response = pokedexClient.fetchPokemonList(page = page)
      response.suspendOnSuccess {
        data.whatIfNotNull { response ->
          pokemons = response.results
          pokemons.forEach { pokemon -> pokemon.page = page }
          pokemonDao.insertPokemonList(pokemons)
          emit(pokemonDao.getAllPokemonList(page))
        }
      }
        // handles the case when the API request gets an error response.
        // e.g., internal server error.
        .onError {
          /** maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper. */
          map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
        }
        // handles the case when the API request gets an exception response.
        // e.g., network connection error.
        .onException { onError(message) }
    } else {
      emit(pokemonDao.getAllPokemonList(page))
    }
  }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}
