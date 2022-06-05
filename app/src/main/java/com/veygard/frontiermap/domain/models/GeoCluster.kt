package com.veygard.frontiermap.domain.models

data  class GeoCluster(val list: List<MultiPolygon>, val perimeterLengthKm: Int)