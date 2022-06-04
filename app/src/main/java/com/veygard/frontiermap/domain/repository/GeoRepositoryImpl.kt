package com.veygard.frontiermap.domain.repository

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.Result.response
import com.veygard.frontiermap.data.GeoApi
import org.json.JSONObject


class GeoRepositoryImpl(private val geoApi: GeoApi): GeoRepository {
    override suspend fun getRussia(): JSONObject? {
        var geoJson: JSONObject? = null

        Log.e("test_di", "repository working")
        try {
            val call = geoApi.getRussiaApi()
            when {
                call.isSuccessful -> {
                    geoJson = JSONObject(Gson().toJson(call.body()))
                }
                call.code() in 400..499 -> {
                }
                call.code() in 500..599 -> {

                }
                else -> {
                }
            }
        } catch (e: Exception) {
        }
        return geoJson
    }
}