package com.veygard.frontiermap.presentation.viewModel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veygard.frontiermap.domain.models.Point
import com.veygard.frontiermap.domain.repository.RepoResult
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.presentation.widgets.CustomTapPolygon
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
            var polyNumber=0
            var pointNumber= 0
            var point: Point = Point(0.0,0.0)

            val result = geoRepository.getRussia()
            when (result) {
                is RepoResult.Success -> {
                    val i=6
                    result.result.forEach { multiPolygon ->
                        try {
                            multiPolygon.polygons.forEachIndexed { indexPoly, poly->
                                polyNumber= indexPoly

                                val geoPoints = ArrayList<GeoPoint>();
                                val polygon = CustomTapPolygon();
                                poly.points.forEachIndexed { indexPoint, points ->
                                    pointNumber= indexPoint
                                    point = points
                                    geoPoints.add(GeoPoint(points.aLatitude,points.aLongitude ))
                                }
                                geoPoints.add(geoPoints[0]);    //forces the loop to close(connect last point to first point)
                                polygon.fillPaint.color = Color.TRANSPARENT //set fill color
                                polygon.points = geoPoints;
                                polygon.outlinePaint.color = Color.BLUE
                                polygon.outlinePaint.strokeWidth = 5f


                                polygon.title = "A sample polygon"
                                map.overlays.add(polygon)
                                Log.e("Polygon added", "poly number $polyNumber")

                            }
                        } catch (e: Exception){
                            Log.e("Point_exception", "Poly:$polyNumber PointNum:$pointNumber, coordinates: ${point.aLatitude} / ${point.aLongitude}")
                            Log.e("Point_exception", "exception $e")
                        }
                    }
                    map.invalidate()
                }
            }
        }
    }
}