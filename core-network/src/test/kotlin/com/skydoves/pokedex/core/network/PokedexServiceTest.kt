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

package com.skydoves.pokedex.core.network

import com.skydoves.pokedex.core.network.service.PokedexService
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class PokedexServiceTest : ApiAbstract<PokedexService>() {

  private lateinit var service: PokedexService

  @Before
  fun initService() {
    service = createService(PokedexService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonListFromNetworkTest() = runTest {
    enqueueResponse("/PokemonResponse.json")
    val response = service.fetchPokemonList()
    val responseBody = requireNotNull((response as ApiResponse.Success).data)

    assertThat(responseBody.count, `is`(964))
    assertThat(responseBody.results[0].name, `is`("bulbasaur"))
    assertThat(responseBody.results[0].url, `is`("https://pokeapi.co/api/v2/pokemon/1/"))
  }

  @Throws(IOException::class)
  @Test
  fun fetchPokemonInfoFromNetworkTest() = runTest {
    enqueueResponse("/Bulbasaur.json")
    val response = service.fetchPokemonInfo("bulbasaur")
    val responseBody = requireNotNull((response as ApiResponse.Success).data)

    assertThat(responseBody.id, `is`(1))
    assertThat(responseBody.name, `is`("bulbasaur"))
    assertThat(responseBody.height, `is`(7))
    assertThat(responseBody.weight, `is`(69))
    assertThat(responseBody.experience, `is`(64))
  }
}
