package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Loja
import java.io.IOException

object LojaRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Loja>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var produtos = arrayListOf<Loja>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val produto = Loja.fromJson(item)
                        produtos.add(produto)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(produtos)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(produtos)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Loja?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var produto : Loja? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    produto = Loja.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(produto)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(produto)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetAllByGinasioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Loja>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja/Ginasio/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var produtos = arrayListOf<Loja>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val produto = Loja.fromJson(item)
                        produtos.add(produto)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(produtos)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(produtos)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newProduto : Loja, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${newProduto.id_ginasio},
                  "nome": "${newProduto.nome}",
                  "tipo_produto": "${newProduto.tipo_produto}",
                  "preco": ${newProduto.preco},
                  "descricao": "${newProduto.descricao}",
                  "estado_produto": "${newProduto.estado_produto}",
                  "foto_produto": "${newProduto.foto_produto}",
                  "quantidade_produto": ${newProduto.quantidade_produto}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja")
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
                        callback("Error: Post Product fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editProduto : Loja, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_produto": ${editProduto.id_produto},
                  "id_ginasio": ${editProduto.id_ginasio},
                  "nome": "${editProduto.nome}",
                  "tipo_produto": "${editProduto.tipo_produto}",
                  "preco": ${editProduto.preco},
                  "descricao": "${editProduto.descricao}",
                  "estado_produto": "${editProduto.estado_produto}",
                  "foto_produto": "${editProduto.foto_produto}",
                  "quantidade_produto": ${editProduto.quantidade_produto}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja/$targetID")
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
                        callback("Error: Patch Product fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Loja/$targetID")
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
                        callback("Error: Delete Product fails")
                    }
            }
        }
    }
}