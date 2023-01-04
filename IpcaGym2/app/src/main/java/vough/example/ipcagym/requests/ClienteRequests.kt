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
import java.io.IOException

object ClienteRequests {
    private val client = OkHttpClient()

    fun login(scope : CoroutineScope, mail: String?, pass: String?, callback: (String)->Unit){
        scope.launch(Dispatchers.IO){
            val json = """
            {
                "mail": "$mail",
                "password": "$pass"
            }
            """
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/login")
                .post(json.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200){
                    val result = response.body!!.string()
                    val jsonObject = JSONObject(result)
                    if (jsonObject.getString("statusCode") == "200"){
                        val JsonData = jsonObject.getJSONObject("data")
                        val JsonValue = JsonData.getString("value")

                        UtilsForRequests.token = JsonValue

                        scope.launch(Dispatchers.Main){
                            callback(JsonValue)
                        }
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("User not found")
                    }
            }
        }
    }

    fun recoverPasswordCliente(scope : CoroutineScope, mail: String?, pass: String?, callback: (String)->Unit){
        scope.launch(Dispatchers.IO){
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/recoverpass?mail=$mail&password=$pass")
                .patch(RequestBody.create(null, ByteArray(0)))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val result = response.body!!.string()

                val jsonObject = JSONObject(result)
                if (jsonObject.getString("statusCode") == "200"){
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("User not found")
                    }
            }
        }
    }
}