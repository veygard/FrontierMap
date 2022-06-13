package com.veygard.frontiermap.domain.models

import android.graphics.Color
import org.koin.core.KoinApplication.Companion.init
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

data class PolygonWith180LongitudeInfo(
    val geoPoints: List<GeoPoint>,
    val isHave180GeoPoints: Boolean,
    val lower180LatitudePoint: GeoPoint? = null,
    val higher180LatitudePoint: GeoPoint? = null,
    val average180Latitude: Int? = null,
): Polygon() {
    init {
        this.points = geoPoints
    }
}

