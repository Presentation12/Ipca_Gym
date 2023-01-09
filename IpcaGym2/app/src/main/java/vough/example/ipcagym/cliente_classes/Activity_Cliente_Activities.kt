package vough.example.ipcagym.cliente_classes

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
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Login
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Perfil_Edit
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Planos_Nutricionais
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Settings
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import java.time.format.DateTimeFormatter

class Activity_Cliente_Activities : AppCompatActivity(){

    var activityList = arrayListOf<Atividade>()
    var client_adapter = AdapterAtividade()
    val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val time_formatter = DateTimeFormatter.ofPattern("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_activities)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){
            if(it != null)
                AtividadeRequests.GetAllByClienteID(lifecycleScope, sessionToken, it?.id_cliente!!){ response ->
                    if(response.isNotEmpty()){
                        activityList = response
                        client_adapter.notifyDataSetChanged()
                    }
                }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Activities, options)

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
                            startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Account::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Definitions::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Login::class.java))
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


        val listViewClients = findViewById<ListView>(R.id.listview_funcionarios)
        listViewClients.adapter = client_adapter

        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    val selectedItemId = bottomNavigationView.selectedItemId
                    if (selectedItemId != R.id.nav_history){
                        startActivity(Intent(this@Activity_Cliente_Activities, Activity_Cliente_Activities::class.java))
                        finish()
                    }

                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterAtividade: BaseAdapter(){
        override fun getCount(): Int {
            return activityList.size
        }

        override fun getItem(position: Int): Any {
            return activityList[position]
        }

        override fun getItemId(position: Int): Long {
            return activityList[position].id_atividade!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_activity,parent,false)

            //Guardar elementos em variaveis
            val date = rootView.findViewById<TextView>(R.id.activityTextView)

            //Adicionar os textos
            date.text = activityList[position].data_entrada?.format(date_formatter)

            //Clicar num rootView abre detalhes
            rootView.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Activities, Activity_Cliente_Activity_Details::class.java)

                intent.putExtra("id_atividade", activityList[position].id_atividade)
                intent.putExtra("data", activityList[position].data_entrada?.format(date_formatter))
                intent.putExtra("hora_entrada", activityList[position].data_entrada?.format(time_formatter))
                intent.putExtra("hora_saida", activityList[position].data_saida?.format(time_formatter))

                startActivity(intent)
            }

            return rootView
        }
    }
}