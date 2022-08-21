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

package com.skydoves.pokedex

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.skydoves.pokedex.core.test.MockUtil
import com.skydoves.pokedex.ui.details.DetailActivity
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailActivityInjectionTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Test
  fun verifyInjection() {
    val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java)
    val transformationLayout = TransformationLayout(ApplicationProvider.getApplicationContext())
    transformationLayout.transitionName = "lapras"
    intent.putExtra("com.skydoves.transformationlayout", transformationLayout.getParcelableParams())
    intent.putExtra(DetailActivity.EXTRA_POKEMON, MockUtil.mockPokemon())
    ActivityScenario.launch<DetailActivity>(intent).use {
      it.moveToState(Lifecycle.State.CREATED)
      it.onActivity { activity ->
        assertThat(activity.detailViewModelFactory).isNotNull()
        assertThat(activity.viewModel).isNotNull()
      }
    }
  }
}
