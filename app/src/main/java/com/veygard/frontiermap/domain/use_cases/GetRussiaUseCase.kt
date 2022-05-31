package com.veygard.frontiermap.domain.use_cases

import com.veygard.frontiermap.domain.repository.GeoRepository

class GetRussiaUseCase(private val geoRepository: GeoRepository) {
    suspend fun start() = geoRepository.getRussia()
}