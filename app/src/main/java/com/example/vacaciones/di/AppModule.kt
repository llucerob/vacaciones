package com.example.vacaciones.di

import android.content.Context
import androidx.room.Room
import com.example.vacaciones.data.AppDatabase
import com.example.vacaciones.repository.LugarRepository

object AppModule {
    private var dbInstance: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return dbInstance ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "vacaciones_db"
        ).build().also { dbInstance = it }
    }

    fun provideLugarRepository(context: Context): LugarRepository {
        val database = provideDatabase(context)
        return LugarRepository(database.lugarDao())
    }
}
