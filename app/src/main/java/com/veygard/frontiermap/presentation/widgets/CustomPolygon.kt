package com.veygard.frontiermap.presentation.widgets

import android.graphics.Color
import android.view.MotionEvent
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon


class CustomPolygon(geoPoints: List<GeoPoint>) : Polygon() {
    private var perimeterMarker: Marker? = null

    init {
        points = geoPoints
        title = "${distance / 1000} km"


        subDescription = "${distance / 1000} km"
        snippet = "${distance / 1000} km"

        fillPaint.color = Color.TRANSPARENT
        this.points = geoPoints
        outlinePaint.color = Color.BLUE
        outlinePaint.strokeWidth = 5f
    }

    var isChecked = false

    private fun showPolygonPerimeterMarkers(polygon: CustomPolygon, map: MapView) {
        perimeterMarker = Marker(map)
        perimeterMarker!!.apply {
            textLabelBackgroundColor = Color.TRANSPARENT
            textLabelForegroundColor = Color.BLUE
            setTextIcon("${(polygon.distance / 1000).toInt()} km")
            position = calculateCenter(polygon.actualPoints)
        }
    }

    override fun onSingleTapUp(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e?.action == MotionEvent.ACTION_UP && contains(e) && !isChecked) {
            isChecked = true
            this.fillPaint.color = Color.BLUE
            this.fillPaint.alpha = 40

            mapView?.let{ showPolygonPerimeterMarkers(this, it)}
            perimeterMarker?.let {  mapView?.overlayManager?.add(it) }

            return true
        }
        if (e?.action == MotionEvent.ACTION_UP && contains(e) && isChecked) {
            this.fillPaint.color = Color.TRANSPARENT
            this.fillPaint.alpha = 0
            perimeterMarker?.textLabelForegroundColor = Color.TRANSPARENT
            isChecked = false
            perimeterMarker?.let {  mapView?.overlayManager?.remove(it) }
            return true
        }
        return super.onSingleTapUp(e, mapView)
    }

    private fun calculateCenter(points: List<GeoPoint>): GeoPoint {

        val longitudes = points.map { it.longitude }.toMutableList()
        val latitudes = points.map { it.latitude }.toMutableList()

        latitudes.sort()
        longitudes.sort()

        val lowX = latitudes.first()
        val highX = latitudes.last()
        val lowy = longitudes.first()
        val highy = longitudes.last()

        val centerX = lowX + ((highX - lowX) / 2)
        val centerY = lowy + ((highy - lowy) / 2)

        return GeoPoint(centerX, centerY);
    }
}