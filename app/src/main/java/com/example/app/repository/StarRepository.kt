package com.example.app.repository

import com.example.app.repository.model.StarResponse
import retrofit2.Response

class StarRepository {

    suspend fun getStarResponse(): Response<StarResponse> =
        StarService.starService.getStarResponse()

}