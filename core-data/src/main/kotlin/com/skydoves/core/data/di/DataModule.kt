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

package com.skydoves.core.data.di

import com.skydoves.core.data.repository.DetailRepository
import com.skydoves.core.data.repository.DetailRepositoryImpl
import com.skydoves.core.data.repository.MainRepository
import com.skydoves.core.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun bindsMainRepository(
    mainRepositoryImpl: MainRepositoryImpl
  ): MainRepository

  @Binds
  fun bindsDetailRepository(
    detailRepositoryImpl: DetailRepositoryImpl
  ): DetailRepository
}