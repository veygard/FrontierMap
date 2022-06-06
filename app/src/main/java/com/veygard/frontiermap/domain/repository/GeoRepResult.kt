package com.veygard.frontiermap.domain.repository

import com.veygard.frontiermap.domain.models.GeoCluster

sealed class GeoRepResult {
    data class Success(val geoClusters: List<GeoCluster>): GeoRepResult()
    object ConnectionError: GeoRepResult()
    object ServerError: GeoRepResult()
    object Else: GeoRepResult()
    object Exception: GeoRepResult()
    object Null: GeoRepResult()
}