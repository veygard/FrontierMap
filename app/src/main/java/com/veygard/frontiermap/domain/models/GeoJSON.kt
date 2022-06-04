package com.veygard.frontiermap.domain.models

data class GeoJSON(
    val features: List<Feature>,
    val type: String
)