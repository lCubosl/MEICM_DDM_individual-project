package com.example.alignment_tool.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("car_preferences")

class CarPreferences(private val context: Context) {

    companion object {
        val CAR_MAKE = stringPreferencesKey("car_make")
        val CAR_MODEL = stringPreferencesKey("car_model")
        val CAR_MAKE_ID = intPreferencesKey("car_make_id")
        val CAR_MODEL_ID = intPreferencesKey("car_model_id")
    }

    val carMake = context.dataStore.data.map { it[CAR_MAKE] ?: "" }
    val carModel = context.dataStore.data.map { it[CAR_MODEL] ?: "" }

    suspend fun saveCarSelection(
        make: String,
        makeId: Int,
        model: String,
        modelId: Int
    ) = context.dataStore.edit {
        it[CAR_MAKE] = make
        it[CAR_MODEL] = model
        it[CAR_MAKE_ID] = makeId
        it[CAR_MODEL_ID] = modelId
    }
}
