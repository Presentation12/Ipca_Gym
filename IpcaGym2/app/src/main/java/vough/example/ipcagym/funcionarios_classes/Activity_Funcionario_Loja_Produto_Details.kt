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

class Activity_Funcionario_Loja_Produto_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produto_details)

        var id_produto = intent.getIntExtra("id_produto", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var tipo_produto = intent.getStringExtra("tipo_produto")
        var preco = intent.getDoubleExtra("preco",0.0)
        var descricao = intent.getStringExtra("descricao")
        var estado_produto = intent.getStringExtra("estado_produto")
        var foto_produto = intent.getStringExtra("foto_produto")
        var quantidade_produto = intent.getIntExtra("quantidade_produto",-1)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

        if (foto_produto != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.imageViewProduto)
            val imageUri: Uri = Uri.parse(foto_produto)
            cliente_image_view.setImageURI(imageUri)
        }

        val nomeProduto = findViewById<TextView>(R.id.nomeProduto)
        nomeProduto.text = nome
        val tipoProduto = findViewById<TextView>(R.id.Tipo)
        tipoProduto.text = tipo_produto
        val precoProduto = findViewById<TextView>(R.id.Preco)
        precoProduto.text = preco.toString()
        val descricaoProduto = findViewById<TextView>(R.id.Descricao)
        descricaoProduto.text = descricao.toString()
        val estadoProduto = findViewById<TextView>(R.id.Estado)
        estadoProduto.text = estado_produto.toString()
        val quantidadeProduto = findViewById<TextView>(R.id.Quantidade)
        quantidadeProduto.text = quantidade_produto.toString()

        findViewById<Button>(R.id.buttonPlus).setOnClickListener{
            quantidade_produto += 1
        }
        findViewById<Button>(R.id.buttonMinus).setOnClickListener{
            quantidade_produto -= 1
        }

        findViewById<Button>(R.id.buttonRemover).setOnClickListener{
            val intent = Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Loja_Produtos::class.java)
            // TODO: remover da lista o produto
            startActivity(intent)
        }

        // TODO: linkagem
        findViewById<Button>(R.id.buttonEditar).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Loja_Produto_Edit::class.java)

            intent.putExtra("id_produto", id_produto)
            intent.putExtra("id_ginasio", id_ginasio)
            intent.putExtra("nome", nome)
            intent.putExtra("tipo_produto", tipo_produto)
            intent.putExtra("preco", preco)
            intent.putExtra("descricao", descricao)
            intent.putExtra("estado_produto", estado_produto)
            intent.putExtra("foto_produto", foto_produto)
            intent.putExtra("quantidade_produto", quantidade_produto)

            startActivity(intent)
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

    }
}