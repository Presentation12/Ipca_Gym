package vough.example.ipcagym.funcionarios_classes
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import java.time.format.DateTimeFormatter

class PaginaInicialFuncionarioActivity: AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    //TODO: Substituir hardcode
    var funcionario = Funcionario(4,2,"Frederico Botelho",null,126789,"null","null","ativo")
    var classificacao = Classificacao(4,2,1,5,"Perfeito",null)

    val classifications_avaliation = classificacao.avaliacao?.let { intArrayOf(it) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_pagina_inicial)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val imageView = findViewById<ImageView>(R.id.profile_pic2)
        val spinner = findViewById<Spinner>(R.id.spinner2)
        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val name_view = findViewById<TextView>(R.id.textView5)
        name_view.text = funcionario.nome

        val classification_view = findViewById<TextView>(R.id.textView8)
        classification_view.text = classifications_avaliation?.average().toString()

        val classification_numbers = findViewById<TextView>(R.id.textView9)
        classification_numbers.text = classifications_avaliation?.sum().toString()

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_clients -> {
                    Toast.makeText(this@PaginaInicialFuncionarioActivity,"Clients", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_capacity -> {
                    Toast.makeText(this@PaginaInicialFuncionarioActivity,"Capacity", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_home -> {
                    Toast.makeText(this@PaginaInicialFuncionarioActivity,"Home", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@PaginaInicialFuncionarioActivity,"Shop", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@PaginaInicialFuncionarioActivity,"History", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }
}

