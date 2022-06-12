package com.veygard.frontiermap.domain.models

import android.graphics.Color
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

data class PolygonWith180LongitudeInfo(
    val geoPoints: List<GeoPoint>,
    val isHave180GeoPoints: Boolean,
    val lowerLatitudePoint: Double? = null,
    val higherLatitudePoint: Double? = null
): Polygon() {
    init {
        this.points = geoPoints
    }
}

