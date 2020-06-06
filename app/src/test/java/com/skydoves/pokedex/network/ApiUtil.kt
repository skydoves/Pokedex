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

package com.skydoves.pokedex.network

import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiUtil {

  fun <T> getCall(data: T) = object : Call<T> {
    override fun enqueue(callback: Callback<T>) = Unit
    override fun isExecuted() = false
    override fun clone(): Call<T> = this
    override fun isCanceled() = false
    override fun cancel() = Unit
    override fun request(): Request = Request.Builder().build()
    override fun execute(): Response<T> = Response.success(data)
  }
}
