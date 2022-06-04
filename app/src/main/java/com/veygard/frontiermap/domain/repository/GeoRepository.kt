package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.data.model.GeoApiResponse

interface GeoRepository {
    suspend fun getRussia(): RepoResult
}