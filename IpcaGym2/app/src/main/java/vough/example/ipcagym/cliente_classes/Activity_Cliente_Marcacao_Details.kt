package vough.example.ipcagym.cliente_classes

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
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Marcacao_Details : AppCompatActivity() {

    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var clienteRefresh : Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_details)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_marcacao_details)

        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        var data_marcacao = intent.getStringExtra("data_marcacao")
        val descricao = intent.getStringExtra("descricao")
        val estado = intent.getStringExtra("estado")

        var data_marcacao_formatado = LocalDateTime.parse(data_marcacao, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken) { resultCliente ->
            clienteRefresh = resultCliente
            if (clienteRefresh?.foto_perfil != null)
            {
                val imageUri: Uri = Uri.parse(resultCliente?.foto_perfil)
                imageView.setImageURI(imageUri)

                GinasioRequests.GetByID(lifecycleScope,sessionToken,clienteRefresh?.id_ginasio){ resultGinasio ->
                    findViewById<TextView>(R.id.GinasioNome).text = resultGinasio?.instituicao
                }
            }
        }

        FuncionarioRequests.GetByID(lifecycleScope,sessionToken,id_funcionario){ resultFuncionaio ->
            findViewById<TextView>(R.id.FuncionarioNome).text = resultFuncionaio?.nome
        }

        findViewById<TextView>(R.id.data).text = data_marcacao_formatado?.format(date_time_formatter)
        findViewById<TextView>(R.id.Descricao).text = descricao

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Cliente_Marcacao_Details,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        var butaoCancelar = findViewById<Button>(R.id.buttonCancelar)
        if (estado == "Ativo") butaoCancelar.visibility = View.VISIBLE
        butaoCancelar.setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Marcacao_Details, Activity_Cliente_Marcacoes::class.java)
            var editMarcacao = Marcacao(id_marcacao,id_funcionario,id_cliente,data_marcacao_formatado,descricao,"Cancelada")
            MarcacaoRequests.PatchCancelMarcacao(lifecycleScope, sessionToken, id_marcacao, editMarcacao) { resultCancelMarcacao ->
                if (resultCancelMarcacao == "User not found")
                {
                    Toast.makeText(this@Activity_Cliente_Marcacao_Details, "Error on canceling an appointment", Toast.LENGTH_LONG)
                }
                else
                {
                    startActivity(intent)
                }
            }
        }
    }
}