package com.veygard.frontiermap.presentation.viewModel

import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.models.PointRaw
import com.veygard.frontiermap.domain.models.PolygonRaw
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.presentation.widgets.CustomTapPolygon
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class MainScreenViewModel(private val geoRepository: GeoRepository) : ViewModel() {

    private var perimeterLength: Double = 0.0
    private var perimeterLengthNew: Double = 0.0
    private var lastLocation: Location? = null

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
                            multiPolygon.polygonRaws.forEach { raw ->
                                map.overlays.add(getNewPolygon(raw))
                                Log.e("Polygon added", "poly number")
                            }
                        } catch (e: Exception) {
                            Log.e("Point_exception", "exception $e")
                        }
                    }
                    map.invalidate()
                    _isLoading.value = false
                }
            }
        }
    }

    private fun getNewPolygon(poly: PolygonRaw): Polygon {
        val geoPoints = ArrayList<GeoPoint>();
        val polygon = CustomTapPolygon();
        poly.pointRaws.forEach { pointRaw ->
            addPerimeterDistance(pointRaw)
            geoPoints.add(GeoPoint(pointRaw.aLatitude, pointRaw.aLongitude))

        }
//        geoPoints.add(geoPoints[0])
        polygon.fillPaint.color = Color.TRANSPARENT
        polygon.points = geoPoints;
        polygon.outlinePaint.color = Color.BLUE
        polygon.outlinePaint.strokeWidth = 5f
        return polygon
    }

    private fun addPerimeterDistance(pointRaw: PointRaw) {
        val newLocation = Location("")
        newLocation.latitude = pointRaw.aLatitude
        newLocation.longitude = pointRaw.aLongitude
        lastLocation?.let { last ->
            perimeterLength += newLocation.distanceTo(lastLocation)/1000
            perimeterLengthNew  += distFrom(newLocation.latitude.toFloat(), newLocation.longitude.toFloat(), last.latitude.toFloat(), last.longitude.toFloat() )/1000
            lastLocation= newLocation
            val i =6
        } ?: run { lastLocation = newLocation }

    }

    fun distFrom(lat1: Float, lng1: Float, lat2: Float, lng2: Float): Float {
        val earthRadius = 3958.75
        val dLat = Math.toRadians((lat2 - lat1).toDouble())
        val dLng = Math.toRadians((lng2 - lng1).toDouble())
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(Math.toRadians(lat2.toDouble())) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val dist = earthRadius * c
        val meterConversion = 1609
        return (dist * meterConversion.toFloat()).toFloat()
    }
}