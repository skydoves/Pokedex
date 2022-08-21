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

package com.skydoves.pokedex.core.data

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.core.data.repository.MainRepositoryImpl
import com.skydoves.pokedex.core.database.PokemonDao
import com.skydoves.pokedex.core.database.entitiy.mapper.asEntity
import com.skydoves.pokedex.core.network.model.PokemonResponse
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.pokedex.core.network.service.PokedexService
import com.skydoves.pokedex.core.test.MainCoroutinesRule
import com.skydoves.pokedex.core.test.MockUtil.mockPokemonList
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainRepositoryImplTest {

  private lateinit var repository: MainRepositoryImpl
  private lateinit var client: PokedexClient
  private val service: PokedexService = mock()
  private val pokemonDao: PokemonDao = mock()

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    client = PokedexClient(service)
    repository = MainRepositoryImpl(client, pokemonDao, coroutinesRule.testDispatcher)
  }

  @Test
  fun fetchPokemonListFromNetworkTest() = runTest {
    val mockData =
      PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
    whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(emptyList())
    whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())
    whenever(service.fetchPokemonList()).thenReturn(ApiResponse.of { Response.success(mockData) })

    repository.fetchPokemonList(
      page = 0,
      onStart = {},
      onComplete = {},
      onError = {}
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val expectItem = awaitItem()[0]
      assertEquals(expectItem.page, 0)
      assertEquals(expectItem.name, "bulbasaur")
      assertEquals(expectItem, mockPokemonList()[0])
      awaitComplete()
    }

    verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
    verify(service, atLeastOnce()).fetchPokemonList()
    verify(pokemonDao, atLeastOnce()).insertPokemonList(mockData.results.asEntity())
    verifyNoMoreInteractions(service)
  }

  @Test
  fun fetchPokemonListFromDatabaseTest() = runTest {
    val mockData =
      PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
    whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())
    whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())

    repository.fetchPokemonList(
      page = 0,
      onStart = {},
      onComplete = {},
      onError = {}
    ).test(2.toDuration(DurationUnit.SECONDS)) {
      val expectItem = awaitItem()[0]
      assertEquals(expectItem.page, 0)
      assertEquals(expectItem.name, "bulbasaur")
      assertEquals(expectItem, mockPokemonList()[0])
      awaitComplete()
    }

    verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
    verify(pokemonDao, atLeastOnce()).getAllPokemonList(page_ = 0)
  }
}
