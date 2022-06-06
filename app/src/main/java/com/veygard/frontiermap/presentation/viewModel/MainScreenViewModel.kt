package com.veygard.frontiermap.presentation.viewModel

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import com.veygard.frontiermap.presentation.widgets.CustomPolygon
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainScreenViewModel(private val getRussiaUseCase: GetRussiaUseCase) : ViewModel() {

    private val _state = MutableLiveData<MainScreenVmState?>()
    val state: LiveData<MainScreenVmState?>
        get() = _state

    fun getRussia(map: MapView) {
        viewModelScope.launch {
            _state.value = MainScreenVmState.Loading

            when (val result = getRussiaUseCase.start()) {
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
                        _state.value = MainScreenVmState.StopLoading
                        _state.value = MainScreenVmState.Success(geoCluster.perimeterLengthKm)
                    }
                }
                is RepoResult.ConnectionError -> _state.value = MainScreenVmState.ConnectionError
                else -> _state.value = MainScreenVmState.ServerError
            }
        }
    }

}