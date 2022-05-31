package com.veygard.frontiermap.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.repository.GeoRepository
import kotlinx.coroutines.launch

class MainScreenViewModel(private val geoRepository: GeoRepository): ViewModel() {
    fun getRussia(){
        viewModelScope.launch {
            val result = geoRepository.getRussia()
        }
    }
}