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

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.skydoves.pokedex.base.LiveCoroutinesViewModel
import com.skydoves.pokedex.model.PokemonInfo
import com.skydoves.pokedex.repository.DetailRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
  private val detailRepository: DetailRepository,
  @Assisted private val pokemonName: String
) : LiveCoroutinesViewModel() {

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val toastLiveData: MutableLiveData<String> = MutableLiveData()
  val pokemonInfoLiveData: LiveData<PokemonInfo?> = launchOnViewModelScope(
    block = {
      isLoading.set(true)
      detailRepository.fetchPokemonInfo(
        name = pokemonName,
        onSuccess = { isLoading.set(false) },
        onError = { toastLiveData.postValue(it) }
      ).asLiveData()
    }
  )

  init {
    Timber.d("init DetailViewModel")
  }

  @AssistedInject.Factory
  interface AssistedFactory {
    fun create(pokemonName: String): DetailViewModel
  }

  companion object {
    fun provideFactory(
      assistedFactory: AssistedFactory,
      pokemonName: String
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(pokemonName) as T
      }
    }
  }
}
