package vough.example.ipcagym.funcionarios_classes

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FuncionarioManagementActivity : AppCompatActivity() {

    var activityList = arrayListOf<Atividade>()
    var client_adapter = FuncionarioActivityAdapter()
    val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val date_formatter_compact = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val time_formatter = DateTimeFormatter.ofPattern("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_flux_control)

        activityList.add(Atividade(1,1,1,
            LocalDateTime.now(),null))
        activityList.add(Atividade(2,2,3,
            LocalDateTime.now(),
            LocalDateTime.now()))
        activityList.add(Atividade(3,2,4,
            LocalDateTime.of(2022,10,10,10,10,10),
            null))
        activityList.add(Atividade(4,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))
        activityList.add(Atividade(5,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))
        activityList.add(Atividade(6,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            null))
        activityList.add(Atividade(7,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))
        activityList.add(Atividade(8,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))
        activityList.add(Atividade(9,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))
        activityList.add(Atividade(0,2,4,
            LocalDateTime.of(2022,12,13,10,10,10),
            LocalDateTime.of(2022,11,16,14,12,50)))

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)

        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listViewClients = findViewById<ListView>(R.id.listByDate)
        listViewClients.adapter = client_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@FuncionarioManagementActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }
    }

    inner class FuncionarioActivityAdapter : BaseAdapter(){
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
            val rootView = layoutInflater.inflate(R.layout.activities_by_date_row,parent,false)
            val rootView2 = layoutInflater.inflate(R.layout.activities_on_date_row,parent,false)

            //TODO: Dependendo de ser entrada ou saida, escolher o icone respetivo
            //TODO: Associar perfil do cliente pelo id_Cliente

            //Guardar elementos em variaveis
            val date = rootView.findViewById<TextView>(R.id.activityDate)
            //val listbydate = rootView.findViewById<ListView>(R.id.listByDate)

            //Aqui é nome (tratar para o caso dos nomes serem muito grandes) e foto
            val id_ginasio = rootView.findViewById<TextView>(R.id.activityClienteName)
            val id_cliente = rootView.findViewById<TextView>(R.id.activityClienteID)
            val hour = rootView.findViewById<TextView>(R.id.horaMovimento)

            if(activityList[position].data_saida == null){
                date.text = activityList[position].data_entrada?.format(date_formatter_compact)
                hour.text = activityList[position].data_entrada?.format(time_formatter)
                hour.setTextColor(Color.GREEN)
                date.setTextColor(Color.GREEN)
            }
            else{
                date.text = activityList[position].data_saida?.format(date_formatter_compact)
                hour.text = activityList[position].data_saida?.format(time_formatter)
                hour.setTextColor(Color.RED)
                date.setTextColor(Color.RED)
            }

            //Adicionar os textos
            id_ginasio.text = activityList[position].id_ginasio?.toString()
            id_cliente.text = activityList[position].id_cliente?.toString()

            return rootView
        }

    }
}