package com.example.vacaciones.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.vacaciones.viewmodel.VacacionesViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@SuppressLint("MissingPermission")
@Composable
fun PantallaMapa(viewModel: VacacionesViewModel, navController: NavController) {
    val lugar = viewModel.lugarSeleccionado.collectAsState().value
    val context = LocalContext.current

    AndroidView(factory = {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )

        MapView(context).apply {
            setMultiTouchControls(true)
            controller.setZoom(16.0)

            lugar?.latitud?.let { lat ->
                lugar?.longitud?.let { lon ->
                    val geoPoint = GeoPoint(lat, lon)
                    controller.setCenter(geoPoint)

                    val marker = Marker(this)
                    marker.position = geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = lugar.nombre
                    overlays.add(marker)
                }
            }
        }
    })
}