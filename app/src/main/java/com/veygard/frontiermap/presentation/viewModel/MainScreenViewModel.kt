package com.veygard.frontiermap.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.repository.GeoRepository
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.views.MapView

class MainScreenViewModel(private val geoRepository: GeoRepository): ViewModel() {

    fun getRussia(map: MapView){
        viewModelScope.launch {
            val result = geoRepository.getRussia()


        }
    }
}