package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente

class Activity_Funcionario_Clientes_List : AppCompatActivity() {

    var list_clientes = arrayListOf<Cliente>()
    var clientes_adapter = AdapterCliente()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_clientes_list)

        //list_clientes.add(Cliente(1,1,1, LocalDateTime.of(2023,1,20,16,30,0),"Consulta Aberta","Ativo"))
        //list_clientes.add(Cliente(2,1,1, LocalDateTime.of(2023,2,4,12,0,0),"Psicologia","Ativo"))
        //list_clientes.add(Cliente(3,1,1, LocalDateTime.of(2023,10,10,18,10,0),"Nutricional","Ativo"))

        val image_view = findViewById<ImageView>(R.id.profile_pic_funcionario_list_clientes)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Clientes_List,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        image_view.setOnClickListener {
            spinner.performClick()
        }

        //TODO: Enviar lista?
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