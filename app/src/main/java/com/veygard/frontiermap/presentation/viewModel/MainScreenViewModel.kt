package com.veygard.frontiermap.presentation.viewModel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.presentation.widgets.CustomPolygon
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainScreenViewModel(private val geoRepository: GeoRepository) : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?>
        get() = _isLoading

    fun getRussia(map: MapView) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = geoRepository.getRussia()
            when (result) {
                is RepoResult.Success -> {
                    result.geoClusters.forEach { geoCluster ->
                        geoCluster.list.forEach { multiPolygon ->
                            try {
                                multiPolygon.polygons.forEach { polygon ->
                                    map.overlays.add(polygon)
                                    Log.e("Polygon added", "poly number")
                                }
                            } catch (e: Exception) {
                                Log.e("Point_exception", "exception $e")
                            }
                        }
                    }
                    _isLoading.value = false
                }
            }
        }
    }

}