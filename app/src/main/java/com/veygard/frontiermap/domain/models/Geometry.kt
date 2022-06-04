package com.veygard.frontiermap.domain.models

data class Geometry(
    val coordinates: List<List<List<List<Double>>>>,
    val type: String
)