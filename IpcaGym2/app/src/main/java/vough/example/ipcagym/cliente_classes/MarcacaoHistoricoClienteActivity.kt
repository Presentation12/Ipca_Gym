package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.data_classes.Plano_Treino
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MarcacaoHistoricoClienteActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var list_marcacoes = arrayListOf<Marcacao>()
    var marcacoes_adapter = AdapterMarcacao()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacoes_list)

        list_marcacoes.add(Marcacao(1,1,1, LocalDateTime.of(2023,1,20,16,30,0),"Consulta Aberta","Ativo"))
        list_marcacoes.add(Marcacao(2,1,1, LocalDateTime.of(2023,2,4,12,0,0),"Psicologia","Ativo"))
        list_marcacoes.add(Marcacao(3,1,1, LocalDateTime.of(2023,10,10,18,10,0),"Nutricional","Ativo"))

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic_cliente_marcacoes)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_planos_treino = findViewById<ListView>(R.id.listviewMarcacoes)
        list_view_planos_treino.adapter = marcacoes_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MarcacaoHistoricoClienteActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@MarcacaoHistoricoClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
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
                val intent = Intent(this@MarcacaoHistoricoClienteActivity, MarcacaoHistoricoClienteDetailsActivity::class.java)

                intent.putExtra("id_marcacao", list_marcacoes[position].id_marcacao)
                intent.putExtra("id_funcionario", list_marcacoes[position].id_funcionario)
                intent.putExtra("id_cliente", list_marcacoes[position].id_cliente)
                intent.putExtra("data_marcacao", list_marcacoes[position].data_marcacao?.format(date_time_formatter))
                intent.putExtra("descricao", list_marcacoes[position].descricao)
                intent.putExtra("estado", list_marcacoes[position].estado)

                startActivity(intent)
            }

            return root_view
        }

    }
}