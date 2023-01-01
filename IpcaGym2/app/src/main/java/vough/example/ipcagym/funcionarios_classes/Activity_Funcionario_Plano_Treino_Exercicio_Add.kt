package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import vough.example.ipcagym.R

class Activity_Funcionario_Plano_Treino_Exercicio_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_add)
        //TODO: CASO AMBAS AS CAIXAS SEJAM DESATIVADAS
        val isSet = findViewById<CheckBox>(R.id.isSets)
        val isTime = findViewById<CheckBox>(R.id.isTime)

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
            val intent = Intent()

            intent.putExtra("id_exercicio", 55)
            //intent.putExtra("id_exercicio", findViewById<EditText>(R.id.nameExerciseValue).text.toString().toInt())
            intent.putExtra("id_plano_treino", 1)
            //intent.putExtra("id_plano_treino", findViewById<EditText>(R.id.id_plano_treino).text.toString().toInt())
            intent.putExtra("nome", findViewById<EditText>(R.id.nameExerciseValue).text.toString())
            intent.putExtra("descricao", findViewById<EditText>(R.id.descriptionExerciseValue).text.toString())
            intent.putExtra("tipo", findViewById<EditText>(R.id.typeExerciseValue).text.toString())

            intent.putExtra("foto_exercicio", "photo")

            //TODO: VERIFICAR SE MINUTOS < 60 E SECS < 60
            if(isSet.isChecked){
                intent.putExtra("tempoMin", "")
                intent.putExtra("tempoSec", "")

                intent.putExtra("repeticoes", findViewById<EditText>(R.id.repetitionsExerciseValue).text.toString().toInt())
                intent.putExtra("series", findViewById<EditText>(R.id.SetsExerciseValue).text.toString().toInt())

                intent.putExtra("aux", "set")

                setResult(RESULT_OK, intent);
                finish()
            }
            else if(isTime.isChecked){
                intent.putExtra("series", -1)
                intent.putExtra("repeticoes", -1)

                intent.putExtra("tempoMin", findViewById<EditText>(R.id.timeMinExerciseValue).text.toString())
                intent.putExtra("tempoSec", findViewById<EditText>(R.id.timeSecsExerciseValue).text.toString())

                intent.putExtra("aux", "time")

                setResult(RESULT_OK, intent);
                finish()
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