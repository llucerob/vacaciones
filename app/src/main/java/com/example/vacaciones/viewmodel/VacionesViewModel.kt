package com.example.vacaciones.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LugarVisita(
    val nombre: String = "",
    val fotos: List<Uri> = emptyList(),
    val latitud: Double? = null,
    val longitud: Double? = null
)

class VacacionesViewModel : ViewModel() {

    private val _lugarVisita = MutableStateFlow(LugarVisita())
    val lugarVisita: StateFlow<LugarVisita> = _lugarVisita

    fun actualizarNombre(nombre: String) {
        _lugarVisita.value = _lugarVisita.value.copy(nombre = nombre)
    }

    fun agregarFoto(uri: Uri) {
        _lugarVisita.value = _lugarVisita.value.copy(
            fotos = _lugarVisita.value.fotos + uri
        )
    }

    fun actualizarUbicacion(lat: Double, lon: Double) {
        _lugarVisita.value = _lugarVisita.value.copy(
            latitud = lat,
            longitud = lon
        )
    }
}

