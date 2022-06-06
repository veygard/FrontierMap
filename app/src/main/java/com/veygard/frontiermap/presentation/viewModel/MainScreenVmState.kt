package com.veygard.frontiermap.presentation.viewModel

sealed class MainScreenVmState {
    object Loading: MainScreenVmState()
    object StopLoading: MainScreenVmState()
    object ConnectionError: MainScreenVmState()
    object ServerError: MainScreenVmState()
    data class Success(val clusterPerimeter: Int? = null): MainScreenVmState()
}