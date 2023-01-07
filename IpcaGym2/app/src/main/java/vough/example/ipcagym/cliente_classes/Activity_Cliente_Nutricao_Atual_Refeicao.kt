package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests
import vough.example.ipcagym.requests.RefeicaoRequests


class Activity_Cliente_Nutricao_Atual_Refeicao : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_refeicao)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_refeicao = intent.getIntExtra("id_refeicao", -1)
        val id_plano_nutricional = intent.getIntExtra("id_plano_nutricional", -1)
        val descricao = intent.getStringExtra("descricao")
        val hora = intent.getStringExtra("hora")
        val foto_refeicao = intent.getStringExtra("foto_refeicao")

        val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_refeicao_page)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

            if (resultCliente?.foto_perfil != null)
            {
                val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                imageView.setImageURI(imageUri)
            }
        }

        findViewById<TextView>(R.id.textViewHora).text = hora
        if (foto_refeicao != null)
        {
            val refeicao_image_view = findViewById<ImageView>(R.id.refeicao_pic)
            val imageUri: Uri = Uri.parse(foto_refeicao)
            refeicao_image_view.setImageURI(imageUri)
        }
        findViewById<TextView>(R.id.textViewDescricaoRefeicao).text = descricao

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener {
            spinner.performClick()
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Atual_Refeicao,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

}