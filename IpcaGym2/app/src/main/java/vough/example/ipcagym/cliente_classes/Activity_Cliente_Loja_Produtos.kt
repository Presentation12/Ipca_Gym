package vough.example.ipcagym.cliente_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.*
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.LojaRequests
import vough.example.ipcagym.requests.PedidoLojaRequests
import vough.example.ipcagym.requests.PedidoRequests
import java.time.LocalDate
import java.time.LocalDateTime

class Activity_Cliente_Loja_Produtos : AppCompatActivity() {

    var carrinho = ArrayList<Pedido_Join>()
    var carrinho_adapter = AdapterCarrinho()

    var produtos_list = arrayListOf<Loja>()
    var produtos_adapter = AdapterLoja()

    //receiver
    var receiverNewData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_loja)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null)
            {
                if (resultCliente.foto_perfil != null && resultCliente.foto_perfil.toString() != "null")
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                LojaRequests.GetAllByGinasioID(lifecycleScope, sessionToken,resultCliente.id_ginasio){ resultProdutosGinasio ->
                    if(resultProdutosGinasio.isNotEmpty()){
                        for(i in resultProdutosGinasio){
                            if(i.estado_produto == "Ativo"){
                                produtos_list.add(i)
                                produtos_adapter.notifyDataSetChanged()
                            }
                        }

                    }
                }
            }
        }

        val carrinho_view = findViewById<ImageView>(R.id.imageViewCarrinho)
        val spinner_carrinho = findViewById<Spinner>(R.id.spinnerCarrinho)
        spinner_carrinho.adapter = carrinho_adapter

        spinner_carrinho.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        carrinho_view.setOnClickListener {

            spinner_carrinho.performClick()
        }

        findViewById<TextView>(R.id.buttonCancelCart).setOnClickListener {
            carrinho.clear()
            carrinho_adapter.notifyDataSetChanged()
            findViewById<Button>(R.id.buttonBuyCart).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.textViewTotalPrice).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.buttonCancelCart).visibility = View.INVISIBLE
        }

        //TODO: Verify buy cart que da post de um peiddo novo e de todos os pedidos de produto
        findViewById<Button>(R.id.buttonBuyCart).setOnClickListener {

            var intent = Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Loja_Pedidos::class.java)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

                // post de pedido
                var newPedido = Pedido(null,resultCliente?.id_cliente, LocalDateTime.now(),"Ativo")
                PedidoRequests.Post(lifecycleScope,sessionToken,newPedido){ resultAddPedido ->
                    if (resultAddPedido == "Error: Post Pedido fails")
                        Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Error: Post Pedido fails", Toast.LENGTH_LONG).show()
                    else
                    {
                        // buscar id do pedido criado atraves do ultimo pedido dos pedidos do cliente
                        PedidoRequests.GetAllByClienteID(lifecycleScope,sessionToken,resultCliente?.id_cliente){ resultPedidos ->

                            val lastPedido = resultPedidos.last()

                            // converto o carrinho em pedido loja
                            var allPedidosProdutos = arrayListOf<Pedido_Loja>()
                            for (p in carrinho)
                            {
                                allPedidosProdutos.add(Pedido_Loja(lastPedido.id_pedido,p.id_produto, p.quantidade_pedido))
                            }
                            // post de todos os pedidos de cada produto do carrinho a base de dados
                            for(p in allPedidosProdutos)
                            {
                                PedidoLojaRequests.PostPedidoChecked(lifecycleScope,sessionToken,p){ resultAddPedidoLoja ->
                                    if (resultAddPedido == "Error: Post PedidoLoja fails")
                                    {
                                        Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Error: Post PedidoLoja fails", Toast.LENGTH_LONG).show()
                                    }
                                    else
                                    {
                                        finish()
                                        startActivity(intent)
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                if (it.resultCode == Activity.RESULT_OK) {

                    var quantidadeComprada = it.data?.getIntExtra("quantidadeComprada", -1)
                    var id_produto = it.data?.getIntExtra("id_produto", -1)
                    var id_ginasio = it.data?.getIntExtra("id_ginasio", -1)
                    var nome = it.data?.getStringExtra("nome")
                    var tipo_produto = it.data?.getStringExtra("tipo_produto")
                    var preco = it.data?.getDoubleExtra("preco", 0.0)
                    var descricao = it.data?.getStringExtra("descricao")
                    var estado_produto = it.data?.getStringExtra("estado_produto")
                    var quantidade_produto = it.data?.getIntExtra("quantidade_produto", -1)

                    LojaRequests.GetByID(lifecycleScope, sessionToken, id_produto){
                        var foto_produto : String? = null
                        if(it != null && it.foto_produto.toString() != "null"){
                            carrinho.add(Pedido_Join(null, null, LocalDateTime.now(), "Ativo", id_produto, id_ginasio, nome, tipo_produto, preco, descricao, estado_produto, foto_produto, quantidade_produto, quantidadeComprada))
                            carrinho_adapter.notifyDataSetChanged()

                            //TODO: total preco
                            var total_price = 0.0
                            for (produto in carrinho) {
                                total_price += (produto.preco?.times(produto.quantidade_pedido!!)!!)
                            }

                            findViewById<TextView>(R.id.textViewTotalPrice).text = String.format("%.2f", total_price) + " €"

                            if (carrinho.count() > 0)
                            {
                                findViewById<Button>(R.id.buttonBuyCart).visibility = View.VISIBLE
                                findViewById<TextView>(R.id.textViewTotalPrice).visibility = View.VISIBLE
                                findViewById<TextView>(R.id.buttonCancelCart).visibility = View.VISIBLE
                            }
                        }
                    }
                }
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

        val adapter = MyAdapter(this@Activity_Cliente_Loja_Produtos, options)

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
                                    this@Activity_Cliente_Loja_Produtos,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Loja_Produtos,
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
                                this@Activity_Cliente_Loja_Produtos,
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
        imageView.setOnClickListener {
            spinner.performClick()
        }

        val list_view_produtos = findViewById<ListView>(R.id.listview_produtos)
        list_view_produtos.adapter = produtos_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterLoja : BaseAdapter(){
        override fun getCount(): Int {
            return produtos_list.size
        }

        override fun getItem(position: Int): Any {
            return produtos_list[position]
        }

        override fun getItemId(position: Int): Long {
            return produtos_list[position].id_produto!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_produto,parent,false)

            val nomeProdutoView = root_view.findViewById<TextView>(R.id.text_view_nome)
            nomeProdutoView.text = produtos_list[position].nome

            val precoProdutoView = root_view.findViewById<TextView>(R.id.text_view_preco)
            precoProdutoView.text = String.format("%.2f", produtos_list[position].preco) + " €"

            if (produtos_list[position].foto_produto != null && produtos_list[position].foto_produto.toString() != "null")
            {
                val pictureByteArray = Base64.decode(produtos_list[position].foto_produto, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                root_view.findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }

            //Clicar num rootView abre os detalhes do produto
            root_view.setOnClickListener {
                if(produtos_list[position].quantidade_produto != 0){
                    val intent = Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Loja_Produto_Details::class.java)

                    intent.putExtra("id_produto", produtos_list[position].id_produto)
                    intent.putExtra("id_ginasio", produtos_list[position].id_ginasio)
                    intent.putExtra("nome", produtos_list[position].nome)
                    intent.putExtra("tipo_produto", produtos_list[position].tipo_produto)
                    intent.putExtra("preco", produtos_list[position].preco)
                    intent.putExtra("descricao", produtos_list[position].descricao)
                    intent.putExtra("estado_produto", produtos_list[position].estado_produto)
                    intent.putExtra("quantidade_produto", produtos_list[position].quantidade_produto)

                    receiverNewData?.launch(intent)
                }
                else
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Product Unavailable", Toast.LENGTH_LONG).show()
            }

            return root_view
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
            val root_view = layoutInflater.inflate(R.layout.row_carrinho,parent,false)

            val nomeProdutoView = root_view.findViewById<TextView>(R.id.text_view_nome)
            nomeProdutoView.text = carrinho[position].nome

            val precoProdutoView = root_view.findViewById<TextView>(R.id.text_view_preco)
            precoProdutoView.text = String.format("%.2f", carrinho[position].preco)

            val quantityProdutoView = root_view.findViewById<TextView>(R.id.textViewQt)
            quantityProdutoView.text = carrinho[position].quantidade_pedido.toString()

            root_view.findViewById<ImageButton>(R.id.imageButtonRemove).setOnClickListener{
                carrinho.remove(carrinho[position])
                carrinho_adapter.notifyDataSetChanged()
                var total_price = 0.0
                for (produto in carrinho) {
                    total_price += (produto.preco?.times(produto.quantidade_pedido!!)!!)
                }

                findViewById<TextView>(R.id.textViewTotalPrice).text = String.format("%.2f", total_price) + " €"
            }
            return root_view
        }

    }
}