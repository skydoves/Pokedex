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

package com.skydoves.pokedex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.pokedex.MainCoroutinesRule
import com.skydoves.pokedex.model.PokemonInfo
import com.skydoves.pokedex.network.PokedexClient
import com.skydoves.pokedex.network.PokedexService
import com.skydoves.pokedex.persistence.PokemonInfoDao
import com.skydoves.pokedex.repository.DetailRepository
import com.skydoves.pokedex.ui.details.DetailViewModel
import com.skydoves.pokedex.utils.MockUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

  private lateinit var viewModel: DetailViewModel
  private lateinit var detailRepository: DetailRepository
  private val pokedexService: PokedexService = mock()
  private val pokdexClient: PokedexClient = PokedexClient(pokedexService)
  private val pokemonInfoDao: PokemonInfoDao = mock()

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @Before
  fun setup() {
    detailRepository = DetailRepository(pokdexClient, pokemonInfoDao)
    viewModel = DetailViewModel(detailRepository)
  }

  @Test
  fun fetchPokemonInfoTest() = runBlocking {
    val mockData = MockUtil.mockPokemonInfo()
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "skydoves")).thenReturn(mockData)

    val observer: Observer<PokemonInfo> = mock()
    val fetchedData: LiveData<PokemonInfo> =
      detailRepository.fetchPokemonInfo(
        name = "skydoves",
        onSuccess = {},
        onError = {})
    fetchedData.observeForever(observer)

    viewModel.fetchPokemonInfo(name = "skydoves")

    verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "skydoves")
    verify(observer).onChanged(mockData)
    fetchedData.removeObserver(observer)
  }
}
