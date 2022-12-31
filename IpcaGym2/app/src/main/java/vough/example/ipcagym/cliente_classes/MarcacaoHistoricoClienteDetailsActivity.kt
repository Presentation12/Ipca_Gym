package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R

class MarcacaoHistoricoClienteDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_details)

        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_marcacao = intent.getStringExtra("data_marcacao")
        val descricao = intent.getStringExtra("descricao")
        val estado = intent.getStringExtra("estado")

        // TODO: fix quando houver linkagem
        findViewById<TextView>(R.id.data).text = data_marcacao
        //findViewById<TextView>(R.id.FuncionarioNome).text = nome_funcionario
        //findViewById<TextView>(R.id.GinasioNome).text = nome_ginasio
        findViewById<TextView>(R.id.Descricao).text = descricao

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic_cliente_marcacao_details)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteDetailsActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        findViewById<Button>(R.id.buttonCancelar).setOnClickListener {
            val intent = Intent(this@MarcacaoHistoricoClienteDetailsActivity, MarcacaoHistoricoClienteActivity::class.java)
            // TODO: quando houver linkagem alterar aqui o estado da marcação
            startActivity(intent)
        }
    }
}