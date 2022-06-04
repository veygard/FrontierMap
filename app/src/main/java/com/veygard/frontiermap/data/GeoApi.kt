package com.veygard.frontiermap.data

import com.veygard.frontiermap.domain.models.GeoJSON
import retrofit2.Response
import retrofit2.http.GET

interface GeoApi {
    @GET("russia.geo.json")
    suspend fun getRussiaApi(): Response<String>
}