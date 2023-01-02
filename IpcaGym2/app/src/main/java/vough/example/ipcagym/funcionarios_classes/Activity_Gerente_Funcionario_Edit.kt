package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario

class Activity_Gerente_Funcionario_Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_edit)

        var id_funcionario = intent.getIntExtra("id_funcionario", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var is_admin = intent.getBooleanExtra("is_admin",false)
        var codigo = intent.getIntExtra("codigo",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var estado = intent.getStringExtra("estado")

        // TODO: funcionario sem atributo foto para o gerente e funcionario a editar
        val image_view = findViewById<ImageView>(R.id.profile_pic)
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
                Toast.makeText(this@Activity_Gerente_Funcionario_Edit,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<TextView>(R.id.editTextNomeFuncionario).hint = nome
        findViewById<TextView>(R.id.editTextCodigoFuncionario).hint = codigo.toString()

        // butao de guardar funcionário editado, e volta a página ida lista de funcionarios
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Funcionarios_List::class.java)

            //TODO: trocar por condições de verificacao de campos preenchidos
            if (findViewById<EditText>(R.id.editTextNomeFuncionario).text.isEmpty() == false)
            {
                nome = findViewById<EditText>(R.id.editTextNomeFuncionario).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty() == false)
            {
                codigo = findViewById<EditText>(R.id.editTextCodigoFuncionario).text.toString().toInt()
            }
            if (findViewById<RadioButton>(R.id.radioButton3).isChecked)
            {
                is_admin = true
            }
            else is_admin = false

            // TODO: Manda objeto com novas mudanças para o patch do backend
            var funcionarioEditado = Funcionario(id_funcionario,id_ginasio,nome,is_admin,codigo,pass_salt,pass_hash,estado)
            startActivity(intent)
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }
}