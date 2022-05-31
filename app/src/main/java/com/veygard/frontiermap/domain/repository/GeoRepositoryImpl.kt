package com.veygard.frontiermap.domain.repository

import android.util.Log
import com.veygard.frontiermap.data.GeoApi

class GeoRepositoryImpl(private val geoApi: GeoApi): GeoRepository {
    override suspend fun getRussia() {
        Log.e("test_di", "repository working")
        geoApi.getRussiaApi()
    }
}