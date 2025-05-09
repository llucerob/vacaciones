package com.example.vacaciones.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun PantallaFotoExpandida(uriString: String?, navController: NavController) {
    val uri = uriString?.let { Uri.parse(it) }

    Box(modifier = Modifier.fillMaxSize()) {
        uri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Foto",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }
    }
}
