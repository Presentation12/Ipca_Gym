package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.data_classes.Refeicao
import java.time.LocalTime


class NutricaoRefeicaoClienteActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_refeicao)

        val id_refeicao = intent.getIntExtra("id_refeicao", -1)
        val id_plano_nutricional = intent.getIntExtra("id_plano_nutricional", -1)
        val descricao = intent.getStringExtra("descricao")
        val hora = intent.getStringExtra("hora")
        val foto_refeicao = intent.getStringExtra("foto_refeicao")

        findViewById<TextView>(R.id.textViewHora).text = hora
        if (foto_refeicao != null)
        {
            val refeicao_image_view = findViewById<ImageView>(R.id.refeicao_pic)
            val imageUri: Uri = Uri.parse(foto_refeicao)
            refeicao_image_view.setImageURI(imageUri)
        }
        findViewById<TextView>(R.id.textViewDescricaoRefeicao).text = descricao


        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic_cliente_refeicao_page)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@NutricaoRefeicaoClienteActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@NutricaoRefeicaoClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@NutricaoRefeicaoClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@NutricaoRefeicaoClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@NutricaoRefeicaoClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@NutricaoRefeicaoClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

}