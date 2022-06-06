package com.veygard.frontiermap.presentation.viewModel

sealed class MainScreenVmState {
    object Loading: MainScreenVmState()
    object StopLoading: MainScreenVmState()
    object Error: MainScreenVmState()
    data class Success(val clusterPerimeter: Int? = null): MainScreenVmState()
}