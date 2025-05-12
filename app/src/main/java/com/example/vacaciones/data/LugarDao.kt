package com.example.vacaciones.data

import androidx.room.*
import com.example.vacaciones.model.Lugar
import kotlinx.coroutines.flow.Flow

@Dao
interface LugarDao {
    @Query("SELECT * FROM lugares ORDER BY orden ASC")
    fun obtenerTodos(): Flow<List<Lugar>>

    @Query("SELECT * FROM lugares WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Lugar?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lugar: Lugar)

    @Update
    suspend fun actualizar(lugar: Lugar)

    @Delete
    suspend fun eliminar(lugar: Lugar)
}
