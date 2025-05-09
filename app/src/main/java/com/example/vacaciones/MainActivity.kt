package com.example.vacaciones

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import com.example.vacaciones.ui.PantallaFotoExpandida
import com.example.vacaciones.ui.PantallaMapa
import com.example.vacaciones.ui.PantallaRegistro
import com.example.vacaciones.viewmodel.VacacionesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    private var currentPhotoUri: Uri? = null
    private lateinit var viewModel: VacacionesViewModel
    private lateinit var lanzarCamaraLauncher: ActivityResultLauncher<Uri>
    private lateinit var solicitarPermisos: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = VacacionesViewModel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val permisos = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        solicitarPermisos = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permisosOtorgados ->
            val todosOtorgados = permisosOtorgados.all { it.value }
            if (todosOtorgados) {
                lanzarCamara()
            }
        }

        lanzarCamaraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { fueGuardada ->
            if (fueGuardada) {
                currentPhotoUri?.let {
                    viewModel.agregarFoto(it)
                }
            }
        }

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            NavHost(navController = navController, startDestination = "registro") {
                composable("registro") {
                    PantallaRegistro(
                        viewModel = viewModel,
                        onAbrirCamara = {
                            val permisosFaltantes = permisos.filter {
                                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                            }

                            if (permisosFaltantes.isEmpty()) {
                                lanzarCamara()
                            } else {
                                solicitarPermisos.launch(permisosFaltantes.toTypedArray())
                            }
                        },
                        onAbrirMapa = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    location?.let {
                                        viewModel.actualizarUbicacion(it.latitude, it.longitude)
                                        navController.navigate("mapa")
                                    }
                                }
                            } else {
                                solicitarPermisos.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                            }
                        },
                        onVerFotoCompleta = { uri ->
                            navController.navigate("verFoto?uri=${Uri.encode(uri.toString())}")
                        }
                    )
                }
                composable(
                    "verFoto?uri={uri}",
                    arguments = listOf(navArgument("uri") { nullable = true })
                ) { backStackEntry ->
                    PantallaFotoExpandida(
                        uriString = backStackEntry.arguments?.getString("uri"),
                        navController = navController
                    )
                }
                composable("mapa") {
                    PantallaMapa(viewModel = viewModel, navController = navController)
                }
            }
        }
    }

    private fun lanzarCamara() {
        val nombreArchivo = "foto_${System.currentTimeMillis()}.jpg"
        val valores = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Vacaciones")
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores)
        uri?.let {
            currentPhotoUri = it
            lanzarCamaraLauncher.launch(it)
        }
    }
}