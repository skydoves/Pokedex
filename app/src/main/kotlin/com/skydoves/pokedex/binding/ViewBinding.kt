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

package com.skydoves.pokedex.binding

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.card.MaterialCardView
import com.skydoves.androidribbon.RibbonRecyclerView
import com.skydoves.androidribbon.ribbonView
import com.skydoves.pokedex.core.model.PokemonInfo
import com.skydoves.pokedex.utils.PokemonTypeUtils
import com.skydoves.pokedex.utils.SpacesItemDecoration
import com.skydoves.progressview.ProgressView
import com.skydoves.rainbow.Rainbow
import com.skydoves.rainbow.RainbowOrientation
import com.skydoves.rainbow.color
import com.skydoves.whatif.whatIfNotNullOrEmpty

object ViewBinding {

  @JvmStatic
  @BindingAdapter("toast")
  fun bindToast(view: View, text: String?) {
    text.whatIfNotNullOrEmpty {
      Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
    }
  }

  @JvmStatic
  @BindingAdapter("paletteImage", "paletteCard")
  fun bindLoadImagePalette(view: AppCompatImageView, url: String, paletteCard: MaterialCardView) {
    Glide.with(view.context)
      .load(url)
      .listener(
        GlidePalette.with(url)
          .use(BitmapPalette.Profile.MUTED_LIGHT)
          .intoCallBack { palette ->
            val rgb = palette?.dominantSwatch?.rgb
            if (rgb != null) {
              paletteCard.setCardBackgroundColor(rgb)
            }
          }.crossfade(true)
      ).into(view)
  }

  @JvmStatic
  @BindingAdapter("paletteImage", "paletteView")
  fun bindLoadImagePaletteView(view: AppCompatImageView, url: String, paletteView: View) {
    val context = view.context
    Glide.with(context)
      .load(url)
      .listener(
        GlidePalette.with(url)
          .use(BitmapPalette.Profile.MUTED_LIGHT)
          .intoCallBack { palette ->
            val light = palette?.lightVibrantSwatch?.rgb
            val domain = palette?.dominantSwatch?.rgb
            if (domain != null) {
              if (light != null) {
                Rainbow(paletteView).palette {
                  +color(domain)
                  +color(light)
                }.background(orientation = RainbowOrientation.TOP_BOTTOM)
              } else {
                paletteView.setBackgroundColor(domain)
              }
              if (context is AppCompatActivity) {
                context.window.apply {
                  addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                  statusBarColor = domain
                }
              }
            }
          }.crossfade(true)
      ).into(view)
  }

  @JvmStatic
  @BindingAdapter("gone")
  fun bindGone(view: View, shouldBeGone: Boolean) {
    view.visibility = if (shouldBeGone) {
      View.GONE
    } else {
      View.VISIBLE
    }
  }

  @JvmStatic
  @BindingAdapter("onBackPressed")
  fun bindOnBackPressed(view: View, onBackPress: Boolean) {
    val context = view.context
    if (onBackPress && context is OnBackPressedDispatcherOwner) {
      view.setOnClickListener {
        context.onBackPressedDispatcher.onBackPressed()
      }
    }
  }

  @JvmStatic
  @BindingAdapter("bindPokemonTypes")
  fun bindPokemonTypes(recyclerView: RibbonRecyclerView, types: List<PokemonInfo.TypeResponse>?) {
    types.whatIfNotNullOrEmpty {
      recyclerView.clear()
      for (type in it) {
        with(recyclerView) {
          addRibbon(
            ribbonView(context) {
              setText(type.type.name)
              setTextColor(Color.WHITE)
              setPaddingLeft(84f)
              setPaddingRight(84f)
              setPaddingTop(2f)
              setPaddingBottom(10f)
              setTextSize(16f)
              setRibbonRadius(120f)
              setTextStyle(Typeface.BOLD)
              setRibbonBackgroundColorResource(
                PokemonTypeUtils.getTypeColor(type.type.name)
              )
            }.apply {
              maxLines = 1
              gravity = Gravity.CENTER
            }
          )
          addItemDecoration(SpacesItemDecoration())
        }
      }
    }
  }

  @JvmStatic
  @BindingAdapter("progressView_labelText")
  fun bindProgressViewLabelText(progressView: ProgressView, text: String?) {
    progressView.labelText = text
  }

  @JvmStatic
  @BindingAdapter("progressView_progress")
  fun bindProgressViewProgress(progressView: ProgressView, value: Int?) {
    if (value != null) {
      progressView.progress = value.toFloat()
    }
  }

  @JvmStatic
  @BindingAdapter("progressView_max")
  fun bindProgressViewMax(progressView: ProgressView, value: Int?) {
    if (value != null) {
      progressView.max = value.toFloat()
    }
  }
}
