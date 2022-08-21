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
import com.skydoves.core.data.repository.DetailRepositoryImpl
import com.skydoves.pokedex.core.database.PokemonInfoDao
import com.skydoves.pokedex.core.database.entitiy.mapper.asEntity
import com.skydoves.pokedex.core.network.service.PokedexClient
import com.skydoves.pokedex.core.network.service.PokedexService
import com.skydoves.pokedex.core.test.MainCoroutinesRule
import com.skydoves.pokedex.core.test.MockUtil.mockPokemonInfo
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DetailRepositoryTest {

  private lateinit var repository: DetailRepositoryImpl
  private lateinit var client: PokedexClient
  private val service: PokedexService = mock()
  private val pokemonInfoDao: PokemonInfoDao = mock()

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  @Before
  fun setup() {
    client = PokedexClient(service)
    repository = DetailRepositoryImpl(client, pokemonInfoDao, coroutinesRule.testDispatcher)
  }

  @Test
  fun fetchPokemonInfoFromNetworkTest() = runTest {
    val mockData = mockPokemonInfo()
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(null)
    whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(
      ApiResponse.of {
        Response.success(
          mockData
        )
      }
    )

    repository.fetchPokemonInfo(name = "bulbasaur", onComplete = {}, onError = {}).test {
      val expectItem = awaitItem()
      assertEquals(expectItem.id, mockData.id)
      assertEquals(expectItem.name, mockData.name)
      assertEquals(expectItem, mockData)
      awaitComplete()
    }

    verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
    verify(service, atLeastOnce()).fetchPokemonInfo(name = "bulbasaur")
    verify(pokemonInfoDao, atLeastOnce()).insertPokemonInfo(mockData.asEntity())
    verifyNoMoreInteractions(service)
  }

  @Test
  fun fetchPokemonInfoFromDatabaseTest() = runTest {
    val mockData = mockPokemonInfo()
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(mockData.asEntity())
    whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(
      ApiResponse.of {
        Response.success(
          mockData
        )
      }
    )

    repository.fetchPokemonInfo(
      name = "bulbasaur",
      onComplete = {},
      onError = {}
    ).test(5.toDuration(DurationUnit.SECONDS)) {
      val expectItem = awaitItem()
      assertEquals(expectItem.id, mockData.id)
      assertEquals(expectItem.name, mockData.name)
      assertEquals(expectItem, mockData)
      awaitComplete()
    }

    verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
    verifyNoMoreInteractions(service)
  }
}
