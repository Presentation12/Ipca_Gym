package vough.example.ipcagym.funcionarios_classes

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import vough.example.ipcagym.R
import java.time.Duration

class Activity_Funcionario_Plano_Treino_Exercicio_Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_detail)

        val nome = intent.getStringExtra("nome")
        val descricao = intent.getStringExtra("descricao")
        val tipo = intent.getStringExtra("tipo")
        val series = intent.getStringExtra("series")

        findViewById<TextView>(R.id.exercicioNome).text = nome
        findViewById<TextView>(R.id.descricaoExercicioValue).text = descricao
        findViewById<TextView>(R.id.tipoExercicioValue).text = tipo
        findViewById<TextView>(R.id.seriesExercicioValue).text = series

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Details,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }
}