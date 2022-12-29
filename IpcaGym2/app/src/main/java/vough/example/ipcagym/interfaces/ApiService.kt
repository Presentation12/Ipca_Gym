package vough.example.ipcagym.interfaces

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente

interface ApiService {
    //CLIENTE
    @GET("/api/Cliente")
    suspend fun GetAllCliente(): Response<List<Cliente>>

    @POST("/api/Cliente/login")
    suspend fun LoginCliente(@Body requestBody: RequestBody): Response<String>

    //ATIVIDADE
    @GET("/api/Atividade")
    suspend fun GetAllAtividade(@Header("Authorization") token : String?): Response<List<Atividade>>
}