package vough.example.ipcagym.funcionarios_classes

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
import vough.example.ipcagym.requests.LojaRequests

class Activity_Funcionario_Loja_Produto_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produto_details)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

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

        findViewById<Button>(R.id.buttonRemover).setOnClickListener{
            val intent = Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Loja_Produtos::class.java)
            LojaRequests.Delete(lifecycleScope,sessionToken,id_produto){ resultProdutoRemove ->
                if (resultProdutoRemove == "Error: Delete Product fails")
                {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Details, "Error: Remove fail", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
        }

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

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Loja_Produto_Details, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if(counter == 0){
                            counter+=1
                            spinner.setSelection(3)
                        }
                        else{
                            startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        imageView.setOnClickListener{ spinner.performClick() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}