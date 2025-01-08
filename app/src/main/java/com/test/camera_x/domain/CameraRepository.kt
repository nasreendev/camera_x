package com.test.camera_x.domain

import android.net.Uri
import androidx.camera.view.LifecycleCameraController

interface CameraRepository {
    suspend fun takePhoto(
        controller: LifecycleCameraController
    )
    suspend fun getSavedPhotos():List<Uri>
}