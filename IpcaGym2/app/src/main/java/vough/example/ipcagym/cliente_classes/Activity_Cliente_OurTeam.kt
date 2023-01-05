package vough.example.ipcagym.cliente_classes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests

class Activity_Cliente_OurTeam : AppCompatActivity() {
    var ourTeamList = arrayListOf<Funcionario>()
    var adapterTeam = OurTeamAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_ourteam)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val listViewFuncionarios= findViewById<ListView>(R.id.listview_funcionarios)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        ourTeamList.add(Funcionario(1,1,"boas",true,1,"bpas","dasd","Ativo"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        listViewFuncionarios.adapter = adapterTeam

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){
            FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, it?.id_ginasio){
                if(!it.isEmpty()) ourTeamList = it
                adapterTeam.notifyDataSetChanged()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_OurTeam,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_OurTeam,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_OurTeam,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_OurTeam,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_OurTeam,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class OurTeamAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return ourTeamList.size
        }

        override fun getItem(position: Int): Any {
            return ourTeamList[position]
        }

        override fun getItemId(position: Int): Long {
            return ourTeamList[position].id_funcionario?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_funcionario,parent,false)

            val nome = rootView.findViewById<TextView>(R.id.funcionarioName)
            val code = rootView.findViewById<TextView>(R.id.idFuncionario)

            nome.text = ourTeamList[position].nome
            code.text = ourTeamList[position].codigo.toString()

            return rootView
        }

    }
}