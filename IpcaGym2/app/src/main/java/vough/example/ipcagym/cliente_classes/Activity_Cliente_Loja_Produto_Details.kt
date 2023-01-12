package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
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
            if(resultCliente != null && resultCliente.foto_perfil.toString() != "null"){
                val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageView.setImageBitmap(bitmap)
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

        LojaRequests.GetByID(lifecycleScope, sessionToken, id_produto) {
            if (it != null && it.foto_produto.toString() != "null") {
                val pictureByteArray = Base64.decode(it.foto_produto, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.imageViewProduto).setImageBitmap(bitmap)
            }
        }

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
            newIntent.putExtra("quantidade_produto", quantidade_produto)

            setResult(RESULT_OK, newIntent)
            finish()
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener{
            val newIntent = Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Loja_Produtos::class.java)
            setResult(RESULT_CANCELED, newIntent)
            finish()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Loja_Produto_Details, options)

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
                                    this@Activity_Cliente_Loja_Produto_Details,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Loja_Produto_Details,
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
                                this@Activity_Cliente_Loja_Produto_Details,
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produto_Details, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}