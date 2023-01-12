package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Funcionario_Clientes_List : AppCompatActivity() {

    var list_clientes = arrayListOf<Cliente>()
    var clientes_adapter = AdapterCliente()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_clientes_list)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_funcionario_list_clientes)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultFuncionario ->

            if (resultFuncionario?.foto_funcionario  != null && resultFuncionario.foto_funcionario != "null")
            {
                val pictureByteArray = Base64.decode(resultFuncionario.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageView.setImageBitmap(bitmap)
            }

            ClienteRequests.GetAllByGymID(lifecycleScope, sessionToken, resultFuncionario?.id_ginasio) {resultClientes ->
                list_clientes = resultClientes
                clientes_adapter.notifyDataSetChanged()
            }
        }

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val sessionRole = preferences.getString("session_role", null)
        var options: List<String>

        if(sessionRole == "Funcionario"){
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Logout", "")
        }
        else{
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Employees" , "Gym Edit", "Logout", "")
        }

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return if(sessionRole == "Funcionario") {
                    8
                }else{
                    10
                }
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Clientes_List, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(sessionRole == "Funcionario"){
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(8)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
                else{
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(10)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                if(sessionRole == "Funcionario")
                    spinner.setSelection(8)
                else
                    spinner.setSelection(10)
            }
        }
        imageView.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.buttonAddCliente).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Cliente_Add::class.java)
            startActivity(intent)
        }

        val list_view_clientes = findViewById<ListView>(R.id.listviewClientes)
        list_view_clientes.adapter = clientes_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_clients);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterCliente : BaseAdapter(){
        override fun getCount(): Int {
            return list_clientes.size
        }

        override fun getItem(position: Int): Any {
            return list_clientes[position]
        }

        override fun getItemId(position: Int): Long {
            return list_clientes[position].id_cliente!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_cliente,parent,false)

            var imageClienteView = findViewById<ImageView>(R.id.profile_pic_cliente)
            if (list_clientes[position].foto_perfil  != null && list_clientes[position].foto_perfil != "null")
            {
                val pictureByteArray = Base64.decode(list_clientes[position].foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageClienteView.setImageBitmap(bitmap)
            }

            val nome_view = root_view.findViewById<TextView>(R.id.text_view_nome)
            nome_view.text = list_clientes[position].nome
            val mail_view = root_view.findViewById<TextView>(R.id.text_view_mail)
            mail_view.text = list_clientes[position].mail
            val estado_view = root_view.findViewById<TextView>(R.id.text_view_estado)
            estado_view.text = list_clientes[position].estado

            //Clicar num rootView abre os detalhes do cliente
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Cliente_Details::class.java)

                intent.putExtra("id_cliente", list_clientes[position].id_cliente)
                intent.putExtra("id_ginasio", list_clientes[position].id_ginasio)
                intent.putExtra("id_plano_nutricional", list_clientes[position].id_plano_nutricional)
                intent.putExtra("nome", list_clientes[position].nome)
                intent.putExtra("mail", list_clientes[position].mail)
                intent.putExtra("telemovel", list_clientes[position].telemovel)
                intent.putExtra("pass_salt", list_clientes[position].pass_salt)
                intent.putExtra("pass_hash", list_clientes[position].pass_hash)
                intent.putExtra("peso", list_clientes[position].peso)
                intent.putExtra("altura", list_clientes[position].altura)
                intent.putExtra("gordura", list_clientes[position].gordura)
                intent.putExtra("estado", list_clientes[position].estado)

                startActivity(intent)
            }

            return root_view
        }

    }

}