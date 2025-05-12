package com.example.vacaciones.repository

import com.example.vacaciones.data.LugarDao
import com.example.vacaciones.model.Lugar
import kotlinx.coroutines.flow.Flow

class LugarRepository(private val dao: LugarDao) {
    val lugares: Flow<List<Lugar>> = dao.obtenerTodos()

    suspend fun insertar(lugar: Lugar) = dao.insertar(lugar)
    suspend fun actualizar(lugar: Lugar) = dao.actualizar(lugar)
    suspend fun eliminar(lugar: Lugar) = dao.eliminar(lugar)
    suspend fun obtenerPorId(id: Int) = dao.obtenerPorId(id)
}
