package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario

class Activity_Gerente_Funcionario_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_create)

        // TODO: funcionario sem atributo foto para o gerente e funcionario a editar
        val imageView = findViewById<ImageView>(R.id.profile_pic)
        /*
        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }
        */

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Gerente_Funcionario_Add,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        // butao de adicionar funcionario novo, e volta a página ida lista de funcionários
        findViewById<Button>(R.id.buttonAddFuncionario).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Add, Activity_Gerente_Funcionarios_List::class.java)
            
            //TODO: trocar por condições de verificacao de campos preenchidos
            var nome : String
            var codigo : Int
            var is_admin : Boolean

            if (findViewById<EditText>(R.id.editTextFuncionarioNome).text.isEmpty() == false )
            {
                nome = findViewById<EditText>(R.id.editTextFuncionarioNome).text.toString()
            }
            else nome = "Default"
            if (findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty() == false)
            {
                codigo = findViewById<EditText>(R.id.editTextCodigoFuncionario).text.toString().toInt()
            }
            else codigo = 0
            if (findViewById<RadioButton>(R.id.radioButton3).isChecked)
            {
                is_admin = true
            }
            else is_admin = false

            // TODO: Manda objeto com novas mudanças para o patch do backend e trocar os nulos
            var funcionarioEditado = Funcionario(null,null,nome,is_admin,codigo,null,null,"Ativo")
            startActivity(intent)
        }
    }
}