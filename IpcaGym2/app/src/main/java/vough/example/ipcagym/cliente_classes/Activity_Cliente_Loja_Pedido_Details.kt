package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Pedido_Join
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PedidoLojaRequests
import vough.example.ipcagym.requests.PedidoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Loja_Pedido_Details : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var list_produtos_pedido = arrayListOf<Pedido_Join>()
    var produto_pedido_adapter = AdapterProdutoPedido()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_loja_pedido_details)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_pedido = intent.getIntExtra("id_pedido", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_pedido = intent.getStringExtra("data_pedido")
        var estado_pedido = intent.getStringExtra("estado_pedido")

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByToken(lifecycleScope,sessionToken){ resultCliente ->
            if (resultCliente != null)
            {
                if (resultCliente.foto_perfil != null && resultCliente.foto_perfil.toString() != "null")
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                PedidoRequests.GetAllConnectionClient(lifecycleScope,sessionToken,id_cliente){ resultJoin ->
                    if(resultJoin.isNotEmpty()){
                        val list_produtos_pedido_result = arrayListOf<Pedido_Join>()

                        for (i in resultJoin){
                            if(i.id_pedido == id_pedido){
                                list_produtos_pedido_result.add(i)
                            }
                        }

                        list_produtos_pedido = list_produtos_pedido_result
                        produto_pedido_adapter.notifyDataSetChanged()

                        var total_price = 0.0
                        for (produto in list_produtos_pedido) {
                            total_price += (produto.preco?.times(produto.quantidade_pedido!!)!!)
                        }
                        findViewById<TextView>(R.id.textViewTotalPreco).text = String.format("%.2f", total_price) + " €"
                    }
                    else{
                        Toast.makeText(this@Activity_Cliente_Loja_Pedido_Details, "Error: PedidoLoja", Toast.LENGTH_LONG).show()
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

        val adapter = MyAdapter(this@Activity_Cliente_Loja_Pedido_Details, options)

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
                                    this@Activity_Cliente_Loja_Pedido_Details,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Loja_Pedido_Details,
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
                                this@Activity_Cliente_Loja_Pedido_Details,
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

        findViewById<TextView>(R.id.textViewIDPedido).text = id_pedido.toString()
        var string : String = "( " + estado_pedido + " )"
        findViewById<TextView>(R.id.textViewEstadoPedido).text = string
        findViewById<TextView>(R.id.textViewDataPedido).text = data_pedido?.format(date_time_formatter).toString()

        val list_view_produtos_pedido = findViewById<ListView>(R.id.listviewProdutosPedido)
        list_view_produtos_pedido.adapter = produto_pedido_adapter

        findViewById<ListView>(R.id.listviewProdutosPedido)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }

        val botao_cancelar = findViewById<Button>(R.id.buttonCancelar)
        if (estado_pedido == "Ativo") botao_cancelar.visibility = View.VISIBLE
        botao_cancelar.setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Loja_Pedido_Details, Activity_Cliente_Loja_Pedidos::class.java)
            // remove os pedidos loja e inativa pedido
            PedidoLojaRequests.DeletePedidoLoja(lifecycleScope,sessionToken,id_pedido){ resultCancelarPedido ->
                if (resultCancelarPedido == "Error: Delete PedidoLoja fails")
                {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedido_Details, "Error: Delete PedidoLoja fails", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
        }
    }

    inner class AdapterProdutoPedido : BaseAdapter(){
        override fun getCount(): Int {
            return list_produtos_pedido.size
        }

        override fun getItem(position: Int): Any {
            return list_produtos_pedido[position]
        }

        override fun getItemId(position: Int): Long {
            return list_produtos_pedido[position].id_produto!!.toLong()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_produto_pedido,parent,false)

            val pedido_nome_view = root_view.findViewById<TextView>(R.id.NomeProdutoPedido)
            pedido_nome_view.text = list_produtos_pedido[position].nome

            val pedido_tipo_view = root_view.findViewById<TextView>(R.id.TipoProdutoPedido)
            pedido_tipo_view.text = list_produtos_pedido[position].tipo_produto

            val pedido_preco_view = root_view.findViewById<TextView>(R.id.PrecoPedido)
            pedido_preco_view.text = list_produtos_pedido[position].preco.toString()

            val pedido_descricao_view = root_view.findViewById<TextView>(R.id.DescricaoPedido)
            pedido_descricao_view.text = list_produtos_pedido[position].descricao

            val pedido_quantidade_view = root_view.findViewById<TextView>(R.id.QuantidadePedido)
            pedido_quantidade_view.text = list_produtos_pedido[position].quantidade_pedido.toString()

            return root_view
        }

    }
}