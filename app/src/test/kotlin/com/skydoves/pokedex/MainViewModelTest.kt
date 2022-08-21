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

package com.skydoves.pokedex

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.core.data.repository.MainRepository
import com.skydoves.core.data.repository.MainRepositoryImpl
import com.skydoves.pokedex.core.database.PokemonDao
import com.skydoves.pokedex.core.database.entitiy.mapper.asEntity
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.pokedex.core.network.service.PokedexService
import com.skydoves.pokedex.core.test.MainCoroutinesRule
import com.skydoves.pokedex.core.test.MockUtil
import com.skydoves.pokedex.ui.main.MainViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainViewModelTest {

  private lateinit var viewModel: MainViewModel
  private lateinit var mainRepository: MainRepository
  private val pokedexService: PokedexService = mock()
  private val pokdexClient: PokedexClient = PokedexClient(pokedexService)
  private val pokemonDao: PokemonDao = mock()

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    mainRepository = MainRepositoryImpl(pokdexClient, pokemonDao, coroutinesRule.testDispatcher)
    viewModel = MainViewModel(mainRepository)
  }

  @Test
  fun fetchPokemonListTest() = runTest {
    val mockData = MockUtil.mockPokemonList()
    whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData.asEntity())
    whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.asEntity())

    mainRepository.fetchPokemonList(
      page = 0,
      onStart = {},
      onComplete = {},
      onError = {}
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val item = awaitItem()
      Assert.assertEquals(item[0].page, 0)
      Assert.assertEquals(item[0].name, "bulbasaur")
      Assert.assertEquals(item, MockUtil.mockPokemonList())
      awaitComplete()
    }

    viewModel.fetchNextPokemonList()

    verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
  }
}
