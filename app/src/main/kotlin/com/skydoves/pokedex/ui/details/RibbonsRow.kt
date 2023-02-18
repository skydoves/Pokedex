/*
 * Designed and developed by 2023 houssem85 (Houssemeddine Daoud)
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

package com.skydoves.pokedex.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.pokedex.utils.PokemonTypeUtils

@Composable
internal fun RibbonsRow(
  types: List<String>,
  modifier: Modifier = Modifier,
) {
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(20.dp),
    modifier = modifier
  ) {
    val shape = RoundedCornerShape(50)
    items(types) {
      Box(
        modifier = modifier
          .size(140.dp, 26.dp)
          .background(
            color = colorResource(id = PokemonTypeUtils.getTypeColor(it)),
            shape = shape
          )
      ) {
        Text(
          text = it,
          modifier = Modifier.align(Alignment.Center),
          color = Color.White,
          fontSize = 16.sp,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@Preview
@Composable
fun RibbonsRowPreview() {
  MaterialTheme {
    RibbonsRow(
      listOf(
        "ground",
        "fighting"
      )
    )
  }
}
