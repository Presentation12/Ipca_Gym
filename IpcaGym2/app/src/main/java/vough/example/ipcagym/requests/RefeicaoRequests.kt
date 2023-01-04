package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Refeicao
import java.io.IOException

object RefeicaoRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Refeicao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Refeicao")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var refeicoes = arrayListOf<Refeicao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val refeicao = Refeicao.fromJson(item)
                        refeicoes.add(refeicao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(refeicoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(refeicoes)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Refeicao?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Refeicao/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var refeicao : Refeicao? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    refeicao = Refeicao.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(refeicao)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(refeicao)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetAllByGinasioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Refeicao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja/Refeicao/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var refeicoes = arrayListOf<Refeicao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val produto = Refeicao.fromJson(item)
                        refeicoes.add(produto)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(refeicoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(refeicoes)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newRefeicao : Refeicao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_plano_nutricional": ${newRefeicao.id_plano_nutricional},
                  "descricao": "${newRefeicao.descricao}",
                  "hora": ${newRefeicao.hora},
                  "foto_refeicao": "${newRefeicao.foto_refeicao}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Refeicao")
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

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editRefeicao : Refeicao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_refeicao": ${editRefeicao.id_refeicao},
                  "id_plano_nutricional": ${editRefeicao.id_plano_nutricional},
                  "descricao": "${editRefeicao.descricao}",
                  "hora": ${editRefeicao.hora},
                  "foto_refeicao": "${editRefeicao.foto_refeicao}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Refeicao/$targetID")
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
                .url(UtilsForRequests.baseURL + "/api/Refeicao/$targetID")
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
}