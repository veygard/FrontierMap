package com.veygard.frontiermap.presentation.widgets

import android.graphics.Color
import android.view.MotionEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomPolygon(geoPoints:List<GeoPoint>) : Polygon() {
    init {
        points = geoPoints
        title = "${distance / 1000} km"
        this.
        subDescription = "${distance / 1000} km"
        snippet = "${distance / 1000} km"

        fillPaint.color = Color.TRANSPARENT
        this.points = geoPoints
        outlinePaint.color = Color.BLUE
        outlinePaint.strokeWidth = 5f
    }
    private var isChecked = false

    override fun onSingleTapUp(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e?.action == MotionEvent.ACTION_UP && contains(e) && !isChecked) {
            isChecked = true
            this.fillPaint.color = Color.BLUE
            this.fillPaint.alpha = 40
            return true;
        }
        if (e?.action == MotionEvent.ACTION_UP && contains(e) && isChecked) {
            this.fillPaint.color = Color.TRANSPARENT
            this.fillPaint.alpha = 0
            isChecked = false
            return true;
        }
        return super.onSingleTapUp(e, mapView)
    }
}