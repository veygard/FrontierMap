package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.domain.models.GeoCluster

sealed class RepoResult {
    data class Success(val geoClusters: List<GeoCluster>): RepoResult()
    object Error: RepoResult()
    object Connection: RepoResult()
    object Server: RepoResult()
    object Else: RepoResult()
    object Exception: RepoResult()
    object Null: RepoResult()
}