package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests

class Activity_Cliente_Nutricao_Planos : AppCompatActivity() {
    var list_planos_nutricionais = arrayListOf<Plano_Nutricional>()
    var planos_adapter = AdapterPlanoNutricao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_planos)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_planos_nutricionais_page)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

            if (resultCliente?.foto_perfil != null)
            {
                val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                imageView.setImageURI(imageUri)
            }
            PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope,sessionToken,resultCliente?.id_ginasio){resultPlanosNutricionais ->
                list_planos_nutricionais = resultPlanosNutricionais
                planos_adapter.notifyDataSetChanged()
            }
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Cliente_Nutricao_Planos,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        imageView.setOnClickListener {
            spinner.performClick()
        }

        val list_view_planos_nutricionais = findViewById<ListView>(R.id.listviewPlanosNutricionais)
        list_view_planos_nutricionais.adapter = planos_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Planos,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Planos,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Planos,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Planos,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Nutricao_Planos,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterPlanoNutricao : BaseAdapter(){
        override fun getCount(): Int {
            return list_planos_nutricionais.size
        }

        override fun getItem(position: Int): Any {
            return list_planos_nutricionais[position]
        }

        override fun getItemId(position: Int): Long {
            return list_planos_nutricionais[position].id_plano_nutricional!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_plano_nutricional,parent,false)

            val tipo_plano_nutricional_text_view = root_view.findViewById<TextView>(R.id.textViewPlanoNutricional)
            tipo_plano_nutricional_text_view.text = list_planos_nutricionais[position].tipo

            if (list_planos_nutricionais[position].foto_plano_nutricional != null)
            {
                val plano_nutricional_image_view = root_view.findViewById<ImageView>(R.id.imageViewPlanoNutricional)
                val imageUri: Uri = Uri.parse(list_planos_nutricionais[position].foto_plano_nutricional)
                plano_nutricional_image_view.setImageURI(imageUri)
            }

            //Clicar num rootView abre os detalhes do plano nutricional
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Nutricao_Plano_Details::class.java)

                intent.putExtra("id_plano_nutricional", list_planos_nutricionais[position].id_plano_nutricional)
                intent.putExtra("id_ginasio", list_planos_nutricionais[position].id_ginasio)
                intent.putExtra("tipo", list_planos_nutricionais[position].tipo)
                intent.putExtra("calorias", list_planos_nutricionais[position].calorias)
                intent.putExtra("foto_plano_nutricional", list_planos_nutricionais[position].foto_plano_nutricional)

                startActivity(intent)
            }

            return root_view
        }

    }
}