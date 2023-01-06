package vough.example.ipcagym.requests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.http.PATCH
import vough.example.ipcagym.MainActivity
import vough.example.ipcagym.data_classes.Atividade
import java.io.IOException

object AtividadeRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Atividade>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Atividade")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var atividades = arrayListOf<Atividade>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val atividade = Atividade.fromJson(item)
                        atividades.add(atividade)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(atividades)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(atividades)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Atividade?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Atividade/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var atividade : Atividade? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    //val item = JSONList.getJSONObject(0)
                    atividade = Atividade.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(atividade)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(atividade)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newAtividade : Atividade, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${newAtividade.id_ginasio},
                  "id_cliente": ${newAtividade.id_cliente},
                  "data_entrada": "${newAtividade.data_entrada}",
                  "data_saida": null
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Atividade")
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

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editAtividade : Atividade, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${editAtividade.id_ginasio},
                  "id_cliente": ${editAtividade.id_cliente},
                  "data_entrada": "${editAtividade.data_entrada}",
                  "data_saida": "${editAtividade.data_saida}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Atividade/$targetID")
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
                .url(UtilsForRequests.baseURL + "/api/Atividade/$targetID")
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