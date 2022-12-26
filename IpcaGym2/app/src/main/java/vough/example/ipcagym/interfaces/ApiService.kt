package vough.example.ipcagym.interfaces

import retrofit2.Call
import retrofit2.http.GET
import vough.example.ipcagym.data_classes.Cliente

interface ApiService {
    @GET("/api/Cliente")
    fun GetAll(): Call<List<Cliente>>
}