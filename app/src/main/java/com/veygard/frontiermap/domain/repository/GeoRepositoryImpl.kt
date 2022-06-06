package com.veygard.frontiermap.domain.repository

import android.util.Log
import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.presentation.widgets.CustomPolygon
import org.osmdroid.util.GeoPoint


class GeoRepositoryImpl(private val geoApi: GeoApi) : GeoRepository {

    override suspend fun getRussia(): GeoRepResult {
        var result: GeoRepResult

        Log.e("test_di", "repository working")
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
                                    polygon.forEach { point ->
                                        val newPoint = GeoPoint(
                                            point.last(),
                                            if (point.first() > 180.0) 180.0 else point.first()
                                        )
                                        polygonPoints.add(newPoint)
                                    }
                                    val newPolygon = CustomPolygon(polygonPoints)
                                    clusterPerimeterLength += (newPolygon.distance/1000).toInt()
                                    multiPolygon.polygons.add(newPolygon)
                                }
                                listOfMultiPolygon.add(multiPolygon)
                            }
                            listOfGeoClusters.add(GeoCluster(listOfMultiPolygon, clusterPerimeterLength))
                        }

                        GeoRepResult.Success(listOfGeoClusters)
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