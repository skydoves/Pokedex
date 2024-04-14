package com.skydoves.pokedex.core.database


import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.skydoves.pokedex.core.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

@ProvidedTypeConverter
class StatsResponseConverter @Inject constructor(
  private val moshi: Moshi,
) {

  @TypeConverter
  fun fromString(value: String): List<PokemonInfo.StatsResponse>? {
    val listType =
      Types.newParameterizedType(List::class.java, PokemonInfo.StatsResponse::class.java)
    val adapter: JsonAdapter<List<PokemonInfo.StatsResponse>> = moshi.adapter(listType)
    return adapter.fromJson(value)
  }

  @TypeConverter
  fun fromInfoType(type: List<PokemonInfo.StatsResponse>?): String {
    val listType =
      Types.newParameterizedType(List::class.java, PokemonInfo.StatsResponse::class.java)
    val adapter: JsonAdapter<List<PokemonInfo.StatsResponse>> = moshi.adapter(listType)
    return adapter.toJson(type)
  }
}