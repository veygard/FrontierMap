package com.veygard.frontiermap.domain.models

import com.veygard.frontiermap.presentation.widgets.CustomPolygon

data class MultiPolygon(val polygons: MutableList<CustomPolygon>)