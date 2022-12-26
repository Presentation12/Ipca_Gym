package vough.example.ipcagym

import vough.example.ipcagym.data_classes.Cliente
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vough.example.ipcagym.interfaces.ApiService

class ClientesManagementActivity {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://localhost:7288")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClientes(): List<Cliente> {

        /*
        val retrofit = Retrofit.Builder()
            .baseUrl("jdbc:sqlserver://localhost;databaseName=<ipcagym>;user=<user>;password=<password>")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        */

        val apiService = retrofit.create(ApiService::class.java)
        val clientesCall = apiService.GetAll()
        val clientes = clientesCall.execute().body()
        return clientes ?: emptyList()
    }
}