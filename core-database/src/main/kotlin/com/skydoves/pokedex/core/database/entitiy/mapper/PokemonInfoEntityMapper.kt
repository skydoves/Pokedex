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

import com.skydoves.pokedex.core.database.entitiy.PokemonInfoEntity
import com.skydoves.pokedex.core.model.PokemonInfo

object PokemonInfoEntityMapper : EntityMapper<PokemonInfo, PokemonInfoEntity> {

  override fun asEntity(domain: PokemonInfo): PokemonInfoEntity {
    return PokemonInfoEntity(
      id = domain.id,
      name = domain.name,
      height = domain.height,
      weight = domain.weight,
      experience = domain.experience,
      types = domain.types,
      hp = domain.hp,
      attack = domain.attack,
      defense = domain.defense,
      speed = domain.speed,
      exp = domain.exp
    )
  }

  override fun asDomain(entity: PokemonInfoEntity): PokemonInfo {
    return PokemonInfo(
      id = entity.id,
      name = entity.name,
      height = entity.height,
      weight = entity.weight,
      experience = entity.experience,
      types = entity.types,
      hp = entity.hp,
      attack = entity.attack,
      defense = entity.defense,
      speed = entity.speed,
      exp = entity.exp
    )
  }
}

fun PokemonInfo.asEntity(): PokemonInfoEntity {
  return PokemonInfoEntityMapper.asEntity(this)
}

fun PokemonInfoEntity.asDomain(): PokemonInfo {
  return PokemonInfoEntityMapper.asDomain(this)
}
