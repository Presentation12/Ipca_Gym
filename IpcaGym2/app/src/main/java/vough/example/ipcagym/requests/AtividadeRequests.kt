package vough.example.ipcagym.requests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.http.PATCH
import vough.example.ipcagym.MainActivity
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente
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

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Atividade) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Post(scope: CoroutineScope, token : String?, jsonRequestBody : String?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Patch(scope: CoroutineScope, token : String?, jsonRequestBody : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }
}