package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R

class Activity_Gerente_Funcionario_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_details)

        var id_funcionario = intent.getIntExtra("id_funcionario", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var is_admin = intent.getBooleanExtra("is_admin",false)
        var codigo = intent.getIntExtra("codigo",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var estado = intent.getStringExtra("estado")

        // TODO: funcionario sem atributo foto
        /*
        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }
        */

        // TODO: quando houver linkagem alterar aqui para remover funcionario
        findViewById<Button>(R.id.buttonRemover).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Funcionarios_List::class.java)
            estado = "Inativo"
            startActivity(intent)
        }

        val nomeFuncionario = findViewById<TextView>(R.id.nomeFuncionario)
        nomeFuncionario.text = nome
        val idFuncionario = findViewById<TextView>(R.id.IdFuncionario)
        idFuncionario.text = id_funcionario.toString()
        //TODO: trocar id ginasio por nome quando houver link
        val nomeGinasio = findViewById<TextView>(R.id.GinasioNome)
        nomeGinasio.text = id_ginasio.toString()
        val codigoFuncionario = findViewById<TextView>(R.id.Codigo)
        codigoFuncionario.text = codigo.toString()
        val estadoFuncionario = findViewById<TextView>(R.id.Estado)
        estadoFuncionario.text = estado

        // TODO: linkagem
        findViewById<Button>(R.id.buttonEditar).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Funcionario_Edit::class.java)

            intent.putExtra("id_funcionario", id_funcionario)
            intent.putExtra("id_ginasio", id_ginasio)
            intent.putExtra("nome", nome)
            intent.putExtra("is_admin", is_admin)
            intent.putExtra("codigo", codigo)
            intent.putExtra("pass_salt", pass_salt)
            intent.putExtra("pass_hash", pass_hash)
            intent.putExtra("estado", estado)

            startActivity(intent)
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

    }
}