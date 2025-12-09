package com.example.alignment_tool.data.repository

import com.example.alignment_tool.data.remote.CarApiService
import com.example.alignment_tool.data.remote.model.CarMake
import com.example.alignment_tool.data.remote.model.CarMakeResponse
import com.example.alignment_tool.data.remote.model.CarModel

class CarRepository(private val api: CarApiService) {

    suspend fun getMakes(): List<CarMake> {
        val response = api.getMakes()  // returns CarMakeResponse
        return response.data           // extract the list
    }

    suspend fun getModels(makeId: Int): List<CarModel> {
        val response = api.getModels(makeId) // if API returns object
        return response.data
    }
}

