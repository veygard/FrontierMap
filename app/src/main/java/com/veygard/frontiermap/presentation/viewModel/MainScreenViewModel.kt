package com.veygard.frontiermap.presentation.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.domain.models.PolygonWith180LongitudeInfo
import com.veygard.frontiermap.domain.repository.GeoRepResult
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import com.veygard.frontiermap.presentation.widgets.CustomClickPolygon
import com.veygard.frontiermap.presentation.widgets.toClickPolygon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
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
            delay(5000)
            val result = getRussiaUseCase.start()
            when (result) {
                is GeoRepResult.Success -> {
                    result.geoClusters.forEach { geoCluster ->
                        geoCluster.list.forEach { multiPolygon ->
                            //                            //Прежде чем добавлять полигоны на карту, нужно проверить если полигоны на 180,
//                            // которые надо соеденить между собой
                            formCombinedCustomClickPolygonList(multiPolygon)
                        }
                        separatePolygon180By70Latitude()
                        combinedPolygonsList.forEach {
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
            when {
                polygon.isHave180GeoPoints -> {
                    polygon180LongitudeList.add(polygon)
                }
                else ->{
//                    combinedPolygonsList.add(polygon.toClickPolygon())
                }

            }
        }
    }

    private fun separatePolygon180By70Latitude() {
        val below70List = mutableListOf<PolygonWith180LongitudeInfo>()
        val upper70List = mutableListOf<PolygonWith180LongitudeInfo>()
        polygon180LongitudeList.forEach {
            if (it.lower180LatitudePoint != null && it.lower180LatitudePoint >= 70.0 || it.higher180LatitudePoint != null && it.higher180LatitudePoint >= 70.0) {
                upper70List.add(it)
            }
            if (it.lower180LatitudePoint != null && it.lower180LatitudePoint < 70.0 || it.higher180LatitudePoint != null && it.higher180LatitudePoint < 70.0) {
                below70List.add(it)
            }
        }
//        combinePolygon180(below70List)
        combinePolygon180(upper70List)
    }

    private fun combinePolygon180(polygons: MutableList<PolygonWith180LongitudeInfo>){
        val firstList = polygons[0].actualPoints.filter { it.longitude != 180.0 && it.longitude != -180.0 }.toMutableList()
        val secondList = polygons[1].actualPoints.filter { it.longitude != 180.0 && it.longitude != -180.0 }
        firstList.addAll(secondList)

        val finalList= mutableListOf<GeoPoint>()
        finalList.add(firstList.first())
        for (i in 0..firstList.size){
            val lastPoint = finalList.last()
            var newPoint: GeoPoint? = null
            var distance = Double.MAX_VALUE
            firstList.forEach { geoPoint ->
                if(!finalList.contains(geoPoint)){
                    val d = lastPoint.distanceToAsDouble(geoPoint)
                    if(d < distance) {
                        distance = d
                        newPoint = geoPoint
                    }
                }
            }
            newPoint?.let {finalList.add(it)}
            Log.e("combine", "new point added: ${newPoint?.longitude}/${newPoint?.latitude} ${finalList.size}/${firstList.size}")
        }
        firstList.add(firstList.first())
        combinedPolygonsList.add(CustomClickPolygon(finalList))
    }

}