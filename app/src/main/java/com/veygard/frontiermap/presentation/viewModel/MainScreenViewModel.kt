package com.veygard.frontiermap.presentation.viewModel

import android.graphics.Color
import android.graphics.Point
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.models.PolygonRaw
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.presentation.widgets.CustomTapPolygon
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class MainScreenViewModel(private val geoRepository: GeoRepository) : ViewModel() {

//    private val perimeterLength = 0.0
//    private var lаstPoint: Point =
    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?>
        get() = _isLoading

    fun getRussia(map: MapView) {
        viewModelScope.launch {
            _isLoading.value = true

            when (val result = geoRepository.getRussia()) {
                is RepoResult.Success -> {
                    result.result.forEach { multiPolygon ->
                        try {
                            multiPolygon.polygonRaws.forEach { raw->
                                map.overlays.add(getNewPolygon(raw))
                                Log.e("Polygon added", "poly number")
                            }
                        } catch (e: Exception){
                            Log.e("Point_exception", "exception $e")
                        }
                    }
                    map.invalidate()
                    _isLoading.value = false
                }
            }
        }
    }

    private fun getNewPolygon(poly: PolygonRaw) : Polygon{
        val geoPoints = ArrayList<GeoPoint>();
        val polygon = CustomTapPolygon();
        poly.pointRaws.forEach{ pointRaw ->
            geoPoints.add(GeoPoint(pointRaw.aLatitude,pointRaw.aLongitude ))
        }
        geoPoints.add(geoPoints[0])
        polygon.fillPaint.color = Color.TRANSPARENT
        polygon.points = geoPoints;
        polygon.outlinePaint.color = Color.BLUE
        polygon.outlinePaint.strokeWidth = 5f
        return polygon
    }
}