package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.data.model.GeoApiResponse
import com.veygard.frontiermap.domain.models.MultiPolygon

sealed class RepoResult {
    data class Success(val result: List<MultiPolygon>): RepoResult()
    object Error: RepoResult()
    object Connection: RepoResult()
    object Server: RepoResult()
    object Else: RepoResult()
    object Exception: RepoResult()
    object Null: RepoResult()
}