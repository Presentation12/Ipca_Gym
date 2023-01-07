package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Marcacao_Consulta : AppCompatActivity() {

    var clienteRefresh : Cliente? = null
    var idsFuncionariosGinasio = ArrayList<Int?>()
    var idFuncionarioSelected : Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_consulta)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_marcacao_page)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null) clienteRefresh = resultCliente

            FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, resultCliente?.id_ginasio) { resultFuncionario ->

                for (x in resultFuncionario)
                {
                    idsFuncionariosGinasio.add(x.id_funcionario)
                }

                if (clienteRefresh?.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(clienteRefresh?.foto_perfil)
                    imageView.setImageURI(imageUri)
                }

                idFuncionarioSelected = idsFuncionariosGinasio[0]
                var selectFuncionario = findViewById<Spinner>(R.id.spinnerFuncionario)
                var optionsFuncionario = idsFuncionariosGinasio
                var adapterFuncionarios = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsFuncionario)
                adapterFuncionarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectFuncionario.adapter = adapterFuncionarios
                selectFuncionario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        idFuncionarioSelected = optionsFuncionario[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Do nothing
                    }
                }

                adapterFuncionarios.notifyDataSetChanged()
            }
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        // butao de marcar consulta, e volta a página inicial
        findViewById<Button>(R.id.buttonMark).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Marcacao_Consulta, MainMenuClienteActivity::class.java)

            var marcacaoType : String
            if (findViewById<RadioButton>(R.id.radioButton2).isChecked)
            {
                marcacaoType = findViewById<RadioButton>(R.id.radioButton2).text.toString()
            }
            else marcacaoType = findViewById<RadioButton>(R.id.radioButton).text.toString()

            var dateSelected = findViewById<EditText>(R.id.editTextDate).text.toString()
            var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            var dateSelectedFormated = LocalDateTime.parse(dateSelected, formatter)

            // TODO: SUBSTITUIR OS NULOS DO OBJETO ABAIXO
            // objeto enviado para o backend
            var newMarcacao = Marcacao(null,idFuncionarioSelected,clienteRefresh?.id_cliente,dateSelectedFormated,marcacaoType,"Ativo")
            MarcacaoRequests.PostChecked(lifecycleScope, sessionToken, newMarcacao) { resultMarcacao ->
                if (resultMarcacao == "User not found")
                {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta, "Error on marking an appointment", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Consulta,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

}