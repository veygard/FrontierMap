package com.veygard.frontiermap.domain.models

import org.osmdroid.views.overlay.Polygon

data class MultiPolygon(val polygons: MutableList<Polygon>)