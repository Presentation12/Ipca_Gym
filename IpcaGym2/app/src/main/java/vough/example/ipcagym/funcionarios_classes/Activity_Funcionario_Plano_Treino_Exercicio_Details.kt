package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ExercicioRequests

class Activity_Funcionario_Plano_Treino_Exercicio_Details : AppCompatActivity() {
    var receiverEditData : ActivityResultLauncher<Intent>? = null
    var exercicio_adapter = ExercicioAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_detail)

        val nome = intent.getStringExtra("nome")
        val descricao = intent.getStringExtra("descricao")
        val tipo = intent.getStringExtra("tipo")
        val series = intent.getStringExtra("series")
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)


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

        receiverEditData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){

                ExercicioRequests.GetByID(lifecycleScope, sessionToken, it?.data?.getIntExtra("id_exericio", -1)){ result ->
                    if(result != null){
                         findViewById<TextView>(R.id.exercicioNome).text = result.nome.toString()
                         findViewById<TextView>(R.id.descricaoExercicioValue).text =  result.descricao.toString()
                         findViewById<TextView>(R.id.tipoExercicioValue).text =  result.tipo.toString()
                         findViewById<TextView>(R.id.seriesExercicioValue).text =  result.tempo.toString()
                    }
                }
            }
        }

        findViewById<Button>(R.id.returnButton).setOnClickListener{
            finish()
        }

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

    inner class ExercicioAdapter : BaseAdapter(){
        override fun getCount(): Int {
            TODO("Not yet implemented")
        }

        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }

        override fun getItemId(position: Int): Long {
            TODO("Not yet implemented")
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            TODO("Not yet implemented")
        }

    }
}