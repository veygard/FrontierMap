package com.veygard.frontiermap.data.model

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)