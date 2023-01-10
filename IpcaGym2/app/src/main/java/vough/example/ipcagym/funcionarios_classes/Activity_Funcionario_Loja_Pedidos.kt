package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Pedido
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PedidoRequests
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Loja_Pedidos : AppCompatActivity() {

    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var list_pedidos = arrayListOf<Pedido>()
    var pedidos_adapter = AdapterPedido()

    // TODO verificar
    var sessionToken : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_pedidos_list)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultFuncionario ->
            if(resultFuncionario != null)
            {
                if (resultFuncionario.foto_funcionario != null)
                {
                    val imageUri: Uri = Uri.parse(resultFuncionario.foto_funcionario)
                    imageView.setImageURI(imageUri)
                }

                PedidoRequests.GetAllByGinasioID(lifecycleScope, sessionToken, resultFuncionario.id_ginasio){ resultPedidos ->
                    if (resultPedidos.count() > 0)
                    {
                        list_pedidos = resultPedidos
                        pedidos_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Funcionario_Loja_Pedidos, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        val list_view_pedidos = findViewById<ListView>(R.id.listviewPedidos)
        list_view_pedidos.adapter = pedidos_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Flux_Control::class.java))
                    finish()
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

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_pedido,parent,false)

            val image_cliente_view = root_view.findViewById<ImageView>(R.id.profile_pic_activity)
            ClienteRequests.GetByID(lifecycleScope, sessionToken, list_pedidos[position].id_cliente){ resultCliente ->
                if (resultCliente != null && resultCliente.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    image_cliente_view.setImageURI(imageUri)
                }
            }

            val data_pedido_view = root_view.findViewById<TextView>(R.id.dataPedido)
            data_pedido_view.text = list_pedidos[position].data_pedido?.format(date_time_formatter).toString() //TODO: substituir pelo nome quando link
            val estado_pedido_view = root_view.findViewById<TextView>(R.id.estadoPedido)
            estado_pedido_view.text = list_pedidos[position].estado_pedido

            //Clicar num rootView abre os detalhes do pedido
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Loja_Pedidos, Activity_Funcionario_Loja_Pedido_Details::class.java)

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