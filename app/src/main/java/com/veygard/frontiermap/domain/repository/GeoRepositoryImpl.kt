package com.veygard.frontiermap.domain.repository

import android.graphics.Color
import android.location.Location
import android.util.Log
import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.presentation.widgets.CustomPolygon
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
                        var perimeterLength = 0f
                        var perimeterLengthAuto = 0.0
                        var lastPoint: GeoPoint? = null
                        val polygonList = mutableListOf<Polygon>()

                        api.features.forEach { cluster->
                            val listOfMultiPolygon = mutableListOf<MultiPolygon>()
                            cluster.geometry.coordinates.forEach { multi->
                                val multiPolygon = MultiPolygon(mutableListOf())
                                multi.forEach { polygon ->
                                    var polygonDistance = 0f
                                    val polygonPoints = mutableListOf<GeoPoint>()
                                    polygon.forEach { point->
                                        val newPoint = GeoPoint(point.last(), if(point.first() > 180.0) 180.0 else point.first())

                                        lastPoint?.let {
                                                perimeterLength += calculateDistance(newPoint,lastPoint!!)
                                                polygonDistance += calculateDistance(newPoint,lastPoint!!)
                                            lastPoint= newPoint
                                        } ?: kotlin.run {
                                            lastPoint= newPoint
                                        }

                                        polygonPoints.add(newPoint)
                                    }
                                    val newPolygon =  CustomPolygon(polygonPoints)
                                    multiPolygon.polygons.add(newPolygon)
                                    if(!polygonList.contains(newPolygon))perimeterLengthAuto += newPolygon.distance / 1000
                                    else Log.e("Polygon_error", "Polygon same")

                                    polygonList.add(newPolygon)
                                }
                                listOfMultiPolygon.add(multiPolygon)
                            }
                            listOfGeoClusters.add(GeoCluster(listOfMultiPolygon, perimeterLength))
                            Log.e("Polygon", "perimeter: $perimeterLengthAuto")
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


    private fun calculateDistance(newPoint: GeoPoint, lastPoint: GeoPoint): Float {
        val newLocation = Location("")
        newLocation.latitude = newPoint.latitude
        newLocation.longitude = newPoint.longitude
        val lastLocation = Location("")
        lastLocation.latitude = lastPoint.latitude
        lastLocation.longitude = lastPoint.longitude

        return newLocation.distanceTo(lastLocation) / 1000
    }
}