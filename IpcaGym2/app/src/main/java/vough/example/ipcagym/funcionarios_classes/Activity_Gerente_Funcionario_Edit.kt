package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Gerente_Funcionario_Edit : AppCompatActivity() {
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
        val foto_funcionario = intent.getStringExtra("foto_funcionario")

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null){
                if (resultGerente.foto_funcionario != null)
                {
                    val imageUri: Uri = Uri.parse(resultGerente.foto_funcionario)
                    imageView.setImageURI(imageUri)
                }
            }
        }

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Gerente_Funcionario_Edit, options)
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
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        if (foto_funcionario != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_funcionario_pic)
            val imageUri: Uri = Uri.parse(foto_funcionario)
            cliente_image_view.setImageURI(imageUri)
        }

        val editNomeFuncionario = findViewById<TextView>(R.id.editTextNomeFuncionario)
        editNomeFuncionario.hint = nome
        val editCodigoFuncionario = findViewById<TextView>(R.id.editTextCodigoFuncionario)
        editCodigoFuncionario.hint = codigo.toString()
        val editIsAdmin = findViewById<CheckBox>(R.id.checkBoxIsAdminEdit)
        if(is_admin == true) editIsAdmin.isChecked = true

        // butao de guardar funcionário editado, e volta a página ida lista de funcionarios
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Funcionarios_List::class.java)

            if (!findViewById<EditText>(R.id.editTextNomeFuncionario).text.isEmpty())
            {
                nome = editNomeFuncionario.text.toString()
            }
            if (!findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty())
            {
                codigo = editCodigoFuncionario.text.toString().toInt()
            }
            is_admin = editIsAdmin.isChecked

            val funcionarioEditado = Funcionario(id_funcionario,id_ginasio,nome,is_admin,codigo,pass_salt,pass_hash,estado, foto_funcionario)
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