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

@file:OptIn(ExperimentalGlideComposeApi::class)

package com.skydoves.pokedex.ui.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.skydoves.pokedex.R
import com.skydoves.pokedex.core.model.Pokemon
import com.skydoves.pokedex.core.model.PokemonInfo

@Composable
fun DetailScreen(
  uiState: DetailUiState,
  onBackIconPressed: () -> Unit = {},
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        colorResource(id = R.color.background)
      )
  ) {
    val context = LocalContext.current
    LaunchedEffect(uiState) {
      if (uiState is DetailUiState.Failure) {
        Toast.makeText(
          context,
          uiState.message,
          Toast.LENGTH_SHORT
        ).show()
      }
    }
    if (uiState is DetailUiState.Loading) {
      CircularProgressIndicator(
        modifier = Modifier
          .align(Alignment.Center)
          .size(50.dp),
        color = colorResource(R.color.colorAccent)
      )
    }
    if (uiState is DetailUiState.Success) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(
            rememberScrollState()
          ), horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Header(uiState, onBackIconPressed)
        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
        )
        Text(
          text = uiState.pokemon.name,
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
        )
        RibbonsRow(
          types = uiState.pokemonInfo.types.map { it.type.name }, modifier = Modifier
        )
        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
        )
        ConstraintLayout(
          modifier = Modifier.fillMaxWidth()
        ) {
          val (weight, height, weightTitle, heightTitle) = createRefs()
          createHorizontalChain(weight, height, chainStyle = ChainStyle.Spread)
          Text(text = uiState.pokemonInfo.getWeightString(),
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
              .padding(10.dp)
              .constrainAs(weight) {
                start.linkTo(parent.start)
              })
          Text(text = uiState.pokemonInfo.getHeightString(),
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
              .padding(10.dp)
              .constrainAs(height) {
                end.linkTo(parent.end)
              })
          Text(text = stringResource(id = R.string.weight),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier.constrainAs(weightTitle) {
              top.linkTo(weight.bottom)
              start.linkTo(weight.start)
              end.linkTo(weight.end)
            })
          Text(text = stringResource(id = R.string.height),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier.constrainAs(heightTitle) {
              top.linkTo(height.bottom)
              start.linkTo(height.start)
              end.linkTo(height.end)
            })
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = stringResource(id = R.string.base_stats),
          fontSize = 21.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          modifier = Modifier.padding(10.dp)
        )
        AbilityRow(
          stringResource(id = R.string.hp),
          uiState.pokemonInfo.hp,
          PokemonInfo.maxHp,
          colorResource(id = R.color.colorPrimary)
        )
        Spacer(modifier = Modifier.size(10.dp))
        AbilityRow(
          stringResource(id = R.string.atk),
          uiState.pokemonInfo.attack,
          PokemonInfo.maxAttack,
          colorResource(id = R.color.md_orange_100)
        )
        Spacer(modifier = Modifier.size(10.dp))
        AbilityRow(
          stringResource(id = R.string.def),
          uiState.pokemonInfo.defense,
          PokemonInfo.maxDefense,
          colorResource(id = R.color.md_blue_100)
        )
        Spacer(modifier = Modifier.size(10.dp))
        AbilityRow(
          stringResource(id = R.string.spd),
          uiState.pokemonInfo.speed,
          PokemonInfo.maxSpeed,
          colorResource(id = R.color.flying)
        )
        Spacer(modifier = Modifier.size(10.dp))
        AbilityRow(
          stringResource(id = R.string.exp),
          uiState.pokemonInfo.exp,
          PokemonInfo.maxExp,
          colorResource(id = R.color.md_green_200)
        )
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
private fun Header(
  uiState: DetailUiState.Success,
  onBackIconPressed: () -> Unit,
) {

  val brush = rememberPaletteBrush(
    imageUrl = uiState.pokemon.getImageUrl()
  )
  ConstraintLayout(
    modifier = Modifier
      .fillMaxWidth()
      .height(260.dp)
      .background(
        brush = brush, shape = RoundedCornerShape(
          bottomEndPercent = 20, bottomStartPercent = 20
        )
      )
  ) {
    val (image, index, backButton, appName) = createRefs()
    GlideImage(model = uiState.pokemon.getImageUrl(),
      contentDescription = null,
      modifier = Modifier
        .size(190.dp)
        .constrainAs(image) {
          bottom.linkTo(
            anchor = parent.bottom, margin = 20.dp
          )
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        })
    Text(text = uiState.pokemonInfo.getIdString(),
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      modifier = Modifier.constrainAs(index) {
        end.linkTo(
          anchor = parent.end, margin = 12.dp
        )
        top.linkTo(
          anchor = parent.top, margin = 12.dp
        )
      })
    IconButton(onClick = onBackIconPressed, modifier = Modifier.constrainAs(backButton) {
      top.linkTo(parent.top)
      start.linkTo(parent.start)
    }) {
      Icon(
        painterResource(R.drawable.ic_arrow), tint = Color.White, modifier = Modifier.padding(
          start = 12.dp, end = 6.dp, top = 12.dp
        ), contentDescription = null
      )
    }
    Text(text = stringResource(id = R.string.app_name),
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      modifier = Modifier.constrainAs(appName) {
        start.linkTo(
          anchor = backButton.end
        )
        top.linkTo(
          anchor = backButton.top, margin = 10.dp
        )
        bottom.linkTo(backButton.bottom)
      })
  }
}

@Composable
private fun AbilityRow(
  title: String,
  progressValue: Int,
  maximumValue: Int,
  progressColor: Color,
) {
  Row {
    Spacer(modifier = Modifier.width(26.dp))
    Text(
      text = title, color = Color.White, fontSize = 12.sp, modifier = Modifier.width(40.dp)
    )
    AnimatedProgressBar(
      modifier = Modifier.weight(1f),
      progressValue = progressValue,
      maximumValue = maximumValue,
      progressColor = progressColor
    )
    Spacer(modifier = Modifier.width(26.dp))
  }
}

@Composable
fun rememberPaletteBrush(
  imageUrl: String,
): Brush {
  val context = LocalContext.current
  var brush by remember { mutableStateOf(Brush.verticalGradient(List(2) { Color.Transparent })) }
  DisposableEffect(imageUrl) {
    Glide.with(context).load(imageUrl).listener(
      GlidePalette.with(imageUrl).use(BitmapPalette.Profile.MUTED_LIGHT).intoCallBack { palette ->
        val light = palette?.lightVibrantSwatch?.rgb
        val domain = palette?.dominantSwatch?.rgb
        if (domain != null) {
          brush = if (light != null) {
            Brush.verticalGradient(listOf(Color(domain), Color(light)))
          } else {
            Brush.verticalGradient(List(2) { Color(domain) })
          }
        }
      }.crossfade(true)
    ).preload()
    onDispose {}
  }
  return brush
}

@Preview
@Composable
fun DetailScreenLoadingPreview() {
  MaterialTheme {
    DetailScreen(uiState = DetailUiState.Loading)
  }
}

@Preview
@Composable
fun DetailScreenSuccessPreview() {
  MaterialTheme {
    val pokemon = Pokemon(
      0, "charmander", "https://pokeapi.co/api/v2/pokemon/4/"
    )
    val pokemonInfo = PokemonInfo(
      id = 15,
      name = "charmander",
      height = 50,
      weight = 10,
      experience = 3,
      types = listOf(PokemonInfo.TypeResponse(1, PokemonInfo.Type("ground")))
    )
    DetailScreen(
      uiState = DetailUiState.Success(
        pokemon = pokemon, pokemonInfo = pokemonInfo
      )
    )
  }
}
