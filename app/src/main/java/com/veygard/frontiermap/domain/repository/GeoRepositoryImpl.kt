package com.veygard.frontiermap.domain.repository

import android.util.Log
import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.models.MultiPolygonRaw
import com.veygard.frontiermap.domain.models.PointRaw
import com.veygard.frontiermap.domain.models.PolygonRaw


class GeoRepositoryImpl(private val geoApi: GeoApi): GeoRepository {
    override suspend fun getRussia(): RepoResult {
        var result: RepoResult

        Log.e("test_di", "repository working")
        try {
            val call = geoApi.getRussiaApi()
            result = when {
                call.isSuccessful -> {
                    call.body()?.let { api->
                        val listOfMultiPolygon = mutableListOf<MultiPolygonRaw>()

                        api.features.forEach { cluster->
                            cluster.geometry.coordinates.forEach { multi->
                                val multiPolygon = MultiPolygonRaw(mutableListOf())
                                multi.forEach { polygon ->
                                    val listOfPoints = mutableListOf<PointRaw>()
                                    polygon.forEach { point->
                                        val reversePoint = PointRaw(if(point.first() > 180.0) 180.0 else point.first(), point.last())
                                        listOfPoints.add(reversePoint)
                                    }
                                    val newPolygon = PolygonRaw(listOfPoints)
                                    multiPolygon.polygonRaws.add(newPolygon)
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