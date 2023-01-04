package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Funcionario
import java.io.IOException

object FuncionarioRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Funcionario>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun GetAllAtivo(scope: CoroutineScope, token : String?, callback: (ArrayList<Funcionario>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Funcionario) -> Unit){
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

    fun loginFuncionario(scope: CoroutineScope, code : String?, pass: String?, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                    "codigo": "$code",
                    "password": "$pass"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/login")
                .post(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
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

    fun recoverPasswordFuncionario(scope : CoroutineScope, code: String?, pass: String?, callback: (String)->Unit){
        scope.launch(Dispatchers.IO){
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/recoverpass?codigo=$code&password=$pass")
                .patch(RequestBody.create(null, ByteArray(0)))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code

                if(statusCode == 200)
                {
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
                        callback("error")
                    }
            }
        }
    }

    fun RegistCliente(scope: CoroutineScope, token : String?, jsonRequestBody : String?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun EditCliente(scope: CoroutineScope, token : String?, jsonRequestBody : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun EditLojaStock(scope: CoroutineScope, token : String?, quantidade : Int?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun EditLoja(scope: CoroutineScope, token : String?, jsonRequestBody : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun EditLotacaoGym(scope: CoroutineScope, token : String?, lotacao : Int?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun GetAvaliacoesOnGym(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Classificacao>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun DeleteFuncionario(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

}