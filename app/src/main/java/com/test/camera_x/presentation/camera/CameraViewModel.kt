package com.test.camera_x.presentation.camera

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.camera_x.domain.CameraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class CameraState(
    val photos: List<Uri> = emptyList(),
)

class CameraViewModel(
    private val cameraRepository: CameraRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()


    fun onTakePhoto(
        controller: LifecycleCameraController,
    ) {
        viewModelScope.launch {
            cameraRepository.takePhoto(controller)
        }
    }

    fun loadSavedPhotos() {
        viewModelScope.launch {
            val photosList = cameraRepository.getSavedPhotos()
            _state.update {
                it.copy(
                    photos = photosList
                )
            }
        }
    }
}
