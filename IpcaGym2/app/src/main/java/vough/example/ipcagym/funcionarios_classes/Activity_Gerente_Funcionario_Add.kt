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

class Activity_Gerente_Funcionario_Add : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_create)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null) {
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

        val adapter = MyAdapter(this@Activity_Gerente_Funcionario_Add, options)
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
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Add, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Gerente_Funcionario_Add, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Gerente_Funcionario_Add, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
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

            var emptyFields = false
            var nome = ""
            var codigo : Int = -1

            if (!findViewById<EditText>(R.id.editTextFuncionarioNome).text.isEmpty())
            {
                nome = findViewById<EditText>(R.id.editTextFuncionarioNome).text.toString()
            }
            else emptyFields = true
            if (!findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty())
            {
                codigo = findViewById<EditText>(R.id.editTextCodigoFuncionario).text.toString().toInt()
            }
            else emptyFields = true
            val isAdmin = findViewById<CheckBox>(R.id.checkBoxisAdmin).isChecked

            if (!emptyFields)
            {
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
                    if(resultGerente != null) {
                        //TODO: FALTA METER FOTO
                        val newFuncionario = Funcionario(null,resultGerente.id_ginasio,nome,isAdmin,codigo,codigo.toString(),null,"Ativo", "photo")
                        FuncionarioRequests.Post(lifecycleScope,sessionToken,newFuncionario){ resultEditFuncionario ->
                            if (resultEditFuncionario == "User not found")
                            {
                                Toast.makeText(this@Activity_Gerente_Funcionario_Add, "Error on create an employee", Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                finish()
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
            else Toast.makeText(this@Activity_Gerente_Funcionario_Add,"Error: Empty fields",Toast.LENGTH_LONG).show()
        }
    }
}