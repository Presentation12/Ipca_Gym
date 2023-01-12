package vough.example.ipcagym.cliente_classes

import android.annotation.SuppressLint
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
        val listViewFuncionarios= findViewById<ListView>(R.id.listview_funcionarios)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Appointments", "Product Requests", "Rate", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 6
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_OurTeam, options)

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
                            spinner.setSelection(6)
                        } else {
                            startActivity(
                                Intent(
                                    this@Activity_Cliente_OurTeam,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(6)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_OurTeam,
                                Activity_Cliente_Definitions::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    2 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_OurTeam,
                                Activity_Cliente_Marcacoes::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    3 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_OurTeam,
                                Activity_Cliente_Loja_Pedidos::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    4 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_OurTeam,
                                Activity_Cliente_Avaliar::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    5 -> {
                        val preferences =
                            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(
                            Intent(
                                this@Activity_Cliente_OurTeam,
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

        listViewFuncionarios.adapter = adapterTeam

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){
            FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, it?.id_ginasio){
                if(!it.isEmpty())
                {
                    for(func in it)
                    {
                        if(func.estado == "Ativo")
                        {
                            ourTeamList.add(func)
                        }
                    }
                }
                adapterTeam.notifyDataSetChanged()
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_OurTeam, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_OurTeam, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_OurTeam, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_OurTeam, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_OurTeam, Activity_Cliente_Activities::class.java))
                    finish()

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