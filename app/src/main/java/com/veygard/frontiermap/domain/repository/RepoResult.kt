package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.domain.models.GeoCluster

sealed class RepoResult {
    data class Success(val geoClusters: List<GeoCluster>): RepoResult()
    object ConnectionError: RepoResult()
    object ServerError: RepoResult()
    object Else: RepoResult()
    object Exception: RepoResult()
    object Null: RepoResult()
}