package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests

class Activity_Funcionario_Cliente_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_novo_cliente)


        val imageView = findViewById<ImageView>(R.id.profile_pic_funcionario_add_cliente)

        //Get token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Cliente_Add, options)
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
                                    this@Activity_Funcionario_Cliente_Add,
                                    Activity_Funcionario_Perfil_Edit::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Funcionario_Cliente_Add,
                                Activity_Funcionario_Settings::class.java
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
                                this@Activity_Funcionario_Cliente_Add,
                                Activity_Funcionario_Login::class.java
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

        //Butão para que , clicando , adiciona um cliente novo
        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Pagina_Inicial::class.java)

            var emptyFields = false
            var nome = ""
            var email = ""
            var contacto = -1

            if (!findViewById<EditText>(R.id.editTextClienteNome).text.isEmpty())
            {
                nome = findViewById<EditText>(R.id.editTextClienteNome).text.toString()
            }
            else emptyFields = true
            if (!findViewById<EditText>(R.id.editTextClienteMail).text.isEmpty())
            {
                email = findViewById<EditText>(R.id.editTextClienteMail).text.toString()
            }
            else emptyFields = true
            if (!findViewById<EditText>(R.id.editTextClienteTelemovel).text.isEmpty())
            {
                contacto = findViewById<EditText>(R.id.editTextClienteTelemovel).text.toString().toInt()
            }
            else emptyFields = true

            if (!emptyFields)
            {
                FuncionarioRequests.GetByToken(lifecycleScope,sessionToken){ resultFuncionarioGetByToken ->
                val RegistClient = Cliente(null,resultFuncionarioGetByToken?.id_ginasio,null,nome,email,contacto,contacto.toString(),null,null,null,null,null,"Ativo")
                    ClienteRequests.Post(lifecycleScope,sessionToken,RegistClient){ resultAddClient ->
                        if (resultAddClient == "Error: Post Client fails")
                        {
                            Toast.makeText(this@Activity_Funcionario_Cliente_Add, "Error on create an client", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            finish()
                            startActivity(intent)
                        }
                    }
                }
            }
            else Toast.makeText(this@Activity_Funcionario_Cliente_Add,"Error: Empty fields",Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_clients);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Add, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}