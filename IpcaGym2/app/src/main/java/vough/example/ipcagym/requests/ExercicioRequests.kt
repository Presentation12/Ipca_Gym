package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Exercicio
import java.io.IOException

object ExercicioRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Exercicio>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Exercicio")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var exercicios = arrayListOf<Exercicio>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val exercicio = Exercicio.fromJson(item)
                        exercicios.add(exercicio)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(exercicios)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(exercicios)
                    }
            }
        }
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Exercicio) -> Unit){
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

    fun GetAllByPlanoID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Exercicio>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }
}