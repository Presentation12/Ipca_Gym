package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Marcacoes_Details: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_marcacoes_details)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_marcacao = intent.getStringExtra("data_marcacao")
        val estado = intent.getStringExtra("estado")
        val descricao = intent.getStringExtra("descricao")

        val buttonCancelar = findViewById<Button>(R.id.buttonCancelar)

        findViewById<TextView>(R.id.marcacaoidmarcacaovalue).text = id_marcacao.toString()

        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
            findViewById<TextView>(R.id.marcacaoclientevalue).text = it?.nome.toString()
        }

        FuncionarioRequests.GetByID(lifecycleScope, sessionToken, id_funcionario){
            findViewById<TextView>(R.id.marcacaofuncionariovalue).text = it?.nome.toString()

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

        val adapter = MyAdapter(this@Activity_Funcionario_Marcacoes_Details, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }
        imageView.setOnClickListener{ spinner.performClick() }

        findViewById<TextView>(R.id.marcacaodatavalue).text = data_marcacao
        findViewById<TextView>(R.id.marcacaodescricaovalue).text = descricao
        findViewById<TextView>(R.id.marcacaoestadovalue).text = estado

        if(estado != "Ativo"){
            buttonCancelar.isVisible = false
        }


        buttonCancelar.setOnClickListener{
           MarcacaoRequests.PatchCancelMarcacao(lifecycleScope, sessionToken, id_marcacao, Marcacao(
                id_marcacao,
                id_funcionario,
                id_cliente,
                LocalDateTime.parse(data_marcacao!! + ":00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                descricao,
                estado)){ result ->
               if(result != "Error: Patch Cancel Marcacao Checked Product fails"){
                   Toast.makeText(this@Activity_Funcionario_Marcacoes_Details, "Appointment cancelled successfully", Toast.LENGTH_SHORT).show()
                   finish()
                   startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Marcacoes::class.java))
               }
               else
                   Toast.makeText(this@Activity_Funcionario_Marcacoes_Details, "Error on cancelling appointment", Toast.LENGTH_SHORT).show()
            }
        }

        //TODO: REMARCAR CONSULTA

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}