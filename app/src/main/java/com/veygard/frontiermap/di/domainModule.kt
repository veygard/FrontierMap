package com.veygard.frontiermap.di

import com.veygard.frontiermap.data.GeoApi
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.repository.GeoRepositoryImpl
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import org.koin.dsl.module

val domainModule = module{
        single { createGeoRepository(get()) }
        single { createGetRussiaUseCase(get()) }
}

fun createGeoRepository(geoApi: GeoApi): GeoRepository = GeoRepositoryImpl(geoApi)
fun createGetRussiaUseCase(geoRepository: GeoRepository) = GetRussiaUseCase(geoRepository)