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

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.pokedex.MainCoroutinesRule
import com.skydoves.pokedex.network.PokedexClient
import com.skydoves.pokedex.network.PokedexService
import com.skydoves.pokedex.persistence.PokemonDao
import com.skydoves.pokedex.repository.MainRepository
import com.skydoves.pokedex.ui.main.MainViewModel
import com.skydoves.pokedex.utils.MockUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.seconds

class MainViewModelTest {

  private lateinit var viewModel: MainViewModel
  private lateinit var mainRepository: MainRepository
  private val pokedexService: PokedexService = mock()
  private val pokdexClient: PokedexClient = PokedexClient(pokedexService)
  private val pokemonDao: PokemonDao = mock()

  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    mainRepository = MainRepository(pokdexClient, pokemonDao)
    viewModel = MainViewModel(mainRepository, SavedStateHandle())
  }

  @Test
  fun fetchPokemonListTest() = runBlocking {
    val mockData = MockUtil.mockPokemonList()
    whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData)
    whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData)

    val fetchedDataFlow = mainRepository.fetchPokemonList(
      page = 0,
      onStart = {},
      onComplete = {},
      onError = {}
    ).test(2.seconds) {
      val item = expectItem()
      Assert.assertEquals(item[0].page, 0)
      Assert.assertEquals(item[0].name, "bulbasaur")
      Assert.assertEquals(item, MockUtil.mockPokemonList())
      expectComplete()
    }

    viewModel.fetchNextPokemonList()

    verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)

    fetchedDataFlow.apply {
      // runBlocking should return Unit
    }
  }
}
