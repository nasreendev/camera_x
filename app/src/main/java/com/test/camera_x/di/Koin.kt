package com.test.camera_x.di

import com.test.camera_x.data.CameraRepositoryImpl
import com.test.camera_x.domain.CameraRepository
import com.test.camera_x.presentation.camera.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
    singleOf(::CameraRepositoryImpl){
        bind<CameraRepository>()
    }
}