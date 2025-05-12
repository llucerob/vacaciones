package com.example.vacaciones.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vacaciones.model.Lugar
import com.example.vacaciones.viewmodel.VacacionesViewModel
import kotlinx.coroutines.launch
import com.example.vacaciones.api.TipoCambioApi

@Composable
fun PantallaListadoLugares(
    viewModel: VacacionesViewModel,
    onVerDetalle: (Int) -> Unit,
    onAgregar: () -> Unit
) {
    val lugares by viewModel.lugares.collectAsState()
    var cambioUSD by remember { mutableStateOf(1.0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        cambioUSD = TipoCambioApi.obtenerCambioCLPtoUSD()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(lugares) { lugar ->
                LugarItem(lugar, cambioUSD, onVerDetalle)
            }
        }
    }
}

@Composable
fun LugarItem(lugar: Lugar, cambioUSD: Double, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(lugar.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(lugar.urlImagen),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp)
            )

            Column {
                Text(lugar.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Alojamiento: \$${lugar.costoAlojamientoCLP} CLP | \$${(lugar.costoAlojamientoCLP / cambioUSD).toInt()} USD")
                Text("Transporte: \$${lugar.costoTransporteCLP} CLP | \$${(lugar.costoTransporteCLP / cambioUSD).toInt()} USD")
            }
        }
    }
}
