package vough.example.ipcagym.funcionarios_classes

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
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Gerente_Funcionario_Edit : AppCompatActivity() {

    var gerenteRefresh : Funcionario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_edit)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var is_admin = intent.getBooleanExtra("is_admin",false)
        var codigo = intent.getIntExtra("codigo",-1)
        val pass_salt = intent.getStringExtra("pass_salt")
        val pass_hash = intent.getStringExtra("pass_hash")
        val estado = intent.getStringExtra("estado")

        val image_view = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null) gerenteRefresh = resultGerente
            /* TODO: foto do gerente
            if (gerenteRefresh?.foto_perfil != null)
            {
                val imageUri: Uri = Uri.parse(gerenteRefresh?)
                imageView.setImageURI(imageUri)
            }
            */
        }

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

        // TODO: funcionario sem atributo foto
        /*
        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }
        */
        val editNomeFuncionario = findViewById<TextView>(R.id.editTextNomeFuncionario)
        editNomeFuncionario.hint = nome
        val editCodigoFuncionario = findViewById<TextView>(R.id.editTextCodigoFuncionario)
        editCodigoFuncionario.hint = codigo.toString()
        val editIsAdmin = findViewById<RadioButton>(R.id.radioButton3)
        if(is_admin == true) editIsAdmin.isChecked = true

        // butao de guardar funcionário editado, e volta a página ida lista de funcionarios
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Funcionarios_List::class.java)

            if (findViewById<EditText>(R.id.editTextNomeFuncionario).text.isEmpty() == false)
            {
                nome = editNomeFuncionario.text.toString()
            }
            if (findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty() == false)
            {
                codigo = editCodigoFuncionario.text.toString().toInt()
            }
            is_admin = editIsAdmin.isChecked

            val funcionarioEditado = Funcionario(id_funcionario,id_ginasio,nome,is_admin,codigo,pass_salt,pass_hash,estado)
            FuncionarioRequests.Patch(lifecycleScope,sessionToken,id_funcionario,funcionarioEditado){ resultEditFuncionario ->
                if (resultEditFuncionario == "User not found")
                {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit, "Error on edit an employee", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
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