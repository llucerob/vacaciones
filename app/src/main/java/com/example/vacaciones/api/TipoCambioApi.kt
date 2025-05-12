package com.example.vacaciones.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object TipoCambioApi {
    suspend fun obtenerCambioCLPtoUSD(): Double {
        return withContext(Dispatchers.IO) {
            try {
                val response = URL("https://mindicador.cl/api").readText()
                val json = JSONObject(response)
                val dolar = json.getJSONObject("dolar")
                dolar.getDouble("valor")
            } catch (e: Exception) {
                1.0 // fallback
            }
        }
    }
}
