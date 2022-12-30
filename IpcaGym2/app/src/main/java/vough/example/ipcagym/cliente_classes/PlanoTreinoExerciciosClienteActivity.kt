package vough.example.ipcagym.cliente_classes

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
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Plano_Treino
import java.time.LocalTime

class PlanoTreinoExerciciosClienteActivity : AppCompatActivity() {

    var exercicios_plano_list = arrayListOf<Exercicio>()
    var exercicio_adapter = AdapterExercicios()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_treino_plano_exercicios)

        exercicios_plano_list.add(Exercicio(1,1,"Flexoes","90 graus de flexao","Braços",1,null,10,null))

        val id_plano_treino = intent.getIntExtra("id_plano_treino", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val tipo = intent.getStringExtra("tipo")
        val foto_plano_treino = intent.getStringExtra("foto_plano_treino")

        findViewById<TextView>(R.id.textViewType).text = tipo

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_exercicios = findViewById<ListView>(R.id.listview_exercicios)
        list_view_exercicios.adapter = exercicio_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@PlanoTreinoExerciciosClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterExercicios : BaseAdapter() {
        override fun getCount(): Int {
            return exercicios_plano_list.size
        }

        override fun getItem(position: Int): Any {
            return exercicios_plano_list[position]
        }

        override fun getItemId(position: Int): Long {
            return exercicios_plano_list[position].id_exercicio!!.toLong()
        }

        //api para usar o Duration.ZERO
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_exercicio,parent,false)

            val exercicio_nome_view = rootView.findViewById<TextView>(R.id.textViewNomeExercicios)
            exercicio_nome_view.text = exercicios_plano_list[position].nome

            val exercicio_quantity_view = rootView.findViewById<TextView>(R.id.textViewSetsExercicio)
            if (exercicios_plano_list[position].tempo == null)
            {
                var seriesRepeticoes = exercicios_plano_list[position].series.toString() + " Series / " + exercicios_plano_list[position].repeticoes.toString() + " Reps"
                exercicio_quantity_view.text = seriesRepeticoes
            }
            else exercicio_quantity_view.text = exercicios_plano_list[position].tempo.toString()

            if (exercicios_plano_list[position].foto_exercicio != null)
            {
                val exercicio_image_view = rootView.findViewById<ImageView>(R.id.imageViewPlanoTreino)
                val imageUri: Uri = Uri.parse(exercicios_plano_list[position].foto_exercicio)
                exercicio_image_view.setImageURI(imageUri)
            }

            return rootView
        }
    }
}