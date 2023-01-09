package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.*
import vough.example.ipcagym.requests.*
import java.time.LocalDateTime

class Cliente_classificacao_activity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_avaliar)

        var clienteRefresh: Cliente? = null

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken) { resultCliente ->
            if (resultCliente != null) clienteRefresh = resultCliente

            val spinner = findViewById<Spinner>(R.id.spinner_avalicao)
            val options = arrayOf("Conta", "Definições", "Sair")
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ result ->
                if(result != null)
                    findViewById<TextView>(R.id.textView_nome_cliente_avaliacao).text = result.nome
            }

            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(
                        this@Cliente_classificacao_activity,
                        options[position],
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }



            findViewById<Button>(R.id.buttom_starts_gym).setOnClickListener() {
                startActivity(
                    Intent(
                        this@Cliente_classificacao_activity,
                        Activity_Cliente_Account::class.java
                    )
                )
            }

                findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener {
                val intent = Intent(this@Cliente_classificacao_activity, PaginaInicialClienteActivity::class.java)

                var descricao = ""
                var emptyFields = false
                var rating : Int = -1

                if (!findViewById<EditText>(R.id.campo_comentario).text.isEmpty())
                {
                    descricao = findViewById<EditText>(R.id.campo_comentario).text.toString()
                }
                else emptyFields = true
                //TODO: Valores entre 1 e 5
                if (!findViewById<EditText>(R.id.campo_rating).text.isEmpty())
                {
                    rating = findViewById<EditText>(R.id.campo_rating).text.toString().toInt()
                }
                else emptyFields = true

                if (!emptyFields)
                {

                    ClienteRequests.GetByToken(lifecycleScope, sessionToken){ result ->
                        if(result != null){
                            val newClassificacao = Classificacao(null,clienteRefresh?.id_ginasio,clienteRefresh?.id_cliente,rating,descricao,LocalDateTime.now())

                            ClassificacaoRequests.Post(lifecycleScope,sessionToken,newClassificacao){ resultEditClassificacao ->
                                if (resultEditClassificacao == "User not found")
                                    Toast.makeText(this@Cliente_classificacao_activity, "Error on create an rating", Toast.LENGTH_LONG).show()
                                else
                                {
                                    finish()
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
                else Toast.makeText(this@Cliente_classificacao_activity,"Error: Empty fields",Toast.LENGTH_LONG).show()
            }
        }
    }
}