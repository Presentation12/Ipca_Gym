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
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Treino_Exercicio_Details, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if(counter == 0){
                            counter+=1
                            spinner.setSelection(3)
                        }
                        else{
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Details, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

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