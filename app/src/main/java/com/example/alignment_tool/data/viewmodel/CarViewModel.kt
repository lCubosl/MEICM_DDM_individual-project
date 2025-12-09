package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.data.datastore.CarPreferences
import com.example.alignment_tool.data.remote.model.CarMake
import com.example.alignment_tool.data.remote.model.CarModel
import com.example.alignment_tool.data.repository.CarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CarViewModel(
    private val repository: CarRepository,
    private val prefs: CarPreferences
) : ViewModel() {

    private val _makes = MutableStateFlow<List<CarMake>>(emptyList())
    val makes: StateFlow<List<CarMake>> = _makes

    private val _models = MutableStateFlow<List<CarModel>>(emptyList())
    val models: StateFlow<List<CarModel>> = _models

    private val _selectedMake = MutableStateFlow<CarMake?>(null)
    val selectedMake = _selectedMake.asStateFlow()

    private val _selectedModel = MutableStateFlow<CarModel?>(null)
    val selectedModel = _selectedModel.asStateFlow()

    suspend fun loadMakes() {
        try {
            val makesList = repository.getMakes()
            _makes.value = makesList
        } catch (e: Exception) {
            e.printStackTrace()
            _makes.value = emptyList()
        }
    }

    fun loadModels(makeId: Int) {
        viewModelScope.launch {
            _models.value = repository.getModels(makeId)
        }
    }

    fun selectMake(make: CarMake) {
        _selectedMake.value = make
        loadModels(make.id)
    }

    fun selectModel(model: CarModel) {
        _selectedModel.value = model
    }

    fun saveCarSelection() {
        val make = selectedMake.value ?: return
        val model = selectedModel.value ?: return

        viewModelScope.launch {
            prefs.saveCarSelection(
                make.name, make.id,
                model.name, model.id
            )
        }
    }
}
