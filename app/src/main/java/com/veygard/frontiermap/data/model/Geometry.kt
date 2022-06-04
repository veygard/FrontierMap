package com.veygard.frontiermap.data.model

data class Geometry(
    val coordinates: List<List<List<List<Double>>>>,
    val type: String
)