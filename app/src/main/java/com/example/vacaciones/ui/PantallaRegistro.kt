package com.example.vacaciones.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.vacaciones.viewmodel.VacacionesViewModel

@Composable
fun PantallaRegistro(
    viewModel: VacacionesViewModel = viewModel(),
    onAbrirCamara: () -> Unit,
    onAbrirMapa: () -> Unit,
    onVerFotoCompleta: (Uri) -> Unit
) {
    val estado by viewModel.lugarVisita.collectAsState()
    var nombreLugar by remember { mutableStateOf(estado.nombre) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Registro de Vacaciones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = nombreLugar,
            onValueChange = {
                nombreLugar = it
                viewModel.actualizarNombre(it)
            },
            label = { Text("Nombre del lugar") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onAbrirCamara,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar Foto")
        }

        if (estado.fotos.isNotEmpty()) {
            Text("Fotos capturadas:")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(estado.fotos) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { onVerFotoCompleta(uri) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onAbrirMapa,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver ubicación en mapa")
        }
    }
}