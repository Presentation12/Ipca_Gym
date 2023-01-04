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

object AtividadeRequests {
    private val client = OkHttpClient()

}