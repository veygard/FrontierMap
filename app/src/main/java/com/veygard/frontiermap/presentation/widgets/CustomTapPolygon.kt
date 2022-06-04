package com.veygard.frontiermap.presentation.widgets

import android.graphics.Color
import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class CustomTapPolygon : Polygon() {
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