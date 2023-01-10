package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.data_classes.Refeicao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests
import vough.example.ipcagym.requests.PedidoLojaRequests

class Activity_Cliente_Pagina_Inicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_pagina_inicial)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken) { resultCliente ->

            //if (resultCliente?.foto_perfil != null)
            //{
            //    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
            //   imageView.setImageURI(imageUri)
            //}

            val name_view = findViewById<TextView>(R.id.textView7)
            name_view.text = resultCliente?.nome

            GinasioRequests.GetByID(lifecycleScope, sessionToken, resultCliente?.id_ginasio) { resultGym ->
                if (resultGym != null) {
                    if(resultGym.lotacao == resultGym.lotacaoMax){
                        findViewById<TextView>(R.id.textView_capacidade_atual).setTextColor(Color.RED)
                    }
                    findViewById<TextView>(R.id.textView_capacidade_atual).text = "${resultGym.lotacao} / ${resultGym.lotacaoMax} "
                }
            }
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Pagina_Inicial, options)

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
                                    this@Activity_Cliente_Pagina_Inicial,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Pagina_Inicial,
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
                                this@Activity_Cliente_Pagina_Inicial,
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

        imageView.setOnClickListener{ spinner.performClick() }
        //TODO: Criar um layout para mostrar informação sobre nós
        //findViewById<Button>(R.id.button_sobreNos).setOnClickListener {
            //val intent = Intent(this@Activity_Cliente_Pagina_Inicial, ::class.java)
            //startActivity(intent)

        findViewById<CardView>(R.id.cardView_loja).setOnClickListener {
            val intent = Intent(
                this@Activity_Cliente_Pagina_Inicial,
                Activity_Cliente_Loja_Produtos::class.java
            )
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardView_planos_nutricao).setOnClickListener{
            val intent = Intent(this@Activity_Cliente_Pagina_Inicial,Activity_Cliente_Nutricao_Planos ::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_view_team).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Pagina_Inicial, Activity_Cliente_OurTeam::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_avaliar).setOnClickListener{
            val intent = Intent(this@Activity_Cliente_Pagina_Inicial,Activity_Cliente_Avaliar :: class.java)
            startActivity(intent)
        }
    }
}