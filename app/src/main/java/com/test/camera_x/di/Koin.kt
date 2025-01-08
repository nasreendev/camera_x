package com.test.camera_x.di

import android.app.Application
import com.test.camera_x.data.CameraRepositoryImpl
import org.koin.android.ext.koin.androidContext
import com.test.camera_x.domain.CameraRepository
import com.test.camera_x.presentation.camera.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
    factory<CameraRepository> {
        CameraRepositoryImpl(androidContext() as Application)
    }
}