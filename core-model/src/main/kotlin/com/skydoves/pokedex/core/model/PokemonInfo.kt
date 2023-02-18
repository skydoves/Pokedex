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

package com.skydoves.pokedex.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfo(
  @field:Json(name = "id")
  val id: Int,
  @field:Json(name = "name") val name: String,
  @field:Json(name = "height") val height: Int,
  @field:Json(name = "weight") val weight: Int,
  @field:Json(name = "base_experience") val experience: Int,
  @field:Json(name = "types") val types: List<TypeResponse>,
  val hp: Int = (50..maxHp).random(),
  val attack: Int = (50..maxAttack).random(),
  val defense: Int = (50..maxDefense).random(),
  val speed: Int = (50..maxSpeed).random(),
  val exp: Int = (200..maxExp).random(),
) {

  fun getIdString(): String = String.format("#%03d", id)
  fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
  fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)

  @JsonClass(generateAdapter = true)
  data class TypeResponse(
    @field:Json(name = "slot") val slot: Int,
    @field:Json(name = "type") val type: Type,
  )

  @JsonClass(generateAdapter = true)
  data class Type(
    @field:Json(name = "name") val name: String,
  )

  companion object {
    const val maxHp = 300
    const val maxAttack = 300
    const val maxDefense = 300
    const val maxSpeed = 300
    const val maxExp = 1000
  }
}
