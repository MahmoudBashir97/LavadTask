package com.mahmoudbashir.lavadtask.koinDI

import com.mahmoudbashir.lavadtask.repository.NewsRepository
import com.mahmoudbashir.lavadtask.retrofit.RetrofitInstance
import com.mahmoudbashir.lavadtask.viewModel.NewsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsApi = module {

    single { RetrofitInstance() }
    single { NewsRepository(get()) }


}

val mainViewModel = module {
    viewModel {
        NewsViewModel(androidApplication(),get())
    }
}