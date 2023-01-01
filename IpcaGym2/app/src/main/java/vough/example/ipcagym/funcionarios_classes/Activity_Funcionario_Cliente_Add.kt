package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.MainMenuClienteActivity
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Marcacao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Cliente_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_novo_cliente)


        val imageView = findViewById<ImageView>(R.id.profile_pic_funcionario_add_cliente)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Cliente_Add,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        // butao de adicionar cliente novo, e volta a página ida lista de clientes
        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Clientes_List::class.java)

            var nomeCliente = findViewById<EditText>(R.id.editTextClienteNome).text.toString()
            var mailCliente = findViewById<EditText>(R.id.editTextClienteMail).text.toString()
            //TODO: trocar por condições de verificacao de campos preenchidos
            var telemovelCliente : Int = 0
            if (findViewById<EditText>(R.id.editTextClienteTelemovel).text.isEmpty() == false)
            {
                telemovelCliente = findViewById<EditText>(R.id.editTextClienteTelemovel).text.toString().toInt()
            }

            // TODO: SUBSTITUIR OS NULOS e hardcodes DO OBJETO ABAIXO
            // objeto enviado para o backend
            var id_ginasio = 1
            var newCliente = Cliente(null,id_ginasio,0,nomeCliente,mailCliente,telemovelCliente,"","",null,null,null,null,"Ativo")

            startActivity(intent)
        }
    }
}