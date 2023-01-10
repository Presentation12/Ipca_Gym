package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Horario_Funcionario
import java.io.IOException

object HorarioFuncionarioRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Horario_Funcionario>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var horariosFuncionario = arrayListOf<Horario_Funcionario>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val dia = Horario_Funcionario.fromJson(item)
                        horariosFuncionario.add(dia)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(horariosFuncionario)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(horariosFuncionario)
                    }
            }
        }
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Horario_Funcionario?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var horarioFuncionario : Horario_Funcionario? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    horarioFuncionario = Horario_Funcionario.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(horarioFuncionario)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(horarioFuncionario)
                    }
            }
        }
    }

    fun GetAllByFuncionarioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Horario_Funcionario>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario/funcionario/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var horariosFuncionario = arrayListOf<Horario_Funcionario>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val dia = Horario_Funcionario.fromJson(item)
                        horariosFuncionario.add(dia)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(horariosFuncionario)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(horariosFuncionario)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newHorarioFuncionario : Horario_Funcionario, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_funcionario": ${newHorarioFuncionario.id_funcionario},
                  "hora_entrada": ${newHorarioFuncionario.hora_entrada},
                  "hora_saida": ${newHorarioFuncionario.hora_saida},
                  "dia_semana": "${newHorarioFuncionario.dia_semana}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario")
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
                        callback("Error: Post Day in Employee schedule fails")
                    }
            }
        }
    }

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editHorarioFuncionario : Horario_Funcionario, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_funcionario_horario": ${editHorarioFuncionario.id_funcionario_horario},
                  "id_funcionario": ${editHorarioFuncionario.id_funcionario},
                  "hora_entrada": ${editHorarioFuncionario.hora_entrada},
                  "hora_saida": ${editHorarioFuncionario.hora_saida},
                  "dia_semana": "${editHorarioFuncionario.dia_semana}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario/$targetID")
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
                        callback("Error: Patch Day in Employee schedule fails")
                    }
            }
        }
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID: Int, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/HorarioFuncionario/$targetID")
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
                        callback("Error: Delete Day in Employee schedule fails")
                    }
            }
        }
    }
}