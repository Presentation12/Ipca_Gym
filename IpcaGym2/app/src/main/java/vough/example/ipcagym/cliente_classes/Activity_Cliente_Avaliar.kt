package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
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

                if (resultCliente.foto_perfil  != null && resultCliente.foto_perfil != "null")
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
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

        findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Pagina_Inicial::class.java)

            var descricao = ""
            var emptyFields = false
            var invalid = false
            var rating: Int = -1

            if (!findViewById<EditText>(R.id.campo_comentario).text.isEmpty())
            {
                descricao = findViewById<EditText>(R.id.campo_comentario).text.toString()
            }
            else emptyFields = true

            if (!findViewById<EditText>(R.id.campo_rating).text.isEmpty())
            {
                rating = findViewById<EditText>(R.id.campo_rating).text.toString().toInt()
            }
            else if(findViewById<EditText>(R.id.campo_rating).text.toString().toInt() > 5 || findViewById<EditText>(R.id.campo_rating).text.toString().toInt() < 1)
            {
                invalid = true
            }
            else emptyFields = true



            if (emptyFields)
            {
                Toast.makeText(this@Activity_Cliente_Avaliar, "Error: Empty fields", Toast.LENGTH_LONG).show()
            }
            else if (invalid)
            {
                Toast.makeText(this@Activity_Cliente_Avaliar, "Error: Invalid Number", Toast.LENGTH_LONG).show()
            }
            else{

                ClienteRequests.GetByToken(lifecycleScope, sessionToken) { result ->
                    if (result != null) {
                        val newClassificacao = Classificacao(null, result.id_ginasio, result.id_cliente, rating, descricao, LocalDateTime.now())

                        ClassificacaoRequests.Post(lifecycleScope, sessionToken, newClassificacao) { resultInsertClassificacao ->
                            if (resultInsertClassificacao == "Error: Post Classification fails")
                                Toast.makeText(this@Activity_Cliente_Avaliar, "Error on create an rating", Toast.LENGTH_LONG).show()
                            else {
                                finish()
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Avaliar, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}
