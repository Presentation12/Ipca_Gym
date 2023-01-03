package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Carrinho_Pedido
import vough.example.ipcagym.data_classes.Loja
import java.time.LocalDateTime

class Activity_Cliente_Loja_Carrinho : AppCompatActivity() {

    /*
    var pedidos_carrinho_adapter = AdapterCarrinho()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_loja_carrinho)

        var quantidadeComprada = intent.getIntExtra("quantidadeComprada", -1)

        var id_produto = intent.getIntExtra("id_produto", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var tipo_produto = intent.getStringExtra("tipo_produto")
        var preco = intent.getDoubleExtra("preco",0.0)
        var descricao = intent.getStringExtra("descricao")
        var estado_produto = intent.getStringExtra("estado_produto")
        var foto_produto = intent.getStringExtra("foto_produto")
        var quantidade_produto = intent.getIntExtra("quantidade_produto",-1)

        //TODO: imagem
        val image_view = findViewById<ImageView>(R.id.profile_pic)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Cliente_Loja_Carrinho,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        image_view.setOnClickListener {
            spinner.performClick()
        }

        val list_view_produtos_carrinho = findViewById<ListView>(R.id.listviewProdutosCarrinho)
        list_view_produtos_carrinho.adapter = pedidos_carrinho_adapter

        var total_price = 0.0
        for (produto in carrinho) {
            total_price += produto.preco!!
        }
        findViewById<TextView>(R.id.textViewTotalPrice).text = total_price.toString()

        //TODO: Mandar para a função
        findViewById<Button>(R.id.buttonComprar).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Loja_Carrinho, Activity_Cliente_Loja::class.java)
            //mandar para o post
            carrinho.apply { clear() }
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonContinuar).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Loja_Carrinho, Activity_Cliente_Loja::class.java)
            intent.putExtra("carrinho", carrinho)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCancelar).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Loja_Carrinho, Activity_Cliente_Loja::class.java)
            carrinho.apply { clear() }
            startActivity(intent)
        }

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Carrinho,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Carrinho,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Carrinho,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Carrinho,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Carrinho,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterCarrinho : BaseAdapter(){
        override fun getCount(): Int {
            return carrinho.size
        }

        override fun getItem(position: Int): Any {
            return carrinho[position]
        }

        override fun getItemId(position: Int): Long {
            return carrinho[position].id_produto!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_produto,parent,false)

            val nomeProdutoView = root_view.findViewById<TextView>(R.id.text_view_nome)
            nomeProdutoView.text = carrinho[position].nome

            val precoProdutoView = root_view.findViewById<TextView>(R.id.text_view_preco)
            precoProdutoView.text = carrinho[position].preco.toString()

            if (carrinho[position].foto_produto != null)
            {
                val produto_image_view = root_view.findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(carrinho[position].foto_produto)
                produto_image_view.setImageURI(imageUri)
            }

            return root_view
        }

    }
    */
}