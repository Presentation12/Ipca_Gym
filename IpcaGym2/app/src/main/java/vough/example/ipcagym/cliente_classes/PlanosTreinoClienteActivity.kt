package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Treino
import java.time.format.DateTimeFormatter

class PlanosTreinoClienteActivity : AppCompatActivity() {

    var planos_treino_list = arrayListOf<Plano_Treino>()
    var plano_adapter = AdapterPlanosTreino()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_treino_planos)

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_planos_treino = findViewById<ListView>(R.id.listviewPlanosTreino)
        list_view_planos_treino.adapter = plano_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@PlanosTreinoClienteActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@PlanosTreinoClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@PlanosTreinoClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@PlanosTreinoClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@PlanosTreinoClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@PlanosTreinoClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterPlanosTreino : BaseAdapter(){
        override fun getCount(): Int {
            return planos_treino_list.size
        }

        override fun getItem(position: Int): Any {
            return planos_treino_list[position]
        }

        override fun getItemId(position: Int): Long {
            return planos_treino_list[position].id_plano_treino!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_plano_treino,parent,false)

            //Guardar elementos em variaveis
            val plano_treino_view = root_view.findViewById<TextView>(R.id.textViewPlanoTreino)

            //Adicionar os textos
            plano_treino_view.text = planos_treino_list[position].tipo

            //Clicar num rootView abre o plano de treino
            root_view.setOnClickListener {
                val intent = Intent(this@PlanosTreinoClienteActivity, PlanoTreinoExerciciosClienteActivity::class.java)

                intent.putExtra("id_plano_treino", planos_treino_list[position].id_plano_treino)
                intent.putExtra("id_ginasio", planos_treino_list[position].id_ginasio)
                intent.putExtra("tipo", planos_treino_list[position].tipo)
                intent.putExtra("foto_plano_treino", planos_treino_list[position].foto_plano_treino)

                startActivity(intent)
            }

            return root_view
        }

    }
}