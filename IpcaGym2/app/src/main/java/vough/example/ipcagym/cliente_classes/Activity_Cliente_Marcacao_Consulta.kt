package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Marcacao_Consulta : AppCompatActivity() {

    var idsFuncionariosGinasio = ArrayList<Int?>()
    var idFuncionarioSelected : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_consulta)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_marcacao_page)
        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null){

                FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, resultCliente?.id_ginasio) { resultFuncionario ->
                    if(resultFuncionario.isNotEmpty()){
                        for (x in resultFuncionario)
                        {
                            idsFuncionariosGinasio.add(x.id_funcionario)
                        }

                        if (resultCliente?.foto_perfil != null)
                        {
                            val imageUri: Uri = Uri.parse(resultCliente?.foto_perfil)
                            imageView.setImageURI(imageUri)
                        }

                        idFuncionarioSelected = idsFuncionariosGinasio[0]!!
                        //Select Funcionario != null
                        val selectFuncionario = findViewById<Spinner>(R.id.spinnerFuncionario)
                        val optionsFuncionario = idsFuncionariosGinasio
                        val adapterFuncionarios = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsFuncionario)
                        adapterFuncionarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        selectFuncionario.adapter = adapterFuncionarios

                        selectFuncionario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                idFuncionarioSelected = optionsFuncionario[position]!!
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Do nothing
                            }
                        }

                        adapterFuncionarios.notifyDataSetChanged()
                    }
                }
            }
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Marcacao_Consulta, options)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        if (counter == 0) {
                            counter += 1
                            spinner.setSelection(3)
                        } else {
                            startActivity(
                                Intent(
                                    this@Activity_Cliente_Marcacao_Consulta,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Marcacao_Consulta,
                                Activity_Cliente_Definitions::class.java
                            )
                        )
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences =
                            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Marcacao_Consulta,
                                Activity_Cliente_Login::class.java
                            )
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        // butao de marcar consulta, e volta a página inicial
        findViewById<Button>(R.id.buttonMark).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Marcacao_Consulta, Activity_Cliente_Marcacoes::class.java)

            var marcacaoType : String
            if (findViewById<RadioButton>(R.id.radioButton2).isChecked)
            {
                marcacaoType = findViewById<RadioButton>(R.id.radioButton2).text.toString()
            }
            else marcacaoType = findViewById<RadioButton>(R.id.radioButton).text.toString()

            var dateSelected = findViewById<EditText>(R.id.editTextDate).text.toString()
            var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            var dateSelectedFormated = LocalDateTime.parse(dateSelected, formatter)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
                if(resultCliente != null){
                    var newMarcacao = Marcacao(null, idFuncionarioSelected, resultCliente?.id_cliente,dateSelectedFormated,marcacaoType,"Ativo")
                    // objeto enviado para o backend
                    MarcacaoRequests.PostChecked(lifecycleScope, sessionToken, newMarcacao) { resultMarcacao ->
                        if (resultMarcacao == "User not found")
                            Toast.makeText(this@Activity_Cliente_Marcacao_Consulta, "Error on marking an appointment", Toast.LENGTH_LONG).show()
                        else
                        {
                            finish()
                            startActivity(intent)
                        }
                    }
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