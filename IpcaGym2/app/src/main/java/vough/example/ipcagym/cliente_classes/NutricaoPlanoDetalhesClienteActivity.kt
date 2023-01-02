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

class NutricaoPlanoDetalhesClienteActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    val time_formatter = DateTimeFormatter.ofPattern("HH:mm")
    var refeicoes_adapter = AdapterRefeicao()
    var list_refeicoes = arrayListOf<Refeicao>()
    // Hardcode

    //api para tempo
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_plano_details)

        val id_plano_nutricional = intent.getIntExtra("id_plano_nutricional", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val tipo = intent.getStringExtra("tipo")
        val calorias = intent.getIntExtra("calorias",-1)
        val foto_plano_nutricional = intent.getStringExtra("foto_plano_nutricional")

        // Hardcode
        list_refeicoes.add(Refeicao(1,1,"Banana", LocalTime.of(10,30,0),null))
        list_refeicoes.add(Refeicao(2,1,"Rojão", LocalTime.of(12,30,0),null))
        list_refeicoes.add(Refeicao(3,1,"Bife", LocalTime.of(16,30,0),null))

        findViewById<TextView>(R.id.textViewCalorias).text = calorias.toString()
        findViewById<TextView>(R.id.textViewTipoNutricao).text = tipo

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val image_view = findViewById<ImageView>(R.id.profile_pic_cliente_nutricao)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //TODO: fazer troca de plano do cliente neste butao
        findViewById<Button>(R.id.buttonSubmeterNovoPlanoCliente).setOnClickListener {
            // tem de se passar o cliente atual ou aceder a ele para leterar o id do plano nutricional
        }

        val list_view_refeicoes = findViewById<ListView>(R.id.listviewRefeicoes)
        list_view_refeicoes.adapter = refeicoes_adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,options[position], Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@NutricaoPlanoDetalhesClienteActivity,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterRefeicao : BaseAdapter(){
        override fun getCount(): Int {
            return list_refeicoes.size
        }

        override fun getItem(position: Int): Any {
            return list_refeicoes[position]
        }

        override fun getItemId(position: Int): Long {
            return list_refeicoes[position].id_refeicao!!.toLong()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_refeicao,parent,false)

            val refeicao_text_view = root_view.findViewById<TextView>(R.id.textViewHoraRefeicao)
            refeicao_text_view.text = list_refeicoes[position].hora.toString()

            if (list_refeicoes[position].foto_refeicao != null)
            {
                val refeicao_image_view = root_view.findViewById<ImageView>(R.id.imageViewRefeicao)
                val imageUri: Uri = Uri.parse(list_refeicoes[position].foto_refeicao)
                refeicao_image_view.setImageURI(imageUri)
            }

            //Clicar num rootView abre o plano de treino
            root_view.setOnClickListener {
                val intent = Intent(this@NutricaoPlanoDetalhesClienteActivity, NutricaoRefeicaoClienteActivity::class.java)

                intent.putExtra("id_refeicao", list_refeicoes[position].id_refeicao)
                intent.putExtra("id_plano_nutricional", list_refeicoes[position].id_plano_nutricional)
                intent.putExtra("descricao", list_refeicoes[position].descricao)
                intent.putExtra("hora", list_refeicoes[position].hora?.format(time_formatter))
                intent.putExtra("foto_refeicao", list_refeicoes[position].foto_refeicao)

                startActivity(intent)
            }

            return root_view
        }

    }
}