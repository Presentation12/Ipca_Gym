package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.data_classes.Plano_Nutricional
import java.io.IOException

object MarcacaoRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Marcacao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var marcacoes = arrayListOf<Marcacao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val marcacao = Marcacao.fromJson(item)
                        marcacoes.add(marcacao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Marcacao?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var marcacao : Marcacao? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    //val item = JSONList.getJSONObject(0)
                    marcacao = Marcacao.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(marcacao)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(marcacao)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newMarcacao : Marcacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_funcionario": ${newMarcacao.id_funcionario},
                  "id_cliente": ${newMarcacao.id_cliente},
                  "data_marcacao": "${newMarcacao.data_marcacao}",
                  "descricao": "${newMarcacao.descricao}",
                  "estado": "${newMarcacao.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao")
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
                        callback("User not found")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editMarcacao : Marcacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_marcacao": ${editMarcacao.id_marcacao},
                  "id_funcionario": ${editMarcacao.id_funcionario},
                  "id_cliente": ${editMarcacao.id_cliente},
                  "data_marcacao": "${editMarcacao.data_marcacao}",
                  "descricao": "${editMarcacao.descricao}",
                  "estado": "${editMarcacao.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/$targetID")
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
                        callback("User not found")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/$targetID")
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
                        callback("User not found")
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetAllByFuncionarioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Marcacao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/funcionario/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var marcacoes = arrayListOf<Marcacao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val marcacao = Marcacao.fromJson(item)
                        marcacoes.add(marcacao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetAllByClienteID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Marcacao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/cliente/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var marcacoes = arrayListOf<Marcacao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val marcacao = Marcacao.fromJson(item)
                        marcacoes.add(marcacao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(marcacoes)
                    }
            }
        }
    }

    fun PostChecked(scope: CoroutineScope, token : String?, newMarcacao : Marcacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_funcionario": ${newMarcacao.id_funcionario},
                  "id_cliente": ${newMarcacao.id_cliente},
                  "data_marcacao": "${newMarcacao.data_marcacao}",
                  "descricao": "${newMarcacao.descricao}",
                  "estado": "${newMarcacao.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/new/marcacao")
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
                        callback("User not found")
                    }
            }
        }
    }

    fun PatchCancelMarcacao(scope: CoroutineScope, token : String?, targetID: Int, editMarcacao : Marcacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_marcacao": ${editMarcacao.id_marcacao},
                  "id_funcionario": ${editMarcacao.id_funcionario},
                  "id_cliente": ${editMarcacao.id_cliente},
                  "data_marcacao": "${editMarcacao.data_marcacao}",
                  "descricao": "${editMarcacao.descricao}",
                  "estado": "${editMarcacao.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/cancelamento/$targetID")
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
                        callback("User not found")
                    }
            }
        }
    }

    fun PatchRescheduleMarcacao(scope: CoroutineScope, token : String?, targetID: Int, editMarcacao : Marcacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_marcacao": ${editMarcacao.id_marcacao},
                  "id_funcionario": ${editMarcacao.id_funcionario},
                  "id_cliente": ${editMarcacao.id_cliente},
                  "data_marcacao": "${editMarcacao.data_marcacao}",
                  "descricao": "${editMarcacao.descricao}",
                  "estado": "${editMarcacao.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Marcacao/remarcacao/$targetID")
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
                        callback("User not found")
                    }
            }
        }
    }
}