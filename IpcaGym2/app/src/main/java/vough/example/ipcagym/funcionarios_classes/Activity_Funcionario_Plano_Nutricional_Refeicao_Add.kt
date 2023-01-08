package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Refeicao
import vough.example.ipcagym.requests.RefeicaoRequests
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Nutricional_Refeicao_Add : AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicao_add)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        findViewById<Button>(R.id.importPhotoMeal).setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //100 é o request code de escolher imagem
            startActivityForResult(intent, 100)
        }

        //TODO: Verificar as horas nao passam dos limites
        findViewById<Button>(R.id.addNewMealButton).setOnClickListener {
            val newIntent = Intent()

            var minAuxInt = findViewById<TextView>(R.id.refeicaoHourHourValue).text.toString().toInt()
            var secAuxInt = findViewById<TextView>(R.id.refeicaoHourMinuteValue).text.toString().toInt()

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

            val tempoToPatch = "$minAuxString:$secAuxString:00"

            val jsonBody = """
                {
                  "id_plano_nutricional": ${intent.getIntExtra("id_plano_nutricional", -1)},
                  "descricao": "${findViewById<TextView>(R.id.refeicaoDescriptionValue).text}",
                  "hora": "$tempoToPatch",
                  "foto_refeicao": "$newImageValue"
                }
            """

            RefeicaoRequests.Post(lifecycleScope, sessionToken, jsonBody){
                if(it != "User not found")
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Add, "Meal added sucessfully", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Add, "Error on adding meal", Toast.LENGTH_LONG).show()
            }

            setResult(RESULT_OK, newIntent)
            finish()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            newImageValue = data?.data.toString()
            findViewById<ImageView>(R.id.refeicaoPhotoValue).setImageURI(data?.data)
        }
    }
}