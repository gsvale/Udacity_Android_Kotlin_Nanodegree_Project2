package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// Build the Moshi object that Retrofit will use
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


// Retrofit builder to build a retrofit object using a Moshi converter
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface AsteroidsService {

    @GET("/planetary/apod")
    fun getPictureOfDay(
        @Query("api_key") API_KEY: String = Constants.API_KEY
    ): Deferred<NetworkPictureOfDay>

    @GET("/neo/rest/v1/feed")
    fun getAsteroids(
        @Query("api_key") API_KEY: String = Constants.API_KEY
    ): Deferred<String>

}

// Api object that exposes the Retrofit service
object AsteroidsAPI {
    val retrofitService: AsteroidsService by lazy { retrofit.create(AsteroidsService::class.java) }
}