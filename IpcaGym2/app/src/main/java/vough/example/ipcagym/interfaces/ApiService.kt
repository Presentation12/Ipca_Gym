package vough.example.ipcagym.interfaces

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente

interface ApiService {
    @GET("/api/Cliente")
    fun GetAllCliente(): Call<List<Cliente>>

    @GET("/api/Atividade")
    suspend fun GetAllAtividade(@Header("Authorization") token : String?): Response<List<Atividade>>
}