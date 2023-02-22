package com.skydoves.pokedex.ui.details

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AnimatedProgressBar(
  progressValue: Int,
  maximumValue: Int,
  progressColor: Color,
  modifier: Modifier,
) {

  var value by remember { mutableStateOf(0) }
  LaunchedEffect(progressValue) {
    value = progressValue
  }
  // state represent the percent of the progress value compared with maximum value
  val percent by remember(value) {
    derivedStateOf {
      value.toFloat() / maximumValue.toFloat()
    }
  }
  val animatedPercent by animateFloatAsState(
    targetValue = percent, tween(
      durationMillis = 1500, delayMillis = 0, easing = LinearOutSlowInEasing
    )
  )
  val shape = RoundedCornerShape(50)
  Box(
    modifier = modifier
      .height(18.dp)
      .background(
        color = Color.White, shape = shape
      )
  ) {
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(animatedPercent)
        .background(
          color = progressColor, shape = shape
        )
    )
    Text(
      text = "$progressValue/$maximumValue",
      fontSize = 12.sp,
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth(animatedPercent)
        .padding(end = 5.dp)
        .align(Alignment.CenterStart),
      textAlign = TextAlign.End
    )
  }
}