package vough.example.ipcagym.cliente_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
                if (resultCliente.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    imageView.setImageURI(imageUri)
                }

                LojaRequests.GetAllByGinasioID(lifecycleScope, sessionToken,resultCliente.id_ginasio){ resultProdutosGinasio ->
                    if(resultProdutosGinasio.isNotEmpty()){
                        produtos_list = resultProdutosGinasio
                        produtos_adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val carrinho_view = findViewById<ImageView>(R.id.imageViewCarrinho)
        val spinner_carrinho = findViewById<Spinner>(R.id.spinnerCarrinho)
        spinner_carrinho.adapter = carrinho_adapter
        spinner_carrinho.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //Toast.makeText(this@Activity_Cliente_Loja, carrinho[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        carrinho_view.setOnClickListener {
            spinner_carrinho.performClick()
            if (carrinho.count() > 0)
            {
                findViewById<Button>(R.id.buttonBuyCart).visibility = View.VISIBLE
                findViewById<TextView>(R.id.textViewTotalPrice).visibility = View.VISIBLE
                findViewById<TextView>(R.id.buttonCancelCart).visibility = View.VISIBLE
            }
        }

        //TODO: Verify cancelar carrinho que remove tudo da lista
        findViewById<TextView>(R.id.buttonCancelCart).setOnClickListener {
            carrinho.clear()
            carrinho_adapter.notifyDataSetChanged()
        }

        //TODO: Verify buy cart que da post de um peiddo novo e de todos os pedidos de produto
        findViewById<Button>(R.id.buttonBuyCart).setOnClickListener {
            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

                // post de pedido
                var newPedido = Pedido(null,resultCliente?.id_cliente, LocalDateTime.now(),"Ativo")
                PedidoRequests.Post(lifecycleScope,sessionToken,newPedido){ resultAddPedido ->
                    if (resultAddPedido == "User not found")
                    {
                        //erro
                    }
                    else
                    {
                        // buscar id do pedido criado
                        PedidoRequests.GetAll(lifecycleScope,sessionToken){ resultPedidos ->

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
                                    if (resultAddPedido == "User not found")
                                    {
                                        //erro
                                    }
                                    else
                                    {
                                        // correu bem
                                    }
                                }
                            }

                        }
                    }
                }

                // post pedido loja
            }
        }

        //TODO: total preco
        var total_price = 0.0
        for (produto in carrinho) {
              total_price += produto.preco!!
        }
        findViewById<TextView>(R.id.textViewTotalPrice).text = total_price.toString()

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
                    var foto_produto = it.data?.getStringExtra("foto_produto")
                    var quantidade_produto = it.data?.getIntExtra("quantidade_produto", -1)

                    carrinho.add(Pedido_Join(null, null, LocalDateTime.now(), "Ativo", id_produto, id_ginasio, nome, tipo_produto, preco, descricao, estado_produto, foto_produto, quantidade_produto, quantidadeComprada))
                    carrinho_adapter.notifyDataSetChanged()
                }
            }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener {
            spinner.performClick()
        }

        val list_view_produtos = findViewById<ListView>(R.id.listview_produtos)
        list_view_produtos.adapter = produtos_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Main Menu", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Treino", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Refeicoes", Toast.LENGTH_LONG)
                        .show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Produtos, "Atividades", Toast.LENGTH_LONG)
                        .show()
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
            precoProdutoView.text = produtos_list[position].preco.toString()

            if (produtos_list[position].foto_produto != null)
            {
                val produto_image_view = root_view.findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(produtos_list[position].foto_produto)
                produto_image_view.setImageURI(imageUri)
            }

            //Clicar num rootView abre os detalhes do produto
            root_view.setOnClickListener {

                val intent = Intent(this@Activity_Cliente_Loja_Produtos, Activity_Cliente_Loja_Produto_Details::class.java)

                intent.putExtra("id_produto", produtos_list[position].id_produto)
                intent.putExtra("id_ginasio", produtos_list[position].id_ginasio)
                intent.putExtra("nome", produtos_list[position].nome)
                intent.putExtra("tipo_produto", produtos_list[position].tipo_produto)
                intent.putExtra("preco", produtos_list[position].preco)
                intent.putExtra("descricao", produtos_list[position].descricao)
                intent.putExtra("estado_produto", produtos_list[position].estado_produto)
                intent.putExtra("foto_produto", produtos_list[position].foto_produto)
                intent.putExtra("quantidade_produto", produtos_list[position].quantidade_produto)

                receiverNewData?.launch(intent)
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
            precoProdutoView.text = carrinho[position].preco.toString()

            val quantityProdutoView = root_view.findViewById<TextView>(R.id.textViewQt)
            quantityProdutoView.text = carrinho[position].quantidade_pedido.toString()

            //TODO: remover artigos carrinho
            findViewById<ImageButton>(R.id.imageButtonRemove).setOnClickListener{
                carrinho.remove(carrinho[position])
                carrinho_adapter.notifyDataSetChanged()
            }
            return root_view
        }

    }
}