package com.veygard.frontiermap.di

import com.veygard.frontiermap.presentation.viewModel.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module{
    viewModelOf(::MainScreenViewModel)
}