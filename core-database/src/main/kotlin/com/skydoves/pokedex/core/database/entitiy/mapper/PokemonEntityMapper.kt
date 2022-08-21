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

package com.skydoves.pokedex.core.database.entitiy.mapper

import com.skydoves.pokedex.core.database.entitiy.PokemonEntity
import com.skydoves.pokedex.core.model.Pokemon

object PokemonEntityMapper : EntityMapper<List<Pokemon>, List<PokemonEntity>> {

  override fun asEntity(domain: List<Pokemon>): List<PokemonEntity> {
    return domain.map { pokemon ->
      PokemonEntity(
        page = pokemon.page,
        name = pokemon.name,
        url = pokemon.url
      )
    }
  }

  override fun asDomain(entity: List<PokemonEntity>): List<Pokemon> {
    return entity.map { pokemonEntity ->
      Pokemon(
        page = pokemonEntity.page,
        name = pokemonEntity.name,
        url = pokemonEntity.url
      )
    }
  }
}

fun List<Pokemon>.asEntity(): List<PokemonEntity> {
  return PokemonEntityMapper.asEntity(this)
}

fun List<PokemonEntity>.asDomain(): List<Pokemon> {
  return PokemonEntityMapper.asDomain(this)
}
