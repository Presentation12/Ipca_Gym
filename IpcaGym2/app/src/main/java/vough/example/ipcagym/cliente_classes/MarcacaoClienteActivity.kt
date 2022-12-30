package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Marcacao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MarcacaoClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_consulta)

        // TODO: SUBSTITUIR HARDCODES
        // Hardcode lista de ids de funcionarios do ginasio
        var idFuncionarioSelected: Int = -1
        val selectFuncionario = findViewById<Spinner>(R.id.spinnerFuncionario)
        val optionsFuncionario = arrayOf(1, 2, 3)
        val adapterFuncionarios = ArrayAdapter(this, android.R.layout.simple_spinner_item, optionsFuncionario)
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val imageView = findViewById<ImageView>(R.id.profile_pic_marcacao_page)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MarcacaoClienteActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@MarcacaoClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@MarcacaoClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@MarcacaoClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@MarcacaoClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@MarcacaoClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        // butao de marcar consulta, e volta a página inicial
        findViewById<Button>(R.id.buttonMark).setOnClickListener {
            val intent = Intent(this@MarcacaoClienteActivity, MainMenuClienteActivity::class.java)

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
            var newMarcacao = Marcacao(null,idFuncionarioSelected,null,dateSelectedFormated,marcacaoType,"Ativo")

            startActivity(intent)
        }
    }

}