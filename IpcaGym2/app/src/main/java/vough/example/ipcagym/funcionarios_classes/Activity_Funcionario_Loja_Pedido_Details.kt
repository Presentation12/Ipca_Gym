package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Pedido
import vough.example.ipcagym.data_classes.Pedido_Join
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PedidoLojaRequests
import vough.example.ipcagym.requests.PedidoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Loja_Pedido_Details : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var list_produtos_pedido = arrayListOf<Pedido_Join>()
    var produto_pedido_adapter = AdapterProdutoPedido()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_pedido_details)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_pedido = intent.getIntExtra("id_pedido", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_pedido = intent.getStringExtra("data_pedido")
        var estado_pedido = intent.getStringExtra("estado_pedido")

        var data_pedido_formatado = LocalDateTime.parse(data_pedido, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultFuncionario ->
            if (resultFuncionario != null)
            {
                if (resultFuncionario.foto_funcionario != null)
                {
                    val imageUri: Uri = Uri.parse(resultFuncionario.foto_funcionario)
                    imageView.setImageURI(imageUri)
                }

                ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){ resultCliente ->

                    var imageViewCliente = findViewById<ImageView>(R.id.profile_pic_user)
                    if (resultCliente?.foto_perfil != null)
                    {
                        val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                        imageViewCliente.setImageURI(imageUri)
                    }
                    findViewById<TextView>(R.id.textViewNomeUser).text =  resultCliente?.nome
                }

                PedidoRequests.GetAllConnectionClient(lifecycleScope,sessionToken,id_cliente){ resultJoinsCliente ->
                    if (resultJoinsCliente != null)
                    {
                        for (pedidoLoja in resultJoinsCliente)
                        {
                            if (pedidoLoja.id_pedido == id_pedido)
                            {
                                list_produtos_pedido.add(pedidoLoja)
                            }
                        }
                        produto_pedido_adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Loja_Pedido_Details, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Login::class.java))
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
        var string : String = "(" + estado_pedido + ")"
        findViewById<TextView>(R.id.textViewEstadoPedido).text = string
        findViewById<TextView>(R.id.textViewDataPedido).text =  data_pedido?.format(date_time_formatter).toString()
        findViewById<TextView>(R.id.textViewIDCliente).text =  id_cliente.toString()

        val list_view_produtos_pedido = findViewById<ListView>(R.id.listviewProdutos)
        list_view_produtos_pedido.adapter = produto_pedido_adapter

        var total_price = 0.0
        for (produto in list_produtos_pedido) {
            total_price += produto.preco!!
        }
        findViewById<TextView>(R.id.textViewTotalPreco).text = total_price.toString()

        var botao_entregar = findViewById<Button>(R.id.buttonEntregar)
        if(estado_pedido == "Ativo") botao_entregar.visibility = View.VISIBLE
        botao_entregar.setOnClickListener {
            estado_pedido = "Inativo" //entregue
            var editPedido = Pedido(id_pedido,id_cliente,data_pedido_formatado,estado_pedido)
            PedidoRequests.Patch(lifecycleScope, sessionToken, id_pedido, editPedido){ resultEditPedido ->
                if (resultEditPedido == "User not found")
                {
                    Toast.makeText(this@Activity_Funcionario_Loja_Pedido_Details, "Error: Edit fail", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedido_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
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