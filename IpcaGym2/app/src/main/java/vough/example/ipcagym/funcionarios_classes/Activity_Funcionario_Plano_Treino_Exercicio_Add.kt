package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ExercicioRequests
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Treino_Exercicio_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_add)
        //TODO: CASO AMBAS AS CAIXAS SEJAM DESATIVADAS
        val isSet = findViewById<CheckBox>(R.id.isSets)
        val isTime = findViewById<CheckBox>(R.id.isTime)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        findViewById<CheckBox>(R.id.timeMinExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.timeSecsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.textView53).isInvisible = true
        findViewById<CheckBox>(R.id.textView54).isInvisible = true

        findViewById<CheckBox>(R.id.SetsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.repetitionsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.textView55).isInvisible = true
        findViewById<CheckBox>(R.id.textView56).isInvisible = true

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.addNewPlanButton).setOnClickListener{
            val intentAddIn = Intent()

            intentAddIn.putExtra("nome", findViewById<EditText>(R.id.nameExerciseValue).text.toString())
            intentAddIn.putExtra("descricao", findViewById<EditText>(R.id.descriptionExerciseValue).text.toString())
            intentAddIn.putExtra("tipo", findViewById<EditText>(R.id.typeExerciseValue).text.toString())

            //TODO: Adicionar foto
            intentAddIn.putExtra("foto_exercicio", "photo")

            //TODO: VERIFICAR SE MINUTOS < 60 E SECS < 60
            if(isSet.isChecked){
                intentAddIn.putExtra("tempoMin", "")
                intentAddIn.putExtra("tempoSec", "")

                intentAddIn.putExtra("repeticoes", findViewById<EditText>(R.id.repetitionsExerciseValue).text.toString().toInt())
                intentAddIn.putExtra("series", findViewById<EditText>(R.id.SetsExerciseValue).text.toString().toInt())

                intentAddIn.putExtra("aux", "set")

                val jsonBody = """
                    {
                      "id_plano_treino": ${intent.getIntExtra("id_plano_treino", -1)},
                      "nome": "${intentAddIn.getStringExtra("nome")}",
                      "descricao": "${intentAddIn.getStringExtra("descricao")}",
                      "tipo": "${intentAddIn.getStringExtra("tipo")}",
                      "series": ${intentAddIn.getIntExtra("series", -1)},
                      "tempo": null,
                      "repeticoes": ${intentAddIn.getIntExtra("repeticoes", -1)},
                      "foto_exercicio": "${intentAddIn.getStringExtra("foto_exercicio")}"
                    }
                """
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, intent.getIntExtra("id_plano_treino", -1).toString(), Toast.LENGTH_SHORT).show()
                ExercicioRequests.Post(lifecycleScope, sessionToken, jsonBody){ result ->
                    if(result != "User not found"){
                        setResult(RESULT_OK, intentAddIn);
                        finish()
                    }
                    else
                        Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, "Error on adding exercise", Toast.LENGTH_SHORT).show()
                }

                setResult(RESULT_OK, intentAddIn);
                finish()
            }
            else if(isTime.isChecked){
                intentAddIn.putExtra("series", -1)
                intentAddIn.putExtra("repeticoes", -1)

                intentAddIn.putExtra("tempoMin", findViewById<EditText>(R.id.timeMinExerciseValue).text.toString().toInt())
                intentAddIn.putExtra("tempoSec", findViewById<EditText>(R.id.timeSecsExerciseValue).text.toString().toInt())

                intentAddIn.putExtra("aux", "time")

                var minAuxInt = intentAddIn.getIntExtra("tempoMin",-1)
                var secAuxInt = intentAddIn.getIntExtra("tempoSec",-1)

                var minAuxString : String?
                var secAuxString : String?

                if(minAuxInt < 10)
                    minAuxString = "0$minAuxInt"
                else
                    minAuxString = minAuxInt.toString()

                if(secAuxInt < 10)
                    secAuxString = "0$secAuxInt"
                else
                    secAuxString = secAuxInt.toString()

                val tempoToPatch = LocalTime.parse("00:$minAuxString:$secAuxString", DateTimeFormatter.ofPattern("HH:mm:ss"))

                val jsonBody = """
                    {
                      "id_plano_treino": ${intent.getIntExtra("id_plano_treino", -1)},
                      "nome": "${intentAddIn.getStringExtra("nome")}",
                      "descricao": "${intentAddIn.getStringExtra("descricao")}",
                      "tipo": "${intentAddIn.getStringExtra("tipo")}",
                      "series": null,
                      "tempo": "$tempoToPatch",
                      "repeticoes": null,
                      "foto_exercicio": "${intentAddIn.getStringExtra("foto_exercicio")}"
                    }
                """

                ExercicioRequests.Post(lifecycleScope, sessionToken, jsonBody){ result ->
                    if(result != "User not found"){
                        setResult(RESULT_OK, intentAddIn);
                        finish()
                    }
                    else
                        Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, "Error on adding new exercise", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, "You need to insert more information!", Toast.LENGTH_LONG).show()
            }
        }

        isSet.setOnClickListener{
            isTime.isChecked = false

            findViewById<CheckBox>(R.id.SetsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.repetitionsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.textView55).isInvisible = false
            findViewById<CheckBox>(R.id.textView56).isInvisible = false

            findViewById<CheckBox>(R.id.timeMinExerciseValue).isInvisible = true
            findViewById<CheckBox>(R.id.timeSecsExerciseValue).isInvisible = true
            findViewById<CheckBox>(R.id.textView53).isInvisible = true
            findViewById<CheckBox>(R.id.textView54).isInvisible = true
        }

        isTime.setOnClickListener{
            isSet.isChecked = false

            findViewById<CheckBox>(R.id.SetsExerciseValue).isInvisible = true
            findViewById<CheckBox>(R.id.repetitionsExerciseValue).isInvisible = true
            findViewById<CheckBox>(R.id.textView55).isInvisible = true
            findViewById<CheckBox>(R.id.textView56).isInvisible = true

            findViewById<CheckBox>(R.id.timeMinExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.timeSecsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.textView53).isInvisible = false
            findViewById<CheckBox>(R.id.textView54).isInvisible = false
        }
    }
}