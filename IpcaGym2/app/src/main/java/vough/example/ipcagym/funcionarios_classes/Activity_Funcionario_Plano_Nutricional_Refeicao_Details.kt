package vough.example.ipcagym.funcionarios_classes

import android.content.Context
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
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Details, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Details, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
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