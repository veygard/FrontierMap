package com.veygard.frontiermap.presentation.viewModel

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.domain.repository.GeoRepository
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

class MainScreenViewModel(private val geoRepository: GeoRepository) : ViewModel() {

    fun getRussia(map: MapView) {
        viewModelScope.launch {
            val result = geoRepository.getRussia()
            val i = 6
            when (result) {
                is RepoResult.Success -> {

                    result.result.forEach { multiPolygon ->
                        try {
                            multiPolygon.polygons[0].let { poly ->
                                val geoPoints = ArrayList<GeoPoint>();
                                val polygon = Polygon();
                                poly.points.forEach { points ->
                                    geoPoints.add(GeoPoint(points.aLatitude,points.aLongitude ))
                                }
                                geoPoints.add(geoPoints.get(0));    //forces the loop to close(connect last point to first point)
                                polygon.fillPaint.color = Color.TRANSPARENT //set fill color
                                polygon.points = geoPoints;
                                polygon.title = "A sample polygon"
                                map.overlays.add(polygon)

                                val newPoint =  GeoPoint(geoPoints.first().latitude, geoPoints.first().longitude)
                                map.controller.setCenter(newPoint)
                            }
                        } catch (e: Exception){

                        }
                    }

                }
            }
        }
    }
}