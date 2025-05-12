package com.example.vacaciones.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vacaciones.model.Lugar
import com.example.vacaciones.repository.LugarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VacacionesViewModel(private val repository: LugarRepository) : ViewModel() {

    val lugares: StateFlow<List<Lugar>> = repository.lugares
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _lugarSeleccionado = MutableStateFlow<Lugar?>(null)
    val lugarSeleccionado: StateFlow<Lugar?> = _lugarSeleccionado.asStateFlow()

    fun seleccionarLugar(id: Int) {
        viewModelScope.launch {
            _lugarSeleccionado.value = repository.obtenerPorId(id)
        }
    }

    fun insertar(lugar: Lugar) {
        viewModelScope.launch {
            repository.insertar(lugar)
        }
    }

    fun actualizar(lugar: Lugar) {
        viewModelScope.launch {
            repository.actualizar(lugar)
        }
    }

    fun eliminar(lugar: Lugar) {
        viewModelScope.launch {
            repository.eliminar(lugar)
        }
    }

    fun limpiarSeleccion() {
        _lugarSeleccionado.value = null
    }
}
