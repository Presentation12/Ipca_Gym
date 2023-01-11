package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests

class Activity_Funcionario_Flux_Control_Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_flux_control_details)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        //fazer na outra activity
        val id_atividade = intent.getIntExtra("id_atividade", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data = intent.getStringExtra("data")
        val data_E = intent.getStringExtra("data_E")
        val hora_entrada = intent.getStringExtra("hora_entrada")
        val hora_saida = intent.getStringExtra("hora_saida")
        val state = intent.getBooleanExtra("state", true)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null){
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
            }
        }


        //Escrever no layout detalhado

        findViewById<TextView>(R.id.idAtividadeAtividadeDetailFuncionario).text = id_atividade.toString()

        GinasioRequests.GetByID(lifecycleScope, sessionToken, id_ginasio){ response ->
            if(response != null) findViewById<TextView>(R.id.idGinasioAtividadeDetailFuncionario).text = response.instituicao.toString()
        }

        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){ response ->
            if(response != null) findViewById<TextView>(R.id.idClienteAtividadeDetailFuncionario).text = response.nome.toString()
        }

        val dataView = findViewById<TextView>(R.id.dataAtividadeDetailFuncionario)
        val hora = findViewById<TextView>(R.id.horaAtividadeDetailFuncionario)

        if(state == true){
            hora.text = hora_entrada
            hora.setTextColor(Color.GREEN)
            findViewById<TextView>(R.id.textViewHourDetailFuncionario).text = "  Entry Hour"
            dataView.text = data
        }
        else{
            hora.text = hora_saida
            hora.setTextColor(Color.RED)
            findViewById<TextView>(R.id.textViewHourDetailFuncionario).text = "  Exit Hour"
            dataView.text = data_E
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Flux_Control_Details, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if(counter == 0){
                            counter+=1
                            spinner.setSelection(3)
                        }
                        else{
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }
    }
}