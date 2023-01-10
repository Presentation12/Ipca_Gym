package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Pedido_Loja
import vough.example.ipcagym.data_classes.Plano_Nutricional
import java.io.IOException

object PedidoLojaRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Pedido_Loja>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var pedidosLoja = arrayListOf<Pedido_Loja>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val pedidoLoja = Pedido_Loja.fromJson(item)
                        pedidosLoja.add(pedidoLoja)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(pedidosLoja)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(pedidosLoja)
                    }
            }
        }
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Pedido_Loja?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var pedidoLoja : Pedido_Loja? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    pedidoLoja = Pedido_Loja.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(pedidoLoja)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(pedidoLoja)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newPedidoLoja : Pedido_Loja, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_pedido": ${newPedidoLoja.id_pedido},
                  "id_produto": ${newPedidoLoja.id_produto},
                  "quantidade_pedido": ${newPedidoLoja.quantidade_pedido}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja")
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
                        callback("Error: Post PedidoLoja fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editPedidoLoja : Pedido_Loja, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_pedido": ${editPedidoLoja.id_pedido},
                  "id_produto": ${editPedidoLoja.id_produto},
                  "quantidade_pedido": ${editPedidoLoja.quantidade_pedido}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/$targetID")
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
                        callback("Error: Patch PedidoLoja fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/$targetID")
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
                        callback("Error: Delete PedidoLoja fails")
                    }
            }
        }
    }

    fun GetAllByPedidoIDID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Pedido_Loja>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/Pedido/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var pedidosLoja = arrayListOf<Pedido_Loja>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val pedidoLoja = Pedido_Loja.fromJson(item)
                        pedidosLoja.add(pedidoLoja)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(pedidosLoja)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(pedidosLoja)
                    }
            }
        }
    }

    fun DeletePedidoLoja(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/Pedido/$targetID")
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
                        callback("Error: Delete PedidoLoja fails")
                    }
            }
        }
    }

    fun PostPedidoChecked(scope: CoroutineScope, token : String?, newPedidoLoja : Pedido_Loja, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_pedido": ${newPedidoLoja.id_pedido},
                  "id_produto": ${newPedidoLoja.id_produto},
                  "quantidade_pedido": ${newPedidoLoja.quantidade_pedido}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/PedidoLoja/PedidoChecked")
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
                        callback("Error: Post PedidoLoja fails")
                    }
            }
        }
    }
}