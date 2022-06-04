package com.veygard.frontiermap.data.model

data class GeoApiResponse(
    val features: List<Feature>,
    val type: String
)