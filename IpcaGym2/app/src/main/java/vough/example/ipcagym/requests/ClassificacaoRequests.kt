package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Classificacao
import java.io.IOException

object ClassificacaoRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Classificacao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Classificacao")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var classificacoes = arrayListOf<Classificacao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val classificacao = Classificacao.fromJson(item)
                        classificacoes.add(classificacao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(classificacoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(classificacoes)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Classificacao?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Classificacao/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var classificacao : Classificacao? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    classificacao = Classificacao.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(classificacao)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(classificacao)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newClassificacao : Classificacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${newClassificacao.id_ginasio},
                  "id_cliente": ${newClassificacao.id_cliente},
                  "avaliacao": ${newClassificacao.avaliacao},
                  "comentario": "${newClassificacao.comentario}",
                  "data_avaliacao": "${newClassificacao.data_avaliacao}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Classificacao")
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
                        callback("Error: Post Classification fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editClassificacao : Classificacao, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_avaliacao": ${editClassificacao.id_avaliacao},
                  "id_ginasio": ${editClassificacao.id_ginasio},
                  "id_cliente": ${editClassificacao.id_cliente},
                  "avaliacao": ${editClassificacao.avaliacao},
                  "comentario": "${editClassificacao.comentario}",
                  "data_avaliacao": "${editClassificacao.data_avaliacao}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Classificacao/$targetID")
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
                        callback("Error: Patch Classification fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Classificacao/$targetID")
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
                        callback("Error: Delete Classification fails")
                    }
            }
        }
    }
}