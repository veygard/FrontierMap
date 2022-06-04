package com.veygard.frontiermap.domain.repository

import android.util.Log
import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.data.model.GeoApiResponse
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.domain.models.Point
import com.veygard.frontiermap.domain.models.Polygon


class GeoRepositoryImpl(private val geoApi: GeoApi): GeoRepository {
    override suspend fun getRussia(): RepoResult {
        var result: RepoResult

        Log.e("test_di", "repository working")
        try {
            val call = geoApi.getRussiaApi()
            result = when {
                call.isSuccessful -> {
                    call.body()?.let { api->
                        val listOfMultiPolygon = mutableListOf<MultiPolygon>()

                        api.features.forEach { cluster->
                            cluster.geometry.coordinates.forEach { multi->
                                val multiPolygon = MultiPolygon(mutableListOf())
                                multi.forEach { polygon ->
                                    val listOfPoints = mutableListOf<Point>()
                                    polygon.forEach { point->
                                        val reversePoint = Point(point.last(), if(point.first() > 180.0) 180.0 else point.first())
                                        listOfPoints.add(reversePoint)
                                    }
                                    val newPolygon = Polygon(listOfPoints)
                                    multiPolygon.polygons.add(newPolygon)
                                }
                                listOfMultiPolygon.add(multiPolygon)
                            }
                        }


                        RepoResult.Success(listOfMultiPolygon)
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
}