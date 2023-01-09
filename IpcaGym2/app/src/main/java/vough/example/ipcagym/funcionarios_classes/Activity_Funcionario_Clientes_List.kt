package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

            //TODO: foto gerente
            /*
            if (resultFuncionario?.foto_perfil != null)
            {
                val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                imageView.setImageURI(imageUri)
            }
            */
            ClienteRequests.GetAllByGymID(lifecycleScope, sessionToken, resultFuncionario?.id_ginasio) {resultClientes ->
                list_clientes = resultClientes
                clientes_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Funcionario_Clientes_List, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Login::class.java))
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

        findViewById<Button>(R.id.buttonAddCliente).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Clientes_List, Activity_Funcionario_Cliente_Add::class.java)
            startActivity(intent)
        }

        val list_view_clientes = findViewById<ListView>(R.id.listviewClientes)
        list_view_clientes.adapter = clientes_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Clientes_List,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Clientes_List,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Clientes_List,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Clientes_List,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Clientes_List,"Atividades", Toast.LENGTH_LONG).show()
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

            val nome_view = root_view.findViewById<TextView>(R.id.text_view_nome)
            nome_view.text = list_clientes[position].nome
            val mail_view = root_view.findViewById<TextView>(R.id.text_view_mail)
            mail_view.text = list_clientes[position].mail

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
                intent.putExtra("foto_perfil", list_clientes[position].foto_perfil)
                intent.putExtra("estado", list_clientes[position].estado)

                startActivity(intent)
            }

            return root_view
        }

    }

}