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

@file:Suppress("SpellCheckingInspection")

package com.skydoves.pokedex.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.pokedex.MainCoroutinesRule
import com.skydoves.pokedex.model.PokemonInfo
import com.skydoves.pokedex.network.ApiUtil.getCall
import com.skydoves.pokedex.network.PokedexClient
import com.skydoves.pokedex.network.PokedexService
import com.skydoves.pokedex.persistence.PokemonInfoDao
import com.skydoves.pokedex.utils.MockUtil.mockPokemonInfo
import com.skydoves.sandwich.ResponseDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailRepositoryTest {

  private lateinit var repository: DetailRepository
  private lateinit var client: PokedexClient
  private val service: PokedexService = mock()
  private val pokemonInfoDao: PokemonInfoDao = mock()

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @Before
  fun setup() {
    client = PokedexClient(service)
    repository = DetailRepository(client, pokemonInfoDao)
  }

  @Test
  fun fetchPokemonInfoFromNetwork() = runBlocking {
    val mockData = mockPokemonInfo()
    val dataSourceCall =
      ResponseDataSource<PokemonInfo>().combine(getCall(mockData)) {}
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(null)
    whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(dataSourceCall)

    val loadData = repository.fetchPokemonInfo(name = "bulbasaur", onSuccess = {}, onError = {})
    verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
    verify(service, atLeastOnce()).fetchPokemonInfo(name = "bulbasaur")

    val observer: Observer<PokemonInfo> = mock()
    loadData.observeForever(observer)

    val updatedData = mockPokemonInfo()
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(updatedData)

    loadData.postValue(updatedData)
    verify(observer).onChanged(updatedData)
    loadData.removeObserver(observer)
  }

  @Test
  fun fetchPokemonInfoFromDatabase() = runBlocking {
    val mockData = mockPokemonInfo()
    val dataSourceCall =
      ResponseDataSource<PokemonInfo>().combine(getCall(mockData)) {}
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(mockData)
    whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(dataSourceCall)

    val loadData = repository.fetchPokemonInfo(name = "bulbasaur", onSuccess = {}, onError = {})
    verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
    verifyNoMoreInteractions(service)

    val observer: Observer<PokemonInfo> = mock()
    loadData.observeForever(observer)

    val updatedData = mockPokemonInfo()
    whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(updatedData)

    loadData.postValue(updatedData)
    verify(observer).onChanged(updatedData)
    loadData.removeObserver(observer)
  }
}
