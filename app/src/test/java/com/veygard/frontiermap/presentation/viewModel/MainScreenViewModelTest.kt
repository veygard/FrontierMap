package com.veygard.frontiermap.presentation.viewModel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.veygard.frontiermap.MainCoroutineRule
import com.veygard.frontiermap.domain.models.GeoCluster
import com.veygard.frontiermap.domain.repository.GeoRepResult
import com.veygard.frontiermap.domain.repository.GeoRepository
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCase
import com.veygard.frontiermap.domain.use_cases.GetRussiaUseCaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.osmdroid.views.MapView

@ExperimentalCoroutinesApi
class MainScreenViewModelTest {


    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private val getRussiaUseCase = mock<GetRussiaUseCase>()
    private val mapView = mock<MapView>()

    private lateinit var viewModel: MainScreenViewModel

    @BeforeEach
    fun beforeEach(){
        initViewModel()
        setupDispatcher()
        setExecutorDelegate()
    }
    private fun initViewModel() {
        viewModel = MainScreenViewModel(getRussiaUseCase)
    }

    private fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }
    private fun setExecutorDelegate(){
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor(){
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    @AfterEach
    fun afterEach(){
        clearMocks()
        removeExecutorDelegate()
        tearDownDispatcher()
    }
    private fun clearMocks() {
        Mockito.reset(getRussiaUseCase)
    }
    private fun removeExecutorDelegate(){
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    private fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `should return Success state`() = testDispatcher.runBlockingTest {
        val getRussiaTestResult = GeoRepResult.Success(listOf(GeoCluster(emptyList(),0)))
        Mockito.`when`(getRussiaUseCase.start()).thenReturn(getRussiaTestResult)

        viewModel.getRussia(mapView)

        val expected = MainScreenVmState.Success(0)
        val actual = viewModel.state.value

        Mockito.verify(getRussiaUseCase,Mockito.times(1)).start()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return  error state`() = testDispatcher.runBlockingTest {
        val getRussiaTestResult = GeoRepResult.Exception
        Mockito.`when`(getRussiaUseCase.start()).thenReturn(getRussiaTestResult)

        viewModel.getRussia(mapView)

        val expected = MainScreenVmState.Error
        val actual = viewModel.state.value

        Assertions.assertEquals(expected, actual)
    }
}