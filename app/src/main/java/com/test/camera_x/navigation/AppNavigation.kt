package com.test.camera_x.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.camera_x.presentation.camera.CameraScreen
import com.test.camera_x.presentation.photo.PhotosListScreen

val localNavHostController = compositionLocalOf<NavHostController> {
    error("")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(localNavHostController provides navController) {
        NavHost(navController, Screen.PHOTO_LIST_SCREEN.route) {
            composable(Screen.PHOTO_LIST_SCREEN.route) {
                PhotosListScreen()
            }
            composable(Screen.CAMERA_X_SCREEN.route) {
                CameraScreen()
            }
        }
    }
}

enum class Screen(val route: String) {
    PHOTO_LIST_SCREEN("photo_list_screen"),
    CAMERA_X_SCREEN("camera_x_screen")
}