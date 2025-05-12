package com.example.vacaciones.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vacaciones.viewmodel.VacacionesViewModel
import com.example.vacaciones.R

@Composable
fun PantallaDetalleLugar(
    viewModel: VacacionesViewModel,
    onEditar: () -> Unit,
    onVolver: () -> Unit
) {
    val lugar = viewModel.lugarSeleccionado.collectAsState().value

    if (lugar == null) {
        Text(stringResource(id = R.string.ver_detalle))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = lugar.nombre, style = MaterialTheme.typography.headlineSmall)

        Image(
            painter = rememberAsyncImagePainter(lugar.urlImagen),
            contentDescription = stringResource(id = R.string.url_imagen),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        lugar.uriFoto?.let { uriStr ->
            val uri = Uri.parse(uriStr)
            Text(stringResource(id = R.string.tomar_foto))
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Text("${stringResource(R.string.orden)}: ${lugar.orden}")
        Text("Coordenadas: ${lugar.latitud}, ${lugar.longitud}")
        Text("${stringResource(R.string.costo_alojamiento)}: \$${lugar.costoAlojamientoCLP}")
        Text("${stringResource(R.string.costo_transporte)}: \$${lugar.costoTransporteCLP}")
        Text("${stringResource(R.string.comentarios)}: ${lugar.comentarios}")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onEditar) {
                Text(stringResource(R.string.editar))
            }
            Button(
                onClick = {
                    viewModel.eliminar(lugar)
                    onVolver()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.eliminar))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.volver))
        }
    }
}