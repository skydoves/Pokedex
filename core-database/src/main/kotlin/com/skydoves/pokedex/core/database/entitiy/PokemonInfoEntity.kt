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

package com.skydoves.pokedex.core.database.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skydoves.pokedex.core.model.PokemonInfo

@Entity
data class PokemonInfoEntity(
  @PrimaryKey val id: Int,
  val name: String,
  val height: Int,
  val weight: Int,
  val experience: Int,
  val types: List<PokemonInfo.TypeResponse>,
  val hp: Int,
  val attack: Int,
  val defense: Int,
  val speed: Int,
  val exp: Int,
  val stats: List<PokemonInfo.StatsResponse>,
)
