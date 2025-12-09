package com.example.alignment_tool.data.remote

import com.example.alignment_tool.data.remote.model.CarMakeResponse
import com.example.alignment_tool.data.remote.model.CarModel
import com.example.alignment_tool.data.remote.model.CarModelsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CarApiService {

    @GET("makes/v2")
    suspend fun getMakes(): CarMakeResponse

    @GET("models/v2")
    suspend fun getModels(@Query("make_id") makeId: Int): CarModelsResponse

}
