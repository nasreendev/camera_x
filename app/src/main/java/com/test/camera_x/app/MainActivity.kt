package com.test.camera_x.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.test.camera_x.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!arePermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, CAMERA_PERMISSION, 100
            )
        }
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }

    fun arePermissionsGranted(): Boolean {
        return CAMERA_PERMISSION.all { perssion ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                perssion
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        val CAMERA_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}