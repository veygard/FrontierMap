package com.veygard.frontiermap.util

import android.util.Log
import com.veygard.frontiermap.domain.models.MultiPolygon
import com.veygard.frontiermap.domain.models.PolygonWith180LongitudeInfo
import com.veygard.frontiermap.presentation.widgets.CustomClickPolygon
import com.veygard.frontiermap.presentation.widgets.toClickPolygon
import com.veygard.frontiermap.util.extensions.equal
import org.osmdroid.util.GeoPoint

object Polygon180Merger {
    private val uncompletedPolygons = mutableListOf<PolygonWith180LongitudeInfo>()
    private val preparedPolygonsList = mutableListOf<CustomClickPolygon>()

    fun getPreparedPolygonList() = preparedPolygonsList

    fun formCombinedCustomClickPolygonList(multiPolygon: MultiPolygon) {
        //полигоны у которых нет 180 точек сразу добавляем в готовый список
        //остальные на обработку

        multiPolygon.polygons.forEach { polygon ->
            when {
                polygon.isHave180GeoPoints -> {
                    uncompletedPolygons.add(polygon)
                }
                else -> {
                    preparedPolygonsList.add(polygon.toClickPolygon())
                }
            }
        }
    }


    fun prepareUncommittedPolygonsByAverageLatitude() {

        //получаем сет средней широты у полигонов
        val setOfLatitudeAverage = mutableSetOf<Int>()
        uncompletedPolygons.forEach {
            it.average180Latitude?.let { setOfLatitudeAverage.add(it)}
        }

        //делаем карту, и добавляем туда пока пустые списки полигонов
        val polygons = mutableMapOf<Int, MutableList<PolygonWith180LongitudeInfo>>()
        setOfLatitudeAverage.forEach{
            polygons[it] = mutableListOf()
        }

        //проходимся по полигонам и добавляем в карту
        uncompletedPolygons.forEach { poly->
            setOfLatitudeAverage.forEach{ average ->
                if (poly.lower180LatitudePoint != null && poly.average180Latitude == average) {
                    polygons[average]?.add(poly)
                }
            }
        }

        //склеиваем полигоны
        polygons.forEach{
            combinePolygon180(it.value)
        }
    }

    private fun combinePolygon180(polygons: MutableList<PolygonWith180LongitudeInfo>) {
        try {
            //определяем в каком из полигонов 180 точки
            val originalList = when {
                polygons[0].actualPoints.any { it.longitude == 180.0 }  -> polygons[0]
                polygons[1].actualPoints.any { it.longitude == 180.0 }  -> polygons[1]
                else -> null
            }

            //список, где точки -180 будем присоединять к оригиналу
            val donorList = when {
                polygons[0].actualPoints.any { it.longitude <= -179.9 }  -> polygons[0]
                polygons[1].actualPoints.any { it.longitude <= -179.9 }  -> polygons[1]
                else -> null
            }

            //удаляем 180 точки кроме верхней и нижней
            val originalNo180PointsList = mutableListOf<GeoPoint>()
            originalList?.actualPoints?.forEach { point ->
                when {
                    point.equal(originalList.higher180LatitudePoint) || point.equal(originalList.lower180LatitudePoint) -> originalNo180PointsList.add(point)
                    point.longitude != 180.0 -> originalNo180PointsList.add(point)
                }
            }

            //определяем индекс верхней точки 180 - туда будем вставлять донора
            val maxLatitudeIndex = originalNo180PointsList.indexOf(originalList?.higher180LatitudePoint)

            //удаляем -180 точки кроме верхней и нижней у донора
            val donorNoMinus180PointsList = mutableListOf<GeoPoint>()
            donorList?.actualPoints?.forEach { point ->
                when {
                    point.equal(donorList.higher180LatitudePoint) || point.equal(donorList.lower180LatitudePoint) -> donorNoMinus180PointsList.add(point)
                    point.longitude >= -179.9 -> donorNoMinus180PointsList.add(point)
                }
            }

            val donorSorted = sortPointsListByFirst(donorNoMinus180PointsList, donorList?.higher180LatitudePoint)
            originalNo180PointsList.addAll(maxLatitudeIndex+1,donorSorted)

            preparedPolygonsList.add(CustomClickPolygon(originalNo180PointsList))
        } catch (e: Exception){
            Log.e("combinePolygon180 exception", "Something wrong with  Polygon180Merger.combinePolygon180: $e")
        }
    }

    private fun sortPointsListByFirst(originalList: List<GeoPoint>, fistPoint: GeoPoint?): List<GeoPoint> {
        //нужно чтобы лист начинался с первой точки, но в том же порядке что и в оригинале
        //выдергиваем точки с индекса первой и вставляем в начало
        return try {
            val firstPointIndex = originalList.indexOf(fistPoint)
            val partOne = originalList.subList(firstPointIndex,originalList.size)
            val partTwo = originalList.subList(0,firstPointIndex)
            partOne + partTwo
        } catch (e : Exception){
            Log.e("sortPointsListByFirst exception", "Something wrong with  Polygon180Merger.sortPointsListByFirst: $e")
            emptyList()
        }
    }
}