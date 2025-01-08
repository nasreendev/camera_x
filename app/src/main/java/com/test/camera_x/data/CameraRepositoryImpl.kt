package com.test.camera_x.data

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import com.test.camera_x.R
import com.test.camera_x.domain.CameraRepository

class CameraRepositoryImpl(
    private val application: Application,
) : CameraRepository {

    override suspend fun takePhoto(
        controller: LifecycleCameraController,
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(application),
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("NewApi")
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(
                            image.imageInfo.rotationDegrees.toFloat()
                        )
                    }

                    val imageBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0, 0,
                        image.width, image.height,
                        matrix, true
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        savePhoto(imageBitmap)
                    }
                    Toast.makeText(application, "Photo captured Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun savePhoto(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val resolver: ContentResolver = application.contentResolver

            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )

            val appName = application.getString(R.string.app_name)
            val timeInMillis = System.currentTimeMillis()

            val imageContentValues: ContentValues = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "${timeInMillis}_image" + ".jpg"
                )
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/$appName"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.DATE_TAKEN, timeInMillis)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            val imageMediaStoreUri: Uri? = resolver.insert(
                imageCollection, imageContentValues
            )

            imageMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.let { outputStream ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG, 100, outputStream
                        )
                    }

                    imageContentValues.clear()
                    imageContentValues.put(
                        MediaStore.MediaColumns.IS_PENDING, 0
                    )
                    resolver.update(
                        uri, imageContentValues, null, null
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }
    override suspend fun getSavedPhotos(): List<Uri> {
        val resolver: ContentResolver = application.contentResolver
        val images = mutableListOf<Uri>()

        val collectionUri =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val cursor = resolver.query(
            collectionUri,
            arrayOf(MediaStore.Images.Media._ID),
            null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val uri = ContentUris.withAppendedId(collectionUri, id)
                images.add(uri)
            }
        }
        return images
    }
}
