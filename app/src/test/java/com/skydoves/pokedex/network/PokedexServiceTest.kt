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
import com.skydoves.pokedex.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import java.io.IOException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PokedexServiceTest : ApiAbstract<PokedexService>() {

  private lateinit var service: PokedexService
  private val client: PokedexClient = mock()

  @Before
  fun initService() {
    service = createService(PokedexService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonListFromNetworkTest() {
    enqueueResponse("/PokemonResponse.json")

    val responseBody = requireNotNull(service.fetchPokemonList().execute().body())
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
}
