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

package com.skydoves.pokedex.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.pokedex.MainCoroutinesRule
import com.skydoves.pokedex.model.PokemonInfo
import com.skydoves.pokedex.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

@ExperimentalCoroutinesApi
class PokedexServiceTest : ApiAbstract<PokedexService>() {

  private lateinit var service: PokedexService
  private val client: PokedexClient = mock()

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @Before
  fun initService() {
    service = createService(PokedexService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonListFromNetworkTest() = runBlocking {
    enqueueResponse("/PokemonResponse.json")
    val dataSource = requireNotNull(service.fetchPokemonList().toResponseDataSource())
    mockWebServer.takeRequest()

    val call = requireNotNull(dataSource.call?.clone())
    enqueueResponse("/PokemonResponse.json")
    val responseBody = requireNotNull(call.execute().body())
    mockWebServer.takeRequest()

    assertThat(responseBody.count, `is`(964))
    assertThat(responseBody.results[0].name, `is`("bulbasaur"))
    assertThat(responseBody.results[0].url, `is`("https://pokeapi.co/api/v2/pokemon/1/"))

    val onResult: (response: ApiResponse<PokemonResponse>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Success::class.java))
      val response: PokemonResponse = requireNotNull((it as ApiResponse.Success).data)
      assertThat(response.count, `is`(964))
      assertThat(response.results[0].name, `is`("bulbasaur"))
      assertThat(response.results[0].url, `is`("https://pokeapi.co/api/v2/pokemon/1/"))
    }

    whenever(client.fetchPokemonList(0, onResult)).thenAnswer {
      val response: (response: ApiResponse<PokemonResponse>) -> Unit = it.getArgument(1)
      response(ApiResponse.Success(Response.success(responseBody)))
    }

    client.fetchPokemonList(page = 0, onResult = onResult)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonInfoFromNetworkTest() = runBlocking {
    enqueueResponse("/Bulbasaur.json")
    val dataSource = requireNotNull(service.fetchPokemonInfo("bulbasaur")).toResponseDataSource()
    mockWebServer.takeRequest()

    val call = requireNotNull(dataSource.call?.clone())
    enqueueResponse("/Bulbasaur.json")
    val responseBody = requireNotNull(call.execute().body())
    mockWebServer.takeRequest()

    assertThat(responseBody.id, `is`(1))
    assertThat(responseBody.name, `is`("bulbasaur"))
    assertThat(responseBody.height, `is`(7))
    assertThat(responseBody.weight, `is`(69))
    assertThat(responseBody.experience, `is`(64))

    val onResult: (response: ApiResponse<PokemonInfo>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Success::class.java))
      val response: PokemonInfo = requireNotNull((it as ApiResponse.Success).data)
      assertThat(response.id, `is`(1))
      assertThat(response.name, `is`("bulbasaur"))
      assertThat(response.height, `is`(7))
      assertThat(response.weight, `is`(69))
      assertThat(response.experience, `is`(64))
    }

    whenever(client.fetchPokemonInfo("bulbasaur", onResult)).thenAnswer {
      val response: (response: ApiResponse<PokemonInfo>) -> Unit = it.getArgument(1)
      response(ApiResponse.Success(Response.success(responseBody)))
    }

    client.fetchPokemonInfo(name = "bulbasaur", onResult = onResult)
  }
}
