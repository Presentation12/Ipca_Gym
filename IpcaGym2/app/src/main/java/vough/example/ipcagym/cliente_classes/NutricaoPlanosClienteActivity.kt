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
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.data_classes.Refeicao
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NutricaoPlanosClienteActivity : AppCompatActivity() {
    var list_planos_nutricionais = arrayListOf<Plano_Nutricional>()
    var planos_adapter = AdapterPlanoNutricao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_planos)

        // Hardcode
        list_planos_nutricionais.add(Plano_Nutricional(1,1,"Emagrecer",2500 ,null))
        list_planos_nutricionais.add(Plano_Nutricional(2,1,"Engordar",3000, null))
        list_planos_nutricionais.add(Plano_Nutricional(3,1,"Definir",2000, null))

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic_cliente_planos_nutricionais_page)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_planos_nutricionais = findViewById<ListView>(R.id.listviewPlanosNutricionais)
        list_view_planos_nutricionais.adapter = planos_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@NutricaoPlanosClienteActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@NutricaoPlanosClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@NutricaoPlanosClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@NutricaoPlanosClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@NutricaoPlanosClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@NutricaoPlanosClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
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
                val intent = Intent(this@NutricaoPlanosClienteActivity, NutricaoPlanoDetalhesClienteActivity::class.java)

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