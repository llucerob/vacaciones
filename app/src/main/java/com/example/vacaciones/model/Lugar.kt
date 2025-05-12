package com.example.vacaciones.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lugares")
data class Lugar(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val urlImagen: String,
    val latitud: Double,
    val longitud: Double,
    val orden: Int,
    val costoAlojamientoCLP: Int,
    val costoTransporteCLP: Int,
    val comentarios: String,
    val uriFoto: String? = null
)
