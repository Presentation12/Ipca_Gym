package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Refeicao
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Nutricional_Refeicoes : AppCompatActivity() {
    val listRefeicoes = arrayListOf<Refeicao>()
    val refeicaoAdapter = RefeicoesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicoes)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        listRefeicoes.add(Refeicao(1,1, "Refeicao Yoigfhaloigheouibgoipuerahbgiurewaghiouaehrwighapeoriuhgfoipaerhigrehioghaerohgfoipurehgoierhioupgh erou opig opifdopih gfopaerh gopuhera9po upoug heouipr hopre hopuahre o paoreg r eerçk dhgi eriou ghoeur h9opiearyh gpoiuerho ugheraopiug heoripua ghpoiuaer hoipugerh ipuge rpiou hpohgare uoherpog hogre hgoerih wr", LocalTime.now(), "photo"))
        listRefeicoes.add(Refeicao(2,1, "Refeicao X", LocalTime.of(14,30), "photo2"))
        listRefeicoes.add(Refeicao(3,1, "Refeicao W", LocalTime.of(20,0), "happymeal.png"))
        listRefeicoes.add(Refeicao(4,1, "Refeicao >", LocalTime.of(9,45), null))

        findViewById<TextView>(R.id.planoNutriTipoTitle).text = intent.getStringExtra("tipo")
        //TODO: PERDE VALORES DA LISTA AO FAZER DETAILS, OU EDIT
        findViewById<TextView>(R.id.planoNutriCaloriasTitle).text = intent.getIntExtra("calorias", -1).toString() + " KCal"

        val listView = findViewById<ListView>(R.id.listViewRefeicoes)
        listView.adapter = refeicaoAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        findViewById<Button>(R.id.removePlanButton).setOnClickListener {
            val deleteIntent = Intent()

            deleteIntent.putExtra("id_remove", intent.getIntExtra("id_plano_nutricional", -1))
            deleteIntent.putExtra("tipo_remove", intent.getStringExtra("tipo"))

            setResult(RESULT_OK, deleteIntent)
            finish()
        }


        image_view.setOnClickListener {
            spinner.performClick()
        }

    }

    inner class RefeicoesAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return listRefeicoes.size
        }

        override fun getItem(position: Int): Any {
            return listRefeicoes[position]
        }

        override fun getItemId(position: Int): Long {
            return listRefeicoes[position].id_plano_nutricional?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_refeicao, parent, false)

            rootView.findViewById<TextView>(R.id.textViewHoraRefeicao).text = listRefeicoes[position].hora?.format(
                DateTimeFormatter.ofPattern("HH:mm"))

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Details::class.java)

                intent.putExtra("id_plano_nutricional", listRefeicoes[position].id_plano_nutricional)
                intent.putExtra("id_refeicao", listRefeicoes[position].id_refeicao)
                intent.putExtra("descricao", listRefeicoes[position].descricao)
                intent.putExtra("hora", listRefeicoes[position].hora?.format(DateTimeFormatter.ofPattern("HH:mm")))
                intent.putExtra("foto_refeicao", listRefeicoes[position].foto_refeicao)

                startActivity(intent)
            }

            rootView.findViewById<Button>(R.id.buttonDeleteRefeicao).setOnClickListener{
                listRefeicoes.remove(listRefeicoes[position])
                refeicaoAdapter.notifyDataSetChanged()
            }

            return rootView
        }

    }
}