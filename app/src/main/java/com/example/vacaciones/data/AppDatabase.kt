package com.example.vacaciones.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vacaciones.model.Lugar

@Database(entities = [Lugar::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lugarDao(): LugarDao
}
