package com.example.vacaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.vacaciones.ui.*
import com.example.vacaciones.viewmodel.VacacionesViewModel
import com.example.vacaciones.viewmodel.VacacionesViewModelFactory

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            val factory = VacacionesViewModelFactory(applicationContext)
            val viewModel: VacacionesViewModel = viewModel(factory = factory)
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "listado") {

                composable("listado") {
                    PantallaListadoLugares(
                        viewModel = viewModel,
                        onVerDetalle = { id ->
                            viewModel.seleccionarLugar(id)
                            navController.navigate("detalle")
                        },
                        onAgregar = {
                            viewModel.limpiarSeleccion()
                            navController.navigate("editar")
                        }
                    )
                }

                composable("detalle") {
                    PantallaDetalleLugar(
                        viewModel = viewModel,
                        onEditar = {
                            navController.navigate("editar")
                        },
                        onVolver = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("editar") {
                    PantallaEditarLugar(
                        viewModel = viewModel,
                        onGuardar = {
                            navController.popBackStack("listado", inclusive = false)
                        }
                    )
                }
            }
        }
    }
}
