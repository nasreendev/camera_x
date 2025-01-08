package com.test.camera_x.presentation.photo

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.test.camera_x.R
import com.test.camera_x.navigation.Screen
import com.test.camera_x.navigation.localNavHostController
import com.test.camera_x.presentation.camera.CameraViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosListScreen(
    viewModel: CameraViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    Log.d("cvv", "PhotosListScreen: ${state.photos}")
    val navController = localNavHostController.current

    LaunchedEffect(state.photos) {
        viewModel.loadSavedPhotos()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Photos")
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.CAMERA_X_SCREEN.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.photo_camera),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
        ) {
            items(state.photos) { photoUri ->
                ImageCard(photoUri)
            }
        }
    }
}

@Composable
fun ImageCard(photoUri: Uri) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}