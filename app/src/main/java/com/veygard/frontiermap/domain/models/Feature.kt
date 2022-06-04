package com.veygard.frontiermap.domain.models

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)