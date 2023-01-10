package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Plano_Nutricional
import java.io.IOException

object PlanoNutricionalRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Plano_Nutricional>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var planosNutricionais = arrayListOf<Plano_Nutricional>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val planoNutricional = Plano_Nutricional.fromJson(item)
                        planosNutricionais.add(planoNutricional)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(planosNutricionais)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(planosNutricionais)
                    }
            }
        }
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Plano_Nutricional?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var planoNutricional : Plano_Nutricional? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    planoNutricional = Plano_Nutricional.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(planoNutricional)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(planoNutricional)
                    }
            }
        }
    }

    fun GetAllByGinasioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Plano_Nutricional>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional/Ginasio/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var planosNutricionais = arrayListOf<Plano_Nutricional>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val planoNutricional = Plano_Nutricional.fromJson(item)
                        planosNutricionais.add(planoNutricional)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(planosNutricionais)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(planosNutricionais)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newPlanoNutricional : Plano_Nutricional, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${newPlanoNutricional.id_ginasio},
                  "tipo": "${newPlanoNutricional.tipo}",
                  "calorias": ${newPlanoNutricional.calorias},
                  "foto_plano_nutricional": "${newPlanoNutricional.foto_plano_nutricional}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional")
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
                        callback("Error: Post Pedido fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editPlanoNutricional : Plano_Nutricional, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_plano_nutricional": ${editPlanoNutricional.id_plano_nutricional},
                  "id_ginasio": ${editPlanoNutricional.id_ginasio},
                  "tipo": "${editPlanoNutricional.tipo}",
                  "calorias": ${editPlanoNutricional.calorias},
                  "foto_plano_nutricional": "${editPlanoNutricional.foto_plano_nutricional}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional/$targetID")
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
                        callback("Error: Patch Pedido fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional/$targetID")
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
                        callback("Error: Delete Pedido fails")
                    }
            }
        }
    }

    fun DeleteChecked(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PlanoNutricional/Delete/$targetID")
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
                        callback("Error: Delete Checked Pedido fails")
                    }
            }
        }
    }
}