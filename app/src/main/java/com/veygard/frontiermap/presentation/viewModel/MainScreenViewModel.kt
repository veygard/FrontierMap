package com.veygard.frontiermap.presentation.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.domain.models.PolygonWith180LongitudeInfo
import com.veygard.frontiermap.domain.repository.GeoRepResult
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import com.veygard.frontiermap.presentation.widgets.CustomClickPolygon
import com.veygard.frontiermap.presentation.widgets.toClickPolygon
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView

class MainScreenViewModel(private val getRussiaUseCase: GetRussiaUseCase) : ViewModel() {
    private val combinedPolygonsList = mutableListOf<CustomClickPolygon>()
    private val polygon180LongitudeList = mutableListOf<PolygonWith180LongitudeInfo>()

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
                            //Прежде чем добавлять полигоны на карту, нужно проверить если полигоны на 180,
                            // которые надо соеденить между собой
                            formCombinedCustomClickPolygonList(multiPolygon)
                            try {


                                multiPolygon.polygons.forEach { polygon ->
                                    map.overlays.add(getCustomClickPolygon(polygon))
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

    private fun getCustomClickPolygon(polygon: PolygonWith180LongitudeInfo): CustomClickPolygon {
        return when (polygon.isHave180GeoPoints) {
            true -> {
                polygon.toClickPolygon()
            }
            else -> polygon.toClickPolygon()
        }
    }

    private fun formCombinedCustomClickPolygonList(multiPolygon: MultiPolygon) {
        multiPolygon.polygons.forEach { polygon ->
            when (polygon.isHave180GeoPoints) {
                true -> {
                    polygon180LongitudeList.add(polygon)
                }
                else -> combinedPolygonsList.add(polygon.toClickPolygon())
            }
            //После того как раскидали полигоны по спискам, делаем склейку полигонов 180 меридиана
            combinePolygon180()
        }
    }

    private fun combinePolygon180(){
         val  ars = 0
    }
}