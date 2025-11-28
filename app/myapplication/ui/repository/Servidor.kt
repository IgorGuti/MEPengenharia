package com.example.myapplication.ui.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Interface para definir a API
interface ApiService {
    @POST("users/app/login")
    fun login(@Body login: Login): Call<LoginResponse>
    @GET("users/app/{route}/reservatorio")
    suspend fun getReservatorio(
        @Path("route") route: String,
        @Query("id") id: String
    ): ReservatorioResponse
    @GET("users/app/{route}/eletrico")
    suspend fun getEletrico(
        @Path("route") route: String,
        @Query("id") id: String
    ): ResponseMedidorEletrico
}
class ApiRepository(private val context: Context) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.17:3000/") // Substitua pela URL base da sua API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    // Função para obter dados do reservatório
    fun getReservatorio(route: String, id: String, onSuccess: (ReservatorioResponse) -> Unit, onFailure: (Throwable) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getReservatorio(route, id)
                Log.d("ApiRepository", "Resposta recebida: $response")
                withContext(Dispatchers.Main) {
                    onSuccess(response)
                }
            } catch (e: Exception) {
                Log.e("ApiRepository", "Erro ao obter dados: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }
    fun getEletrico(route: String, id: String, onSuccess: (ResponseMedidorEletrico) -> Unit, onFailure: (Throwable) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseEletrico = apiService.getEletrico(route, id)
                Log.d("ApiRepository", "Resposta recebida: $responseEletrico")
                withContext(Dispatchers.Main) {
                    onSuccess(responseEletrico)
                }
            } catch (e: Exception) {
                Log.e("ApiRepository", "Erro ao obter dados: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    private fun parseReservatoriosNomes(reservatoriosNomes: String): List<Reservatorio> {
        val reservatorios = mutableListOf<Reservatorio>()
        val parts = reservatoriosNomes.trim().split(";")

        try {
            // Verifica se o número de partes é múltiplo de 2 (ID e nome para cada reservatório)
            if (parts.size % 2 != 0) {
                throw IllegalArgumentException("Número de partes inválido para reservatórios: ${parts.size}. Esperado múltiplo de 2.")
            }
            // Itera sobre as partes e cria Reservatorios
            for (i in parts.indices step 2) {
                val id = parts[i].toIntOrNull()
                if (id == null) {
                    throw IllegalArgumentException("ID inválido em parte[${i}]: ${parts[i]}")
                }
                val nome = parts[i + 1]
                reservatorios.add(Reservatorio(id, nome))
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ApiRepository", "Erro ao processar reservatoriosNomes: ${e.message}")
            throw e
        }
        return reservatorios
    }
    private fun parseMedidoresNomes(medidoresNomes: String): List<Medidor> {
        val medidores = mutableListOf<Medidor>()
        val parts = medidoresNomes.trim().split(";")

        try {
            // Verifica se o número de partes é múltiplo de 2 (ID e nome para cada medidor)
            if (parts.size % 2 != 0) {
                throw IllegalArgumentException("Número de partes inválido para medidores: ${parts.size}. Esperado múltiplo de 2.")
            }
            // Itera sobre as partes e cria Medidores
            for (i in parts.indices step 2) {
                val id = parts[i].toIntOrNull()
                if (id == null) {
                    throw IllegalArgumentException("ID inválido em parte[${i}]: ${parts[i]}")
                }
                val nome = parts[i + 1]
                medidores.add(Medidor(id, nome))
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ApiRepository", "Erro ao processar medidoresNomes: ${e.message}")
            throw e
        }
        return medidores
    }
    private fun parseHidrometrosNomes(hidrometrosNomes: String?): List<Hidrometro> {
        val hidrometros = mutableListOf<Hidrometro>()
        if (hidrometrosNomes.isNullOrEmpty()) return emptyList()

        val parts = hidrometrosNomes.trim().split(";")

        try {
            // Verifica se o número de partes é múltiplo de 2 (ID e nome para cada hidrômetro)
            if (parts.size % 2 != 0) {
                throw IllegalArgumentException("Número de partes inválido para hidrômetros: ${parts.size}. Esperado múltiplo de 2.")
            }

            // Itera sobre as partes e cria Hidrometros
            for (i in parts.indices step 2) {
                val id = parts[i].toIntOrNull()
                if (id == null) {
                    throw IllegalArgumentException("ID inválido em parte[${i}]: ${parts[i]}")
                }
                val nome = parts[i + 1]
                hidrometros.add(Hidrometro(id, nome))
            }

        } catch (e: IllegalArgumentException) {
            Log.e("ApiRepository", "Erro ao processar hidrometrosNomes: ${e.message}")
            throw e
        }
        return hidrometros
    }

    fun login(username: String, password: String, onSuccess: (LoginResponse, List<Reservatorio>, List<Medidor>, List<Hidrometro>) -> Unit, onFailure: (Throwable) -> Unit) {
        val login = Login(username, password)

        val handler = Handler(Looper.getMainLooper())
        val delay: Long = 2000

        val runnable = object : Runnable {
            override fun run() {
                Toast.makeText(context, "Carregando...", Toast.LENGTH_SHORT).show()
                handler.postDelayed(this, delay)
            }
        }
        handler.post(runnable)

        apiService.login(login).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                handler.removeCallbacks(runnable)
                val loginResponse = response.body()
                Log.d("Response Login", loginResponse.toString())
                if (loginResponse != null) {
                    if (loginResponse.resultado == "Login bem-sucedido") {
                        try {
                            val reservatorios = parseReservatoriosNomes(loginResponse.reservatoriosNomes)
                            Log.d("MonitorAvaliação", reservatorios.toString())
                            val medidores = parseMedidoresNomes(loginResponse.medidoresNomes)
                            val hidrometros = parseHidrometrosNomes(loginResponse.hidrometrosNomes)
                            onSuccess(loginResponse, reservatorios, medidores, hidrometros)
                        } catch (e: IllegalArgumentException) {
                            Toast.makeText(context, "Erro ao processar dados: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                        Log.d("ApiRepository", "LoginResponse: $loginResponse")
                    } else {
                        Toast.makeText(context, loginResponse.resultado, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Resposta do servidor vazia", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                handler.removeCallbacks(runnable)
                Toast.makeText(context, "Sem acesso ao servidor nesse momento", Toast.LENGTH_LONG).show()
            }
        })
    }
}
