package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.*
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Login
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Perfil_Edit
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Settings
import vough.example.ipcagym.requests.*
import java.time.LocalDateTime

class Activity_Cliente_Avaliar : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_avaliar)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken) { resultCliente ->
            if (resultCliente != null) {

                if (resultCliente.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    imageView.setImageURI(imageUri)
                }
            }
        }



        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner_avalicao)

        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Avaliar, options)

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
                                    this@Activity_Cliente_Avaliar,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Avaliar,
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
                                this@Activity_Cliente_Avaliar,
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

        findViewById<Button>(R.id.buttom_starts_gym).setOnClickListener() {
            startActivity(
                Intent(
                    this@Activity_Cliente_Avaliar,
                    Activity_Cliente_Account::class.java
                )
            )
        }

        findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener {
            val intent =
                Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Pagina_Inicial::class.java)

            var descricao = ""
            var emptyFields = false
            var rating: Int = -1

            if (!findViewById<EditText>(R.id.campo_comentario).text.isEmpty()) {
                descricao = findViewById<EditText>(R.id.campo_comentario).text.toString()
            } else emptyFields = true
            //TODO: Valores entre 1 e 5
            if (!findViewById<EditText>(R.id.campo_rating).text.isEmpty()) {
                rating = findViewById<EditText>(R.id.campo_rating).text.toString().toInt()
            } else emptyFields = true
            if (!emptyFields) {

                ClienteRequests.GetByToken(lifecycleScope, sessionToken) { result ->
                    if (result != null) {
                        val newClassificacao = Classificacao(
                            null,
                            result.id_ginasio,
                            result.id_cliente,
                            rating,
                            descricao,
                            LocalDateTime.now()
                        )

                        ClassificacaoRequests.Post(
                            lifecycleScope,
                            sessionToken,
                            newClassificacao
                        ) { resultInsertClassificacao ->
                            if (resultInsertClassificacao == "Error: Post Classification fails")
                                Toast.makeText(
                                    this@Activity_Cliente_Avaliar,
                                    "Error on create an rating",
                                    Toast.LENGTH_LONG
                                ).show()
                            else {
                                finish()
                                startActivity(intent)
                            }
                        }
                    }
                }
            } else Toast.makeText(
                this@Activity_Cliente_Avaliar,
                "Error: Empty fields",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
