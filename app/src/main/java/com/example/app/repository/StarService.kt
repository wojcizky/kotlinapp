package com.example.app.repository

import com.example.app.repository.model.StarResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface StarService {

    @GET("/api/people")
    suspend fun getStarResponse(): Response<StarResponse>

    companion object {
        private const val STAR_URL = "https://swapi.dev/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(STAR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val starService: StarService by lazy {
            retrofit.create(StarService::class.java)
        }
    }
}