package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.LojaRequests

class Activity_Cliente_Loja_Produto_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_loja_produto_details)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_produto = intent.getIntExtra("id_produto", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val nome = intent.getStringExtra("nome")
        val tipo_produto = intent.getStringExtra("tipo_produto")
        val preco = intent.getDoubleExtra("preco",0.0)
        val descricao = intent.getStringExtra("descricao")
        val estado_produto = intent.getStringExtra("estado_produto")
        val foto_produto = intent.getStringExtra("foto_produto")
        val quantidade_produto = intent.getIntExtra("quantidade_produto",-1)

        var imageView = findViewById<ImageView>(R.id.profile_pic_produto_details)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null)
            {
                if (resultCliente.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    imageView.setImageURI(imageUri)
                }
            }
        }

        if (foto_produto != null)
        {
            val produto_image_view = findViewById<ImageView>(R.id.imageViewProduto)
            val imageUri: Uri = Uri.parse(foto_produto)
            produto_image_view.setImageURI(imageUri)
        }
        findViewById<TextView>(R.id.nomeProduto).text = nome
        findViewById<TextView>(R.id.Tipo).text = tipo_produto
        findViewById<TextView>(R.id.Preco).text = preco.toString()
        findViewById<TextView>(R.id.Descricao).text = descricao

        findViewById<Button>(R.id.buttonBuy).setOnClickListener {
            val newIntent = Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Loja_Produtos::class.java)

            var quantidadeComprada = findViewById<EditText>(R.id.editTextQuantity).text.toString().toInt()

            newIntent.putExtra("quantidadeComprada", quantidadeComprada)
            newIntent.putExtra("id_produto", id_produto)
            newIntent.putExtra("id_ginasio", id_ginasio)
            newIntent.putExtra("nome", nome)
            newIntent.putExtra("tipo_produto", tipo_produto)
            newIntent.putExtra("preco", preco)
            newIntent.putExtra("descricao", descricao)
            newIntent.putExtra("estado_produto", estado_produto)
            newIntent.putExtra("foto_produto", foto_produto)
            newIntent.putExtra("quantidade_produto", quantidade_produto)

            setResult(RESULT_OK, newIntent)
            finish()
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener{
            val newIntent = Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Loja_Produtos::class.java)
            setResult(RESULT_CANCELED, newIntent)
            finish()
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produto_Details,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produto_Details,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produto_Details,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produto_Details,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produto_Details,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

    }
}