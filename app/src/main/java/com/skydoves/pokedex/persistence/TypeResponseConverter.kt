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

package com.skydoves.pokedex.persistence

import androidx.room.TypeConverter
import com.skydoves.pokedex.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object TypeResponseConverter {

  private val moshi = Moshi.Builder().build()

  @JvmStatic
  @TypeConverter
  fun fromString(value: String): List<PokemonInfo.TypeResponse>? {
    val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
    val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
    return adapter.fromJson(value)
  }

  @JvmStatic
  @TypeConverter
  fun fromInfoType(type: List<PokemonInfo.TypeResponse>?): String {
    val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
    val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
    return adapter.toJson(type)
  }
}
