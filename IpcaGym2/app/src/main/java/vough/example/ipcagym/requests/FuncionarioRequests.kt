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
import vough.example.ipcagym.data_classes.*
import java.io.IOException

object FuncionarioRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Funcionario>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var funcionarios = arrayListOf<Funcionario>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val funcionario = Funcionario.fromJson(item)
                        funcionarios.add(funcionario)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(funcionarios)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(funcionarios)
                    }
            }
        }
    }

    //TODO: por verificar
    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Funcionario?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var funcionario : Funcionario? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    funcionario = Funcionario.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(funcionario)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(funcionario)
                    }
            }
        }
    }

    fun Post(scope: CoroutineScope, token : String?, newFuncionario : Funcionario, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${newFuncionario.id_ginasio},
                  "nome": "${newFuncionario.nome}",
                  "is_admin": ${newFuncionario.is_admin},
                  "codigo": ${newFuncionario.codigo},
                  "pass_salt": "${newFuncionario.pass_salt}",
                  "estado": "${newFuncionario.estado}",
                  "foto_funcionario": "${newFuncionario.foto_funcionario}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario")
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

    fun Patch(scope: CoroutineScope, token : String?, targetID: Int, editFuncionario : Funcionario, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            var jsonBody : String

            if(editFuncionario.foto_funcionario == null || editFuncionario.toString() == null){
                jsonBody = """
                    {
                      "id_funcionario": ${editFuncionario.id_funcionario},
                      "id_ginasio": ${editFuncionario.id_ginasio},
                      "nome": "${editFuncionario.nome}",
                      "is_admin": ${editFuncionario.is_admin},
                      "codigo": ${editFuncionario.codigo},
                      "pass_salt": "${editFuncionario.pass_salt}",
                      "estado": "${editFuncionario.estado}",
                      "foto_funcionario": ${editFuncionario.foto_funcionario}
                    }
                """
            }
            else{
                jsonBody = """
                    {
                      "id_funcionario": ${editFuncionario.id_funcionario},
                      "id_ginasio": ${editFuncionario.id_ginasio},
                      "nome": "${editFuncionario.nome}",
                      "is_admin": ${editFuncionario.is_admin},
                      "codigo": ${editFuncionario.codigo},
                      "pass_salt": "${editFuncionario.pass_salt}",
                      "estado": "${editFuncionario.estado}",
                      "foto_funcionario": "${editFuncionario.foto_funcionario}"
                    }
                """
            }

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/$targetID")
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
                .url(UtilsForRequests.baseURL + "/api/Funcionario/$targetID")
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

    fun RegistClient(scope: CoroutineScope, token : String?, newCliente : Cliente, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                    "id_ginasio": ${newCliente.id_ginasio},
                    "id_plano_nutricional": ${newCliente.id_plano_nutricional},
                    "nome": "${newCliente.nome}",
                    "mail": "${newCliente.mail}",
                    "telemovel": ${newCliente.telemovel},
                    "pass_salt": "${newCliente.pass_salt}",
                    "peso": ${newCliente.peso},
                    "altura": ${newCliente.altura},
                    "gordura": ${newCliente.gordura},
                    "foto_perfil": "${newCliente.foto_perfil}",
                    "estado": "${newCliente.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/register/cliente")
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

    fun EditCliente(scope: CoroutineScope, token : String?, targetID: Int, editCliente : Cliente, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                    "id_cliente": ${editCliente.id_cliente},
                    "id_ginasio": ${editCliente.id_ginasio},
                    "id_plano_nutricional": ${editCliente.id_plano_nutricional},
                    "nome": "${editCliente.nome}",
                    "mail": "${editCliente.mail}",
                    "telemovel": ${editCliente.telemovel},
                    "pass_salt": "${editCliente.pass_salt}",
                    "peso": ${editCliente.peso},
                    "altura": ${editCliente.altura},
                    "gordura": ${editCliente.gordura},
                    "foto_perfil": "${editCliente.foto_perfil}",
                    "estado": "${editCliente.estado}"
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/edit/cliente/$targetID")
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

    fun EditLojaStock(scope: CoroutineScope, token : String?, editProduto : Loja, targetID : Int?, callback: (String) -> Unit){
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
                .url(UtilsForRequests.baseURL + "/api/Funcionario/changestock/$targetID")
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

    fun EditLoja(scope: CoroutineScope, token : String?, editProduto : Loja, targetID : Int?, callback: (String) -> Unit){
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
                .url(UtilsForRequests.baseURL + "/api/Funcionario/edit/product/$targetID")
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

    fun EditLotacaoGym(scope: CoroutineScope, token : String?, editGinasio : Ginasio, targetID : Int?, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {
            val jsonBody = """
                {
                  "id_ginasio": ${editGinasio.id_ginasio},
                  "instituicao": "${editGinasio.instituicao}",
                  "estado": "${editGinasio.estado}",
                  "foto_ginasio": "${editGinasio.foto_ginasio}",
                  "contacto": ${editGinasio.contacto},
                  "lotacao": ${editGinasio.lotacao},
                  "lotacaoMax": ${editGinasio.lotacaoMax}
                }
            """

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/edit/ocupation/$targetID")
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

    //TODO: por verificar
    fun GetAvaliacoesOnGym(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Classificacao>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/avaliacoes/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var avaliacoes = arrayListOf<Classificacao>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val avaliacao = Classificacao.fromJson(item)
                        avaliacoes.add(avaliacao)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(avaliacoes)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(avaliacoes)
                    }
            }
        }
    }

    fun DeleteFuncionario(scope: CoroutineScope, token : String?, targetID : Int?, callback: (String) -> Unit){
        scope.launch(Dispatchers.IO) {

            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/delete/$targetID")
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

    fun GetByToken(scope: CoroutineScope, token : String?, callback: (Funcionario?) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/getbytoken")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var funcionario : Funcionario? = null

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val item = JSONData.getJSONObject("value")

                    funcionario = Funcionario.fromJson(item)

                    scope.launch(Dispatchers.Main){
                        callback(funcionario)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(funcionario)
                    }
            }
        }
    }

    fun GetAllByGym(scope: CoroutineScope, token : String?, targetID: Int?, callback: (ArrayList<Funcionario>) -> Unit){
        scope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(UtilsForRequests.baseURL + "/api/Funcionario/allbygym/$targetID")
                .get()
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val statusCode = response.code
                var funcionarios = arrayListOf<Funcionario>()

                if(statusCode == 200) {
                    val result = response.body!!.string()

                    val jsonObject = JSONObject(result)
                    val JSONData = jsonObject.getJSONObject("data")
                    val JSONList = JSONData.getJSONArray("value")

                    for (i in 0 until JSONList.length()) {
                        val item = JSONList.getJSONObject(i)
                        val funcionario = Funcionario.fromJson(item)
                        funcionarios.add(funcionario)
                    }

                    scope.launch(Dispatchers.Main){
                        callback(funcionarios)
                    }
                }
                else
                    scope.launch(Dispatchers.Main){
                        callback(funcionarios)
                    }
            }
        }
    }
}