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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skydoves.core.data.repository.DetailRepository
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.core.model.PokemonInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
  detailRepository: DetailRepository,
  @Assisted private val pokemon: Pokemon
) : ViewModel() {

  private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState.Loading)
  val uiState: StateFlow<DetailUiState> = _uiState

  init {
    Timber.d("init DetailViewModel")
    detailRepository.fetchPokemonInfo(
      name = pokemon.name,
      onComplete = {},
      onError = { message ->
        _uiState.update {
          DetailUiState.Failure(message!!)
        }
      }
    ).onEach { pokemonInfo ->
      _uiState.update {
        DetailUiState.Success(pokemonInfo, pokemon)
      }
    }.launchIn(viewModelScope)
  }

  @dagger.assisted.AssistedFactory
  interface AssistedFactory {
    fun create(pokemon: Pokemon): DetailViewModel
  }

  companion object {
    fun provideFactory(
      assistedFactory: AssistedFactory,
      pokemon: Pokemon
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(pokemon) as T
      }
    }
  }
}

sealed interface DetailUiState {

  object Loading : DetailUiState

  data class Success(
    val pokemonInfo: PokemonInfo,
    val pokemon: Pokemon
  ) : DetailUiState

  data class Failure(
    val message: String
  ) : DetailUiState
}
