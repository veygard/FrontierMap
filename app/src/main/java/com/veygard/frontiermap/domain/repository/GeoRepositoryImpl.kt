package com.veygard.frontiermap.domain.repository

import android.graphics.Color
import android.util.Log
import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.presentation.widgets.CustomTapPolygon
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon


class GeoRepositoryImpl(private val geoApi: GeoApi): GeoRepository {
    override suspend fun getRussia(): RepoResult {
        var result: RepoResult

        Log.e("test_di", "repository working")
        try {
            val call = geoApi.getRussiaApi()
            result = when {
                call.isSuccessful -> {
                    call.body()?.let { api->
                        val listOfGeoClusters = mutableListOf<GeoCluster>()


                        api.features.forEach { cluster->
                            val listOfMultiPolygon = mutableListOf<MultiPolygon>()
                            cluster.geometry.coordinates.forEach { multi->
                                val multiPolygon = MultiPolygon(mutableListOf())
                                multi.forEach { polygon ->
                                    val listOfPoints = mutableListOf<GeoPoint>()
                                    polygon.forEach { point->
                                        val reversePoint = GeoPoint(point.last(), if(point.first() > 180.0) 180.0 else point.first())
                                        listOfPoints.add(reversePoint)
                                    }
                                    val newPolygon = getNewPolygon(listOfPoints)
                                    multiPolygon.polygons.add(newPolygon)
                                }
                                listOfMultiPolygon.add(multiPolygon)
                            }
                            listOfGeoClusters.add(GeoCluster(listOfMultiPolygon))
                        }

                        RepoResult.Success(listOfGeoClusters)
                    } ?: RepoResult.Null
                }
                call.code() in 400..499 -> {
                    RepoResult.Connection
                }
                call.code() in 500..599 -> {
                    RepoResult.Server
                }
                else -> {
                    RepoResult.Else
                }
            }
        } catch (e: Exception) {
            result= RepoResult.Exception
        }
        return result
    }

    private fun getNewPolygon(points: MutableList<GeoPoint>): Polygon {
        val geoPoints = ArrayList<GeoPoint>()
        val polygon = CustomTapPolygon()
        points.forEach { geoPoint ->
            geoPoints.add(geoPoint)
        }
        geoPoints.add(geoPoints[0])
        polygon.fillPaint.color = Color.TRANSPARENT
        polygon.points = geoPoints
        polygon.outlinePaint.color = Color.BLUE
        polygon.outlinePaint.strokeWidth = 5f
        return polygon
    }
}