package vough.example.ipcagym.requests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import vough.example.ipcagym.MainActivity
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.interfaces.ApiService

object AtividadeRequests {
    val token = "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTUxMiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiIxIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiQWRtaW4iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3NlcmlhbG51bWJlciI6IjIiLCJleHAiOjE2NzIzNDY5MzJ9.p-14qTyQ3P7VRKm9kB4gBZrvUCacZE_s29lAuuO4I91ndt5_Vdj8yu5eZszjaS7TgMUSqecRVJHBMEgOfeRB8g"
    private val client = OkHttpClient()

    fun getAllAtividade(scope : CoroutineScope, token: String?, callback: (ArrayList<Atividade>)->Unit){
        scope.launch(Dispatchers.IO){
            val retrofit = Retrofit.Builder().baseUrl("https://localhost:7288").client(client).build()
            val service = retrofit.create(ApiService::class.java)
            val response = service.GetAllAtividade(token)

            if(response.isSuccessful){
                val result = response.body()!!.toString()
                val jsonObject = JSONObject(result)
                val listAtividades = arrayListOf(Atividade.fromJson(jsonObject))

                scope.launch(Dispatchers.Main){ callback(listAtividades) }
            }
            else{
                val listAtividades = arrayListOf<Atividade>()
                scope.launch(Dispatchers.Main){ callback(listAtividades) }
            }
        }
    }
}