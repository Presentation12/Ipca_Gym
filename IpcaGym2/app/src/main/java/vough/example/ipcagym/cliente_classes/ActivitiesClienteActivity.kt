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
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import java.time.format.DateTimeFormatter

class ActivitiesClienteActivity : AppCompatActivity(){

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

        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listViewClients = findViewById<ListView>(R.id.listview_funcionarios)
        listViewClients.adapter = client_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@ActivitiesClienteActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@ActivitiesClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
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
            date.text = activityList[position].data_saida?.format(date_formatter)

            //Clicar num rootView abre detalhes
            rootView.setOnClickListener {
                val intent = Intent(this@ActivitiesClienteActivity, ActivityDetailClienteActivity::class.java)

                intent.putExtra("id_atividade", activityList[position].id_atividade)
                intent.putExtra("data", activityList[position].data_saida?.format(date_formatter))
                intent.putExtra("hora_entrada", activityList[position].data_entrada?.format(time_formatter))
                intent.putExtra("hora_saida", activityList[position].data_saida?.format(time_formatter))

                startActivity(intent)
            }

            return rootView
        }
    }
}