package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Plano_Nutricional_Refeicao_Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicao_details)

        findViewById<TextView>(R.id.horaValueRefeicao).text = intent.getStringExtra("hora")
        findViewById<TextView>(R.id.descricaoValueRefeicao).text = intent.getStringExtra("descricao")

        //TODO: METER FOTO REFEICAO NOS DETALHES
        //findViewById<TextView>(R.id.imageRefeicaoValue).text = intent.getStringExtra("foto_refeicao")

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        findViewById<Button>(R.id.buttonReturnDetailsMeal).setOnClickListener{
            val auxIntent = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Details, Activity_Funcionario_Plano_Nutricional_Refeicoes::class.java)

            auxIntent.putExtra("calorias", intent.getIntExtra("calorias", -1))
            auxIntent.putExtra("id_plano_nutricional", intent.getIntExtra("id_plano_nutricional", -1))
            auxIntent.putExtra("tipo", intent.getStringExtra("tipo"))

            finish()
            startActivity(auxIntent)
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }
}