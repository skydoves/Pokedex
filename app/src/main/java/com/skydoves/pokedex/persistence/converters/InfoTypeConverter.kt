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

package com.skydoves.pokedex.persistence.converters

import androidx.room.TypeConverter
import com.skydoves.pokedex.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

open class InfoTypeConverter {

  private val moshi = Moshi.Builder().build()

  @TypeConverter
  fun fromString(value: String): PokemonInfo.Type? {
    val adapter: JsonAdapter<PokemonInfo.Type> = moshi.adapter(PokemonInfo.Type::class.java)
    return adapter.fromJson(value)
  }

  @TypeConverter
  fun fromInfoType(type: PokemonInfo.Type): String {
    val adapter: JsonAdapter<PokemonInfo.Type> = moshi.adapter(PokemonInfo.Type::class.java)
    return adapter.toJson(type)
  }
}
