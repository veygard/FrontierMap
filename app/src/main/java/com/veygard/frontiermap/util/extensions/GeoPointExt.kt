package com.veygard.frontiermap.util.extensions

import org.osmdroid.util.GeoPoint


fun GeoPoint.equal(second: GeoPoint?) = this.latitude == second?.latitude && this.longitude == second.longitude
