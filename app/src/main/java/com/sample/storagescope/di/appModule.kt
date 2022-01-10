package com.sample.storagescope.di

import com.sample.storagescope.ui.storage_info.StorageViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    viewModel { StorageViewModel(androidApplication()) }
}