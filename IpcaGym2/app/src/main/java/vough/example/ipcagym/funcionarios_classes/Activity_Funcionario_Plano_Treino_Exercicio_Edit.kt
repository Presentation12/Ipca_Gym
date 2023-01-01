package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import vough.example.ipcagym.R

class Activity_Funcionario_Plano_Treino_Exercicio_Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_edit)
        //TODO: CASO AMBAS AS CAIXAS SEJAM DESATIVADAS
        var isSet = findViewById<CheckBox>(R.id.isSets)
        var isTime = findViewById<CheckBox>(R.id.isTime)

        findViewById<CheckBox>(R.id.timeMinExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.timeSecsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.textView53).isInvisible = true
        findViewById<CheckBox>(R.id.textView54).isInvisible = true

        findViewById<CheckBox>(R.id.SetsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.repetitionsExerciseValue).isInvisible = true
        findViewById<CheckBox>(R.id.textView55).isInvisible = true
        findViewById<CheckBox>(R.id.textView56).isInvisible = true

        findViewById<EditText>(R.id.nameExerciseValue).setText(intent.getStringExtra("nome"))
        findViewById<EditText>(R.id.descriptionExerciseValue).setText(intent.getStringExtra("descricao"))
        findViewById<EditText>(R.id.typeExerciseValue).setText(intent.getStringExtra("tipo"))

        if(intent.getStringExtra("aux") == "tempo"){
            findViewById<CheckBox>(R.id.timeMinExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.timeSecsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.textView53).isInvisible = false
            findViewById<CheckBox>(R.id.textView54).isInvisible = false
            isTime.isChecked = true
            isSet.isChecked = false

            findViewById<EditText>(R.id.timeMinExerciseValue).setText(intent.getStringExtra("tempo_min"))
            findViewById<EditText>(R.id.timeSecsExerciseValue).setText(intent.getStringExtra("tempo_sec"))
        }
        else if(intent.getStringExtra("aux") == "series"){
            findViewById<CheckBox>(R.id.SetsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.repetitionsExerciseValue).isInvisible = false
            findViewById<CheckBox>(R.id.textView55).isInvisible = false
            findViewById<CheckBox>(R.id.textView56).isInvisible = false
            isTime.isChecked = false
            isSet.isChecked = true

            findViewById<EditText>(R.id.repetitionsExerciseValue).setText(intent.getStringExtra("repeticoes"))
            findViewById<EditText>(R.id.SetsExerciseValue).setText(intent.getStringExtra("series"))
        }

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.buttonEditExercise).setOnClickListener{
            val intentEditIn = Intent()

            intentEditIn.putExtra("id_exercicio", intent.getIntExtra("id_exercicio", -1))
            intentEditIn.putExtra("id_plano_treino", intent.getIntExtra("id_plano_treino", -1))
            intentEditIn.putExtra("nome", findViewById<EditText>(R.id.nameExerciseValue).text.toString())
            intentEditIn.putExtra("descricao", findViewById<EditText>(R.id.descriptionExerciseValue).text.toString())
            intentEditIn.putExtra("tipo", findViewById<EditText>(R.id.typeExerciseValue).text.toString())

            //TODO: Tratar de foto
            intentEditIn.putExtra("foto_exercicio", "photo")

            //TODO: VERIFICAR QUE MIN < 60 e SEC < 60
            if(isSet.isChecked){
                intentEditIn.putExtra("tempoMin",0)
                intentEditIn.putExtra("tempoSec",0)

                intentEditIn.putExtra("repeticoes", findViewById<EditText>(R.id.repetitionsExerciseValue).text.toString().toInt())
                intentEditIn.putExtra("series", findViewById<EditText>(R.id.SetsExerciseValue).text.toString().toInt())

                intentEditIn.putExtra("aux","set")

                setResult(RESULT_OK, intentEditIn);
                finish()
            }
            else if(isTime.isChecked){
                intentEditIn.putExtra("repeticoes", -1)
                intentEditIn.putExtra("series", -1)

                intentEditIn.putExtra("tempoMin", findViewById<EditText>(R.id.timeMinExerciseValue).text.toString().toInt())
                intentEditIn.putExtra("tempoSec", findViewById<EditText>(R.id.timeSecsExerciseValue).text.toString().toInt())

                intentEditIn.putExtra("aux", "time")

                setResult(RESULT_OK, intentEditIn);
                finish()
            }else{
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Edit, "You need to insert more information!", Toast.LENGTH_LONG).show()
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