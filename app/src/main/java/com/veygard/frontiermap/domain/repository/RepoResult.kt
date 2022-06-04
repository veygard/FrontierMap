package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.domain.models.MultiPolygonRaw

sealed class RepoResult {
    data class Success(val result: List<MultiPolygonRaw>): RepoResult()
    object Error: RepoResult()
    object Connection: RepoResult()
    object Server: RepoResult()
    object Else: RepoResult()
    object Exception: RepoResult()
    object Null: RepoResult()
}