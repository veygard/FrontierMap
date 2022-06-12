package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.domain.models.PolygonWith180LongitudeInfo
import org.osmdroid.util.GeoPoint


class GeoRepositoryImpl(private val geoApi: GeoApi) : GeoRepository {

    override suspend fun getRussia(): GeoRepResult {
        var result: GeoRepResult

        try {
            val call = geoApi.getRussiaApi()
            result = when {
                call.isSuccessful -> {
                    call.body()?.let { api ->
                        val listOfGeoClusters = mutableListOf<GeoCluster>()

                        api.features.forEach { cluster ->
                            val listOfMultiPolygon = mutableListOf<MultiPolygon>()
                            var clusterPerimeterLength = 0

                            cluster.geometry.coordinates.forEach { multi ->
                                val multiPolygon = MultiPolygon(mutableListOf())
                                multi.forEach { polygon ->
                                    val polygonPoints = mutableListOf<GeoPoint>()
                                    var isHave180GeoPoint = false
                                    var lowerLatitudePoint: Double? = null
                                    var higherLatitudePoint: Double? = null


                                    polygon.forEach { point ->
                                        val newPoint = GeoPoint(
                                            point.last(),
                                            if (point.first() > 180.0) {
                                                180.0
                                            }
                                            else point.first()
                                        )
                                        if (newPoint.longitude >= 180.0 || newPoint.longitude <= -179.0) {
                                            isHave180GeoPoint = true
                                            //запоминаем высшую и низшую точки полигона у точек меридиана 180
                                            if (lowerLatitudePoint == null || (lowerLatitudePoint != null && lowerLatitudePoint!! > newPoint.latitude)) lowerLatitudePoint =
                                                newPoint.latitude
                                            if (higherLatitudePoint == null || (higherLatitudePoint != null && higherLatitudePoint!! < newPoint.latitude)) higherLatitudePoint =
                                                newPoint.latitude
                                        }


                                        polygonPoints.add(newPoint)
                                    }
                                    val newPolygon = PolygonWith180LongitudeInfo(
                                        polygonPoints,
                                        isHave180GeoPoint,
                                        lowerLatitudePoint,
                                        higherLatitudePoint
                                    )
                                    clusterPerimeterLength += (newPolygon.distance / 1000).toInt()
                                    multiPolygon.polygons.add(newPolygon)
                                }
                                listOfMultiPolygon.add(multiPolygon)
                            }
                            listOfGeoClusters.add(
                                GeoCluster(
                                    listOfMultiPolygon,
                                    clusterPerimeterLength
                                )
                            )
                        }

                        if (listOfGeoClusters.isNotEmpty()) GeoRepResult.Success(listOfGeoClusters)
                        else GeoRepResult.Null

                    } ?: GeoRepResult.Null
                }
                call.code() in 400..499 -> {
                    GeoRepResult.ConnectionError
                }
                call.code() in 500..599 -> {
                    GeoRepResult.ServerError
                }
                else -> {
                    GeoRepResult.Else
                }
            }
        } catch (e: Exception) {
            result = GeoRepResult.Exception
        }
        return result
    }
}