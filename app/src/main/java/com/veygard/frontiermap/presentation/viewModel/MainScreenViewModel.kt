package com.veygard.frontiermap.presentation.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView

class MainScreenViewModel(private val getRussiaUseCase: GetRussiaUseCase) : ViewModel() {

    private val _state = MutableLiveData<MainScreenVmState?>()
    val state: LiveData<MainScreenVmState?>
        get() = _state

    fun getRussia(map: MapView) {
        viewModelScope.launch {
            _state.value = MainScreenVmState.Loading
            val result = getRussiaUseCase.start()
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
                        _state.value = MainScreenVmState.StopLoading
                        _state.value = MainScreenVmState.Success(geoCluster.perimeterLengthKm)
                    }
                }
                else -> _state.value = MainScreenVmState.Error
            }
        }
    }

}