package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.MarcacaoHistoricoClienteActivity
import vough.example.ipcagym.data_classes.Cliente

class Activity_Funcionario_Cliente_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_detalhes_cliente)

        var id_cliente = intent.getIntExtra("id_cliente", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var id_plano_nutricional = intent.getIntExtra("id_plano_nutricional", -1)
        var nome = intent.getStringExtra("nome")
        var mail = intent.getStringExtra("mail")
        var telemovel = intent.getIntExtra("telemovel",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var peso = intent.getDoubleExtra("peso",0.0)
        var altura = intent.getIntExtra("altura",-1)
        var gordura = intent.getDoubleExtra("gordura",0.0)
        var foto_perfil = intent.getStringExtra("foto_perfil")
        var estado = intent.getStringExtra("estado")

        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }

        // TODO: quando houver linkagem alterar aqui para remover cliente
        findViewById<Button>(R.id.button_apagar).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Clientes_List::class.java)
            estado = "Inativo"
            startActivity(intent)
        }

        val nomeCliente = findViewById<TextView>(R.id.textViewNomeCliente)
        nomeCliente.text = nome
        val mailCliente = findViewById<TextView>(R.id.textViewMail)
        mailCliente.text = mail
        val contactoCliente = findViewById<TextView>(R.id.textViewContacto)
        contactoCliente.text = telemovel.toString()
        val pesoCliente = findViewById<TextView>(R.id.textViewPeso)
        pesoCliente.text = peso.toString()
        val alturaCliente = findViewById<TextView>(R.id.textViewAltura)
        alturaCliente.text = altura.toString()
        val gorduraCliente = findViewById<TextView>(R.id.textViewGordura)
        gorduraCliente.text = gordura.toString()

        // TODO: linkagem
        findViewById<Button>(R.id.button_alterar).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Cliente_Edit::class.java)

            intent.putExtra("id_cliente", id_cliente)
            intent.putExtra("id_ginasio", id_ginasio)
            intent.putExtra("id_plano_nutricional", id_plano_nutricional)
            intent.putExtra("nome", nome)
            intent.putExtra("mail", mail)
            intent.putExtra("telemovel", telemovel)
            intent.putExtra("pass_salt", pass_salt)
            intent.putExtra("pass_hash", pass_hash)
            intent.putExtra("foto_perfil", foto_perfil)
            intent.putExtra("peso", peso)
            intent.putExtra("altura", altura)
            intent.putExtra("estado", estado)

            startActivity(intent)
        }

        //TODO: Atribuir valores quando houver link
        var media_entradas_anuais = findViewById<TextView>(R.id.textViewMediaEntradasAnuais)
        var media_entradas_mensais = findViewById<TextView>(R.id.textViewMediaEntradasMensais)
        var entradas_mes_atual = findViewById<TextView>(R.id.textViewEntradasMesAtual)

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

    }

}