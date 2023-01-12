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
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.LoginReceiverModel
import java.io.IOException

object ClienteRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Cliente>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var clientes = arrayListOf<Cliente>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val cliente = Cliente.fromJson(item)
                        clientes.add(cliente)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(clientes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(clientes)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Cliente?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var cliente : Cliente? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    cliente = Cliente.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(cliente)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(cliente)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newCliente : Cliente, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                    "id_ginasio": ${newCliente.id_ginasio},
                    "id_plano_nutricional": ${newCliente.id_plano_nutricional},
                    "nome": "${newCliente.nome}",
                    "mail": "${newCliente.mail}",
                    "telemovel": ${newCliente.telemovel},
                    "pass_salt": "${newCliente.pass_salt}",
                    "peso": ${newCliente.peso},
                    "altura": ${newCliente.altura},
                    "gordura": ${newCliente.gordura},
                    "foto_perfil": "${newCliente.foto_perfil}",
                    "estado": "${newCliente.estado}" 
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente")
                .post(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("Error: Post Client fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int?, editCliente : Cliente?, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                    "id_cliente": ${editCliente?.id_cliente},
                    "id_ginasio": ${editCliente?.id_ginasio},
                    "id_plano_nutricional": ${editCliente?.id_plano_nutricional},
                    "nome": "${editCliente?.nome}",
                    "mail": "${editCliente?.mail}",
                    "telemovel": ${editCliente?.telemovel},
                    "pass_salt": "${editCliente?.pass_salt}",
                    "peso": ${editCliente?.peso},
                    "altura": ${editCliente?.altura},
                    "gordura": ${editCliente?.gordura},
                    "foto_perfil": "${editCliente?.foto_perfil}",
                    "estado": "${editCliente?.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/$targetID")
                .patch(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("Error: Patch Client fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/$targetID")
                .delete()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("Error: Delete Client fails")
                    }
            }
        }
    }

    fun login(scope : CoroutineScope, mail: String?, pass: String?, callback: (LoginReceiverModel)->Unit){
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
                var resultLogin = LoginReceiverModel("", "")

                if(statusCode == 200){
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getJSONObject("value")

                    resultLogin = LoginReceiverModel.fromJson(JsonValue)

                    scope.launch(Dispatchers.Main){
                        callback(resultLogin)
                    }
                }
                else if(statusCode == 204){
                    resultLogin.token = ""
                    resultLogin.role = "Wrong"

                    scope.launch(Dispatchers.Main){
                        callback(resultLogin)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(resultLogin)
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

                val statusCode = response.code

                if(statusCode == 200)
                {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("Error: Recover Client password fails")
                    }
            }
        }
    }

    fun DeleteCliente(scope: CoroutineScope, token : String?, targetID : Int?, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/remove/cliente/$targetID")
                .delete()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JsonData = jsonObject.getJSONObject("data")
                    val JsonValue = JsonData.getString("value")

                    scope.launch(Dispatchers.Main){
                        callback(JsonValue)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback("Error: Delete Client fails")
                    }
            }
        }
    }

    fun GetByToken(scope: CoroutineScope, token : String?, callback: (Cliente?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/getbytoken")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var cliente : Cliente? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    cliente = Cliente.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(cliente)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(cliente)
                    }
            }
        }
    }

    fun GetAllByGymID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Cliente>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Cliente/Gym/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var clientes = arrayListOf<Cliente>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val cliente = Cliente.fromJson(item)
                        clientes.add(cliente)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(clientes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(clientes)
                    }
            }
        }
    }
}