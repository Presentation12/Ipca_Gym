package vough.example.ipcagym

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import vough.example.ipcagym.data_classes.Cliente
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vough.example.ipcagym.interfaces.ApiService
import java.io.IOException

class ClientesManagementActivity : AppCompatActivity() {
    val clientes = arrayListOf<Cliente>()
    var client_adapter = AdapterClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes_list)

        /*val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl()
            .build()*/

        clientes.addAll(getClientes())
        //get();
        val listViewClients = findViewById<ListView>(R.id.list)
        listViewClients.adapter = client_adapter
    }

    fun get() {
        val client = OkHttpClient()
        //val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data)
        val request = Request.Builder()
            .url("https://localhost:7288/api/Cliente")
            .get()
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            Toast.makeText(this@ClientesManagementActivity,response.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@ClientesManagementActivity,response.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun getClientes(): List<Cliente> {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:4040.ngrok.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val clientesCall = apiService.GetAll()
        val clientes = clientesCall.execute().body()

        /*val request = Request.Builder()
            .get()
            .url("https://localhost:7288/api/Cliente")
            .build()

        OkHttpClient().newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            Toast.makeText(this@ClientesManagementActivity, response.toString(), Toast.LENGTH_LONG).show()
        }*/

        return clientes ?: emptyList()
    }

    inner class AdapterClient : BaseAdapter(){
        override fun getCount(): Int {
            return clientes.size
        }

        override fun getItem(position: Int): Any {
            return clientes[position]
        }

        override fun getItemId(position: Int): Long {
            return clientes[position].id_cliente.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.cliente_row,parent,false)

            val textViewName = rootView.findViewById<TextView>(R.id.text_view_nome)
            textViewName.text = "- " + clientes[position].nome
            textViewName.text = clientes[position].id_cliente.toString()

            return rootView
        }

    }
}