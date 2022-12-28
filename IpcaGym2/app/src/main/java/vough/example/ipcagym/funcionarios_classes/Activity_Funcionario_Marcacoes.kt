package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Marcacao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Marcacoes : AppCompatActivity() {
    val marcacoesList = arrayListOf<Marcacao>()
    var marcacao_adapter = MarcacaoFuncionarioAdapter()
    val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val date_formatter_compact = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val time_formatter = DateTimeFormatter.ofPattern("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_marcacoes)

        marcacoesList.add(Marcacao(marcacoesList.size+1,1,1,LocalDateTime.now(),"Consulta","Ativo"))
        marcacoesList.add(Marcacao(marcacoesList.size+1,2,3,
            LocalDateTime.of(2022,2,24,10,15,12),"Preparação Física","Ativo"))
        marcacoesList.add(Marcacao(marcacoesList.size+1,3,4,
            LocalDateTime.of(2021,3,24,11,13,15),"Consulta","Inativa"))
        marcacoesList.add(Marcacao(marcacoesList.size+1,7,3,
            LocalDateTime.of(2022,2,11,16,43,23),"Consulta","Ativo"))


        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val listViewMarcacoes = findViewById<ListView>(R.id.listViewMarcacoes)

        listViewMarcacoes.adapter = marcacao_adapter

        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Marcacoes,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
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