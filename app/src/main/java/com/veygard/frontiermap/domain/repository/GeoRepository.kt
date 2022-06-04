package com.veygard.frontiermap.domain.repository

import org.json.JSONObject

interface GeoRepository {
    suspend fun getRussia(): JSONObject?
}