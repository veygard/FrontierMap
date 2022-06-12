package com.veygard.frontiermap.presentation.viewModel

import org.osmdroid.util.GeoPoint
import kotlin.math.*

class SortGeoPoint() : Comparator<GeoPoint> {

    override fun compare(p0: GeoPoint, p1: GeoPoint): Int {
//        val lat1 = p0?.latitude  ?: 0.0
//        val lon1 = p0?.longitude  ?: 0.0
//        val lat2 = p1?.latitude  ?: 0.0
//        val lon2 = p1?.longitude  ?: 0.0


//        val distanceToGeoPoint1 =
//            distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1)
//        val distanceToGeoPoint2 =
//            distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2)
        return (p0.distanceToAsDouble(p1)).toInt()
    }

    private fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * asin(
            sqrt(
                sin(deltaLat / 2).pow(2.0) +
                        cos(fromLat) * cos(toLat) *
                        sin(deltaLon / 2).pow(2.0)
            )
        )
        return radius * angle
    }
}