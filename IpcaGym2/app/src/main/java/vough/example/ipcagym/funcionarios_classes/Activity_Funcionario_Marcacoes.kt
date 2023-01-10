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
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Marcacoes : AppCompatActivity() {
    var marcacoesList = arrayListOf<Marcacao>()
    var marcacao_adapter = MarcacaoFuncionarioAdapter()
    val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val date_formatter_compact = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val time_formatter = DateTimeFormatter.ofPattern("hh:mm")
    var funcionarioRefresh : Funcionario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_marcacoes)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val listViewMarcacoes = findViewById<ListView>(R.id.listViewMarcacoes)

        listViewMarcacoes.adapter = marcacao_adapter

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) funcionarioRefresh = result

            MarcacaoRequests.GetAllByFuncionarioID(lifecycleScope, sessionToken, funcionarioRefresh?.id_funcionario!!){
                if(!it.isEmpty()) {
                    marcacoesList = it
                    marcacao_adapter.notifyDataSetChanged()
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Flux_Control::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Marcacoes, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Login::class.java))
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
    }

    inner class MarcacaoFuncionarioAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return marcacoesList.size
        }

        override fun getItem(position: Int): Any {
            return marcacoesList[position]
        }

        override fun getItemId(position: Int): Long {
            return marcacoesList[position].id_marcacao!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_funcionario_marcacao,parent,false)

            val data = rootView.findViewById<TextView>(R.id.marcacaodescricaovalue)

            data.text = marcacoesList[position].data_marcacao?.format(date_formatter) + "  " + marcacoesList[position].data_marcacao?.format(time_formatter)

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Marcacoes, Activity_Funcionario_Marcacoes_Details::class.java)

                intent.putExtra("id_marcacao", marcacoesList[position].id_marcacao)
                intent.putExtra("id_funcionario", marcacoesList[position].id_funcionario)
                intent.putExtra("id_cliente", marcacoesList[position].id_cliente)
                intent.putExtra("data_marcacao", marcacoesList[position].data_marcacao?.format(date_formatter_compact) + " " + marcacoesList[position].data_marcacao?.format(time_formatter))
                intent.putExtra("estado", marcacoesList[position].estado)
                intent.putExtra("descricao", marcacoesList[position].descricao)

                startActivity(intent)
            }

            return rootView
        }

    }
}