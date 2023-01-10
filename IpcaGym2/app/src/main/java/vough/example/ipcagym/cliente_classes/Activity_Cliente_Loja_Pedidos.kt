package vough.example.ipcagym.cliente_classes

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
import vough.example.ipcagym.data_classes.Pedido
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PedidoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Loja_Pedidos : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var list_pedidos = arrayListOf<Pedido>()
    var pedidos_adapter = AdapterPedido()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_loja_pedidos)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByToken(lifecycleScope,sessionToken){ resultCliente ->
            if (resultCliente != null)
            {
                if (resultCliente?.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    imageView.setImageURI(imageUri)
                }

                PedidoRequests.GetAllByClienteID(lifecycleScope, sessionToken, resultCliente.id_cliente){ resultPedidosCliente ->
                    if (resultPedidosCliente.count() > 0)
                    {
                        list_pedidos = resultPedidosCliente
                        pedidos_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Cliente_Loja_Pedidos, options)

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
                                    this@Activity_Cliente_Loja_Pedidos,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Loja_Pedidos,
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
                                this@Activity_Cliente_Loja_Pedidos,
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

        val list_view_pedidos = findViewById<ListView>(R.id.listviewPedidos)
        list_view_pedidos.adapter = pedidos_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedidos,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedidos,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedidos,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedidos,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Loja_Pedidos,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterPedido : BaseAdapter(){
        override fun getCount(): Int {
            return list_pedidos.size
        }

        override fun getItem(position: Int): Any {
            return list_pedidos[position]
        }

        override fun getItemId(position: Int): Long {
            return list_pedidos[position].id_pedido!!.toLong()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_cliente_pedido,parent,false)

            val pedido_data_view = root_view.findViewById<TextView>(R.id.DataPedido)
            pedido_data_view.text = list_pedidos[position].data_pedido?.format(date_time_formatter).toString()

            val pedido_estado_view = root_view.findViewById<TextView>(R.id.EstadoPedido)
            pedido_estado_view.text = list_pedidos[position].estado_pedido

            //Clicar num rootView abre os detalhes do pedido
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Loja_Pedidos, Activity_Cliente_Loja_Pedido_Details::class.java)

                intent.putExtra("id_pedido", list_pedidos[position].id_pedido)
                intent.putExtra("id_cliente", list_pedidos[position].id_cliente)
                intent.putExtra("data_pedido", list_pedidos[position].data_pedido?.format(date_time_formatter))
                intent.putExtra("estado_pedido", list_pedidos[position].estado_pedido)

                startActivity(intent)
            }

            return root_view
        }

    }
}