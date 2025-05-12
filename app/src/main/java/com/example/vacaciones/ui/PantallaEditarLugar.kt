package com.example.vacaciones.ui

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vacaciones.model.Lugar
import com.example.vacaciones.viewmodel.VacacionesViewModel

@Composable
fun PantallaEditarLugar(
    viewModel: VacacionesViewModel,
    onGuardar: () -> Unit
) {
    val lugar = viewModel.lugarSeleccionado.collectAsState().value
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(lugar?.nombre.orEmpty()) }
    var urlImagen by remember { mutableStateOf(lugar?.urlImagen.orEmpty()) }
    var latitud by remember { mutableStateOf(lugar?.latitud?.toString().orEmpty()) }
    var longitud by remember { mutableStateOf(lugar?.longitud?.toString().orEmpty()) }
    var orden by remember { mutableStateOf(lugar?.orden?.toString().orEmpty()) }
    var alojamiento by remember { mutableStateOf(lugar?.costoAlojamientoCLP?.toString().orEmpty()) }
    var transporte by remember { mutableStateOf(lugar?.costoTransporteCLP?.toString().orEmpty()) }
    var comentarios by remember { mutableStateOf(lugar?.comentarios.orEmpty()) }
    var uriFoto by remember { mutableStateOf(lugar?.uriFoto) }

    var nuevaFotoUri by remember { mutableStateOf<Uri?>(null) }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fueTomada ->
        if (fueTomada) {
            uriFoto = nuevaFotoUri.toString()
        }
    }

    fun lanzarCamara() {
        val nombreFoto = "foto_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, nombreFoto)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Vacaciones")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        nuevaFotoUri = uri
        uri?.let { camaraLauncher.launch(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = urlImagen, onValueChange = { urlImagen = it }, label = { Text("URL Imagen") })
        OutlinedTextField(value = latitud, onValueChange = { latitud = it }, label = { Text("Latitud") })
        OutlinedTextField(value = longitud, onValueChange = { longitud = it }, label = { Text("Longitud") })
        OutlinedTextField(value = orden, onValueChange = { orden = it }, label = { Text("Orden") })
        OutlinedTextField(value = alojamiento, onValueChange = { alojamiento = it }, label = { Text("Costo Alojamiento (CLP)") })
        OutlinedTextField(value = transporte, onValueChange = { transporte = it }, label = { Text("Costo Transporte (CLP)") })
        OutlinedTextField(value = comentarios, onValueChange = { comentarios = it }, label = { Text("Comentarios") })

        Button(onClick = { lanzarCamara() }) {
            Text("Tomar Foto")
        }

        uriFoto?.let {
            Text("Foto actual:")
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(it)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val nuevoLugar = Lugar(
                    id = lugar?.id ?: 0,
                    nombre = nombre,
                    urlImagen = urlImagen,
                    latitud = latitud.toDoubleOrNull() ?: 0.0,
                    longitud = longitud.toDoubleOrNull() ?: 0.0,
                    orden = orden.toIntOrNull() ?: 0,
                    costoAlojamientoCLP = alojamiento.toIntOrNull() ?: 0,
                    costoTransporteCLP = transporte.toIntOrNull() ?: 0,
                    comentarios = comentarios,
                    uriFoto = uriFoto
                )

                if (lugar == null) {
                    viewModel.insertar(nuevoLugar)
                } else {
                    viewModel.actualizar(nuevoLugar)
                }

                onGuardar()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}