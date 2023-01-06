package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FluxControlFuncionarioActivity : AppCompatActivity() {

    var activityList = arrayListOf<Atividade>()
    var funcionarioRefresh : Funcionario? = null
    var client_adapter = FuncionarioActivityAdapter()
    val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val date_formatter_compact = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var receiverNewActivity : ActivityResultLauncher<Intent>? = null
    val time_formatter = DateTimeFormatter.ofPattern("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_flux_control)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) funcionarioRefresh = result
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val addButton = findViewById<Button>(R.id.buttonAddNewActivity)

        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listViewActivities = findViewById<ListView>(R.id.listByDate)
        listViewActivities.adapter = client_adapter

        AtividadeRequests.GetAll(lifecycleScope, sessionToken){
            activityList = it

            client_adapter.notifyDataSetChanged()
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

        receiverNewActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val id_ginasio = funcionarioRefresh?.id_ginasio
                val id_cliente = it.data?.getIntExtra("id_cliente", -1)

                val state = it.data?.getBooleanExtra("state", true)
                var biggestDate = LocalDateTime.MIN
                var isNewClient = true

                if(state == true){
                    for(activity in activityList){
                        if(id_ginasio == activity.id_ginasio && id_cliente == activity.id_cliente
                            && (biggestDate < activity.data_entrada)){
                            biggestDate = activity.data_entrada
                            isNewClient = false
                        }
                    }

                    for(activity in activityList){
                        if((id_ginasio == activity.id_ginasio && id_cliente == activity.id_cliente &&
                            (biggestDate == activity.data_entrada && !isNewClient)) || isNewClient){

                            if(activity.data_saida == null){
                                Toast.makeText(this@FluxControlFuncionarioActivity,"This client needs to exit first!", Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                AtividadeRequests.Post(lifecycleScope, sessionToken, Atividade(
                                    null,
                                    id_ginasio,
                                    id_cliente,
                                    LocalDateTime.now(),
                                    null
                                )){ response ->
                                    if(response == "User not found") Toast.makeText(this@FluxControlFuncionarioActivity, "ERROR", Toast.LENGTH_SHORT).show()
                                    else{
                                        Toast.makeText(this@FluxControlFuncionarioActivity,"Activity added successfully", Toast.LENGTH_SHORT).show()
                                        AtividadeRequests.GetAll(lifecycleScope, sessionToken){
                                            activityList = it

                                            client_adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    for(activity in activityList){
                        if(id_ginasio == activity.id_ginasio && id_cliente == activity.id_cliente
                            && biggestDate < activity.data_entrada){
                                biggestDate = activity.data_entrada
                        }
                    }

                    for(activity in activityList){
                        if(id_ginasio == activity.id_ginasio && id_cliente == activity.id_cliente
                            && biggestDate == activity.data_entrada){
                            if(activity.data_saida == null){
                                AtividadeRequests.Patch(lifecycleScope, sessionToken, activity.id_atividade!!, Atividade(
                                    null,
                                    activity.id_ginasio,
                                    activity.id_cliente,
                                    activity.data_entrada,
                                    LocalDateTime.now())){ result ->
                                    if(result != "User not found") {
                                        Toast.makeText(this@FluxControlFuncionarioActivity,"Activity added successfully", Toast.LENGTH_LONG).show()
                                        AtividadeRequests.GetAll(lifecycleScope, sessionToken){
                                            activityList = it

                                            client_adapter.notifyDataSetChanged()
                                        }
                                    }
                                    else Toast.makeText(this@FluxControlFuncionarioActivity,"ERROR", Toast.LENGTH_LONG).show()
                                }
                            }
                            else
                            {
                                Toast.makeText(this@FluxControlFuncionarioActivity,"This client needs to enter first!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(this@FluxControlFuncionarioActivity,"Error on adding a new activity", Toast.LENGTH_LONG).show()
            }
        }

        addButton.setOnClickListener {
            receiverNewActivity?.launch(Intent(this@FluxControlFuncionarioActivity, FluxControlFuncionarioAddActivity::class.java))
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
            val rootView = layoutInflater.inflate(R.layout.row_activities_by_date,parent,false)
            val rootView2 = layoutInflater.inflate(R.layout.row_activities_on_date,parent,false)

            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            //TODO: Dependendo de ser entrada ou saida, escolher o icone respetivo e organizar variaveis para o layout

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


            ClienteRequests.GetByID(lifecycleScope, sessionToken, activityList[position].id_cliente){
                id_ginasio.text = it?.nome
                id_cliente.text = activityList[position].id_cliente?.toString()
            }

            rootView.setOnClickListener {
                val intent = Intent(this@FluxControlFuncionarioActivity, FluxControlFuncionarioDetailsActivity::class.java)

                intent.putExtra("id_atividade", activityList[position].id_atividade)
                intent.putExtra("id_ginasio", activityList[position].id_ginasio)
                intent.putExtra("id_cliente", activityList[position].id_cliente)
                intent.putExtra("data", activityList[position].data_entrada?.format(date_formatter))
                intent.putExtra("data_E", activityList[position].data_saida?.format(date_formatter))
                intent.putExtra("hora_entrada", activityList[position].data_entrada?.format(time_formatter))
                intent.putExtra("hora_saida", activityList[position].data_saida?.format(time_formatter))

                if(hour.currentTextColor.toString() == (-65536).toString()){
                    intent.putExtra("state", false)
                }
                else{
                    intent.putExtra("state", true)
                }

                startActivity(intent)
            }



            return rootView
        }

    }
}