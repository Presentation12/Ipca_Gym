package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.*
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Ginasio
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Flux_Control : AppCompatActivity() {

    var activityList = arrayListOf<Atividade>()
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
        var counter = 0

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null && result.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
            }
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val addButton = findViewById<Button>(R.id.buttonAddNewActivity)

        val sessionRole = preferences.getString("session_role", null)
        var options: List<String>

        if(sessionRole == "Funcionario"){
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Logout", "")
        }
        else{
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Employees" , "Gym Edit", "Logout", "")
        }

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return if(sessionRole == "Funcionario") {
                    8
                }else{
                    10
                }
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Flux_Control, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(sessionRole == "Funcionario"){
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(8)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
                else{
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(10)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                if(sessionRole == "Funcionario")
                    spinner.setSelection(8)
                else
                    spinner.setSelection(10)
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        val listViewActivities = findViewById<ListView>(R.id.listByDate)
        listViewActivities.adapter = client_adapter

        AtividadeRequests.GetAll(lifecycleScope, sessionToken){
            activityList = it

            client_adapter.notifyDataSetChanged()
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    true
                }
                else -> false
            }
        }

        receiverNewActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val id_cliente = it.data?.getIntExtra("id_cliente", -1)
                val state = it.data?.getBooleanExtra("state", true)

                ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
                    if(it != null){
                        val id_ginasio = it.id_ginasio

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
                                        Toast.makeText(this@Activity_Funcionario_Flux_Control,"This client needs to exit first!", Toast.LENGTH_LONG).show()
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
                                            if(response == "Error: Post Activity fails") Toast.makeText(this@Activity_Funcionario_Flux_Control, "ERROR", Toast.LENGTH_SHORT).show()
                                            else{
                                                Toast.makeText(this@Activity_Funcionario_Flux_Control,"Activity added successfully", Toast.LENGTH_SHORT).show()

                                                GinasioRequests.GetByID(lifecycleScope, sessionToken, id_ginasio){ resultGymInfo ->
                                                    if(resultGymInfo != null){
                                                        val newLotacao = resultGymInfo.lotacao?.plus(1)

                                                        GinasioRequests.Patch(lifecycleScope, sessionToken, 1, Ginasio(
                                                            resultGymInfo.id_ginasio,
                                                            resultGymInfo.instituicao,
                                                            resultGymInfo.estado,
                                                            resultGymInfo.foto_ginasio,
                                                            resultGymInfo.contacto,
                                                            newLotacao,
                                                            resultGymInfo.lotacaoMax
                                                        )){ patchResult ->
                                                                Toast.makeText(this@Activity_Funcionario_Flux_Control,patchResult, Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }


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
                                            if(result != "Error: Patch Activity fails") {
                                                Toast.makeText(this@Activity_Funcionario_Flux_Control,"Activity added successfully", Toast.LENGTH_LONG).show()

                                                GinasioRequests.GetByID(lifecycleScope, sessionToken, id_ginasio){ resultGymInfo ->
                                                    if(resultGymInfo != null){
                                                        val newLotacao = resultGymInfo.lotacao?.plus(-1)

                                                        GinasioRequests.Patch(lifecycleScope, sessionToken, 1, Ginasio(
                                                            resultGymInfo.id_ginasio,
                                                            resultGymInfo.instituicao,
                                                            resultGymInfo.estado,
                                                            resultGymInfo.foto_ginasio,
                                                            resultGymInfo.contacto,
                                                            newLotacao,
                                                            resultGymInfo.lotacaoMax
                                                        )){ patchResult ->
                                                            Toast.makeText(this@Activity_Funcionario_Flux_Control,patchResult, Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }

                                                AtividadeRequests.GetAll(lifecycleScope, sessionToken){
                                                    activityList = it

                                                    client_adapter.notifyDataSetChanged()
                                                }
                                            }
                                            else Toast.makeText(this@Activity_Funcionario_Flux_Control,"ERROR", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(this@Activity_Funcionario_Flux_Control,"This client needs to enter first!", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }
                    }
                }

        }

        addButton.setOnClickListener {
            receiverNewActivity?.launch(Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Flux_Control_Add::class.java))
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
            //val rootView2 = layoutInflater.inflate(R.layout.row_activities_on_date,parent,false)

            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            //TODO: Dependendo de ser entrada ou saida, escolher o icone respetivo e organizar variaveis para o layout

            val date = rootView.findViewById<TextView>(R.id.activityDate)
            //val listbydate = rootView.findViewById<ListView>(R.id.listByDate)

            //Aqui é nome (tratar para o caso dos nomes serem muito grandes)
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
                if(it != null){
                    id_ginasio.text = it.nome
                    id_cliente.text = activityList[position].id_cliente?.toString()

                    val pictureByteArray = Base64.decode(it.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    rootView.findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
                }
            }

            rootView.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Flux_Control, Activity_Funcionario_Flux_Control_Details::class.java)

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