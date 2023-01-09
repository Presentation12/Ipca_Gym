package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests
import vough.example.ipcagym.views.VerticalBarCapacityView

class Activity_Funcionario_Capacity : AppCompatActivity() {
    val listAux = arrayListOf<Plano_Treino>()
    val adapterAux = CapacityManagementApapter()
    var funcionarioRefresh : Funcionario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_capacity)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) funcionarioRefresh = result
        }

        val listViewAux = findViewById<ListView>(R.id.auxListView)
        listViewAux.adapter = adapterAux

        listAux.add(Plano_Treino(1,1,"aux","aux"))

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Capacity, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Login::class.java))
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_capacity);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Capacity, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    inner class CapacityManagementApapter : BaseAdapter(){
        override fun getCount(): Int {
            return listAux.size
        }

        override fun getItem(position: Int): Any {
            return listAux[position]
        }

        override fun getItemId(position: Int): Long {
            return listAux[position].id_plano_treino?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_funcionario_capacity_aux,parent,false)

            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            //Esconder percentagens debaixo das barras
            rootView.findViewById<TextView>(R.id.testMonday).isVisible = false
            rootView.findViewById<TextView>(R.id.testMonday2).isVisible = false
            rootView.findViewById<TextView>(R.id.testMonday3).isVisible = false
            rootView.findViewById<TextView>(R.id.testMonday4).isVisible = false
            rootView.findViewById<TextView>(R.id.testMonday5).isVisible = false
            rootView.findViewById<TextView>(R.id.testMonday6).isVisible = false

            FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){  result ->
                if(result != null)
                    GinasioRequests.GetByID(lifecycleScope, sessionToken, result?.id_ginasio!!){ resultGym ->
                        if(resultGym != null)
                            AtividadeRequests.getGymStats(lifecycleScope, sessionToken, result?.id_ginasio!!){ result2 ->
                                if(result2 != null){
                                    rootView.findViewById<TextView>(R.id.capacityCurrentValue).text = result2.current.toString()
                                    rootView.findViewById<TextView>(R.id.capacityExitsValue).text = result2.exits.toString()
                                    rootView.findViewById<TextView>(R.id.capacityTodayValue).text = result2.today.toString()
                                    rootView.findViewById<TextView>(R.id.capacityDailyThisOne).text = String.format("%.2f", result2.dailyAverage)
                                    rootView.findViewById<TextView>(R.id.capacityMonthlyValue).text = String.format("%.2f", result2.monthlyAverage)
                                    rootView.findViewById<TextView>(R.id.capacityYearValue).text = result2.yearTotal.toString()
                                    rootView.findViewById<TextView>(R.id.capacityMaxDayValue).text = result2.maxDay.toString()
                                    rootView.findViewById<TextView>(R.id.capacityMaxMonthCurrentValue).text = result2.maxMonth.toString()

                                    //Preencher as barras
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityMonday).setPercentagem(1 - (result2.averageMonday!!.toFloat() / resultGym.lotacaoMax!!))
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityTuesday).setPercentagem(1 - (result2.averageTuesday!!.toFloat() / resultGym.lotacaoMax!!))
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityWednesday).setPercentagem(1 - (result2.averageWednesday!!.toFloat() / resultGym.lotacaoMax!!))
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityThrusday).setPercentagem(1 - (result2.averageThursday!!.toFloat() / resultGym.lotacaoMax!!))
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityFriday).setPercentagem(1 - (result2.averageFriday!!.toFloat() / resultGym.lotacaoMax!!))
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacitySaturday).setPercentagem(1 - (result2.averageSaturday!!.toFloat() / resultGym.lotacaoMax!!))

                                    //Percentagens de ocupação
                                    /*rootView.findViewById<TextView>(R.id.testMonday).text = String.format("%.2f", (result2.averageMonday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"
                                    rootView.findViewById<TextView>(R.id.testMonday2).text = String.format("%.2f", (result2.averageTuesday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"
                                    rootView.findViewById<TextView>(R.id.testMonday3).text = String.format("%.2f", (result2.averageWednesday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"
                                    rootView.findViewById<TextView>(R.id.testMonday4).text = String.format("%.2f", (result2.averageThursday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"
                                    rootView.findViewById<TextView>(R.id.testMonday5).text = String.format("%.2f", (result2.averageFriday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"
                                    rootView.findViewById<TextView>(R.id.testMonday6).text = String.format("%.2f", (result2.averageSaturday!!.toFloat() / resultGym.lotacaoMax!!) * 100) + " %"*/
                                }
                            }
                    }
            }

            return rootView
        }
    }
}
