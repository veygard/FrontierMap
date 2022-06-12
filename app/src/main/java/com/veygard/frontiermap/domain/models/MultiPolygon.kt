package com.veygard.frontiermap.domain.models


data class MultiPolygon(val polygons: MutableList<PolygonWith180LongitudeInfo>)