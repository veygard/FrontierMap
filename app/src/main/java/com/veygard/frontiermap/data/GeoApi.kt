package com.veygard.frontiermap.data

import android.util.Log
import retrofit2.http.GET

interface GeoApi {
    @GET("russia.geo.json")
    suspend fun getRussiaApi(){}
}