package com.veygard.frontiermap.presentation.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.repository.GeoRepResult
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import com.veygard.frontiermap.util.Polygon180Merger.formCombinedCustomClickPolygonList
import com.veygard.frontiermap.util.Polygon180Merger.getPreparedPolygonList
import com.veygard.frontiermap.util.Polygon180Merger.prepareUncommittedPolygonsByAverageLatitude
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
                is GeoRepResult.Success -> {
                    result.geoClusters.forEach { geoCluster ->
                        geoCluster.list.forEach { multiPolygon ->
                            //Прежде чем добавлять полигоны на карту, нужно проверить есть ли полигоны на 180,
                            // которые надо соединить между собой
                            formCombinedCustomClickPolygonList(multiPolygon)
                        }

                        prepareUncommittedPolygonsByAverageLatitude()
                        getPreparedPolygonList().forEach {
                            try {
                                map.overlays.add(it)
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
