package com.veygard.frontiermap.domain.use_cases

import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.repository.GeoRepResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetRussiaUseCaseTest {
    private val geoRepository = mock<GeoRepository>()

    @AfterEach
    fun clearMocks(){
        Mockito.reset(geoRepository)
    }

    @Test
    fun `should return GeoRepResult ServerError status`() = runBlocking{
        Mockito.`when`(geoRepository.getRussia()).thenReturn(GeoRepResult.ServerError)

        val useCase = GetRussiaUseCase(geoRepository = geoRepository )
        val actual = useCase.start()
        val expected = GeoRepResult.ServerError

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return GeoRepResult Success status`() = runBlocking{
        val emptyGeoClusters = emptyList<GeoCluster>()
        Mockito.`when`(geoRepository.getRussia()).thenReturn(GeoRepResult.Success(emptyGeoClusters))

        val useCase = GetRussiaUseCase(geoRepository = geoRepository )
        val actual = useCase.start()
        val expected = GeoRepResult.Success(emptyGeoClusters)

        Assertions.assertEquals(expected, actual)
    }
}