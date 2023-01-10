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
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Marcacoes : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var clienteRefresh : Cliente? = null
    var list_marcacoes = arrayListOf<Marcacao>()
    var marcacoes_adapter = AdapterMarcacao()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacoes)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_marcacoes)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null) clienteRefresh = resultCliente

            MarcacaoRequests.GetAllByClienteID(lifecycleScope, sessionToken, resultCliente?.id_cliente) { resultMarcacoes ->
                clienteRefresh = resultCliente
                if (clienteRefresh?.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(clienteRefresh?.foto_perfil)
                    imageView.setImageURI(imageUri)
                }

                list_marcacoes = resultMarcacoes
                marcacoes_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Cliente_Marcacoes, options)

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
                                    this@Activity_Cliente_Marcacoes,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Marcacoes,
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
                                this@Activity_Cliente_Marcacoes,
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

        val list_view_planos_treino = findViewById<ListView>(R.id.listviewMarcacoes)
        list_view_planos_treino.adapter = marcacoes_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Marcacoes,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Marcacoes,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Marcacoes,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Marcacoes,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Marcacoes,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterMarcacao : BaseAdapter(){
        override fun getCount(): Int {
            return list_marcacoes.size
        }

        override fun getItem(position: Int): Any {
            return list_marcacoes[position]
        }

        override fun getItemId(position: Int): Long {
            return list_marcacoes[position].id_marcacao!!.toLong()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_cliente_marcacao,parent,false)

            val marcacao_date_view = root_view.findViewById<TextView>(R.id.activityDate)
            marcacao_date_view.text = list_marcacoes[position].data_marcacao?.format(date_time_formatter).toString()

            //Clicar num rootView abre os detalhes da marcação
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Marcacoes, Activity_Cliente_Marcacao_Details::class.java)

                intent.putExtra("id_marcacao", list_marcacoes[position].id_marcacao)
                intent.putExtra("id_funcionario", list_marcacoes[position].id_funcionario)
                intent.putExtra("id_cliente", list_marcacoes[position].id_cliente)
                intent.putExtra("data_marcacao", list_marcacoes[position].data_marcacao?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                intent.putExtra("descricao", list_marcacoes[position].descricao)
                intent.putExtra("estado", list_marcacoes[position].estado)

                startActivity(intent)
            }

            return root_view
        }

    }
}