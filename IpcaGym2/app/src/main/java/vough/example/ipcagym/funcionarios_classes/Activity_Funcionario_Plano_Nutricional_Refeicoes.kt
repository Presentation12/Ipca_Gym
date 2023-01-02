package vough.example.ipcagym.funcionarios_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional

class Activity_Funcionario_Plano_Nutricional_Refeicoes : AppCompatActivity() {
    val listPlanosNutricionais = arrayListOf<Plano_Nutricional>()
    val adapter_nutri = PlanNutriAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_nutricionais)

        listPlanosNutricionais.add(Plano_Nutricional(1,1, "Emagrecer", 2500, "photo"))
        listPlanosNutricionais.add(Plano_Nutricional(2,1, "Engordar", 5500, "photo2"))
        listPlanosNutricionais.add(Plano_Nutricional(3,1, "Definir", 3500, null))
        
        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listPlanosNutricionaisView = findViewById<ListView>(R.id.listViewPlanosNutricionais)
        listPlanosNutricionaisView.adapter = adapter_nutri

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }

    inner class PlanNutriAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return listPlanosNutricionais.size
        }

        override fun getItem(position: Int): Any {
            return listPlanosNutricionais[position]
        }

        override fun getItemId(position: Int): Long {
            return listPlanosNutricionais[position].id_plano_nutricional?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_plano_nutricional, parent, false)

            rootView.findViewById<TextView>(R.id.textViewPlanoNutricional).text = listPlanosNutricionais[position].tipo

            return rootView
        }

    }
}