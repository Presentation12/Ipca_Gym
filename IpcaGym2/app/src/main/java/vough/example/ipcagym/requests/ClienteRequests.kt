package vough.example.ipcagym.requests

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import vough.example.ipcagym.cliente_classes.LoginClienteActivity
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Atividade.Companion.fromJson
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.interfaces.ApiService

object ClienteRequests {
    private val client = OkHttpClient()

    fun login(scope : CoroutineScope, mail: String?, pass: String?, callback: (String)->Unit){
        scope.launch(Dispatchers.IO){
            val retrofit = Retrofit.Builder().baseUrl("https://localhost:7288").client(client).build()
            val service = retrofit.create(ApiService::class.java)
            val json = """
            {
                "mail": "$mail",
                "password": "$pass"
            }
            """

            val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)

            val response = service.LoginCliente(body)

            if(response.code() == 200){
                val result = response.body()!!.toString()
                val jsonObject = JSONObject(result)

                scope.launch(Dispatchers.Main){ callback(result) }
            }
            else{
                val tokenResult = ""
                scope.launch(Dispatchers.Main){ callback(tokenResult) }
            }
        }
    }

    /*fun login(username: String, password : String): String{
        val json = """
        {
            "mail": "$username",
            "password": "$password"
        }
    """

        // Build the request
        val request = Request.Builder()
            .url("http://127.0.0.1:4040/api/Cliente/login")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        // Send the request and get the response
        val response: Response = client.newCall(request).execute()

        // Check the response code
        if (response.code == 200) {
            // Parse the response body as JSON
            val json: JSONObject = JSONObject(response.body!!.string())

            // Check if the login was successful
            if (json.getBoolean("success")) {
                return "Sucesso!"
            }
        }

        return "Erro!"
    }*/
}