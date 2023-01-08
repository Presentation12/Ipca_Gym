package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomappbar.BottomAppBar
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Ginasio
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests
import vough.example.ipcagym.views.VerticalBarCapacityView
import java.util.zip.Inflater

class CapacityManagementActivity : AppCompatActivity() {
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
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
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

            FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){  result ->
                if(result != null)
                    GinasioRequests.GetByID(lifecycleScope, sessionToken, result?.id_ginasio!!){ resultGym ->
                        if(resultGym != null)
                            AtividadeRequests.getGymStats(lifecycleScope, sessionToken, result?.id_ginasio!!){ result2 ->
                                if(result2 != null)
                                    rootView.findViewById<TextView>(R.id.capacityCurrentValue).text = result2.current.toString()
                                    rootView.findViewById<TextView>(R.id.capacityExitsValue).text = result2.exits.toString()
                                    rootView.findViewById<TextView>(R.id.capacityTodayValue).text = result2.today.toString()
                                    rootView.findViewById<TextView>(R.id.capacityDailyThisOne).text = String.format("%.2f", result2.dailyAverage)
                                    rootView.findViewById<TextView>(R.id.capacityMonthlyValue).text = String.format("%.2f", result2.monthlyAverage)
                                    rootView.findViewById<TextView>(R.id.capacityYearValue).text = result2.yearTotal.toString()
                                    rootView.findViewById<TextView>(R.id.capacityMaxDayValue).text = result2.maxDay.toString()
                                    rootView.findViewById<TextView>(R.id.capacityMaxMonthCurrentValue).text = result2.maxMonth.toString()

                                    //Preencher as barras
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityMonday).setPercentagem(result2.averageMonday!!.toFloat() / resultGym.lotacaoMax!!)
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityTuesday).setPercentagem(result2.averageTuesday!!.toFloat() / resultGym.lotacaoMax!!)
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityWednesday).setPercentagem(result2.averageWednesday!!.toFloat() / resultGym.lotacaoMax!!)
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityThrusday).setPercentagem(result2.averageThursday!!.toFloat() / resultGym.lotacaoMax!!)
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacityFriday).setPercentagem(result2.averageFriday!!.toFloat() / resultGym.lotacaoMax!!)
                                    rootView.findViewById<VerticalBarCapacityView>(R.id.capacitySaturday).setPercentagem(result2.averageSaturday!!.toFloat() / resultGym.lotacaoMax!!)
                            }
                    }
            }

            return rootView
        }

    }
}
