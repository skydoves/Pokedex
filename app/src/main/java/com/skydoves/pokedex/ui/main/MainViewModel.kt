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

package com.skydoves.pokedex.ui.main

import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.bindables.bindingProperty
import com.skydoves.pokedex.base.LiveCoroutinesViewModel
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val mainRepository: MainRepository,
  private val savedStateHandle: SavedStateHandle
) : LiveCoroutinesViewModel() {

  private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
  val pokemonListLiveData: LiveData<List<Pokemon>>

  @get:Bindable
  var toastMessage: String? by bindingProperty(null)
    private set

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set

  init {
    Timber.d("init MainViewModel")

    pokemonListLiveData = pokemonFetchingIndex.asLiveData().switchMap { page ->
      mainRepository.fetchPokemonList(
        page = page,
        onStart = { isLoading = true },
        onSuccess = { isLoading = false },
        onError = { toastMessage = it }
      ).asLiveDataOnViewModelScope()
    }
  }

  @MainThread
  fun fetchNextPokemonList() {
    if (!isLoading) {
      pokemonFetchingIndex.value++
    }
  }
}
