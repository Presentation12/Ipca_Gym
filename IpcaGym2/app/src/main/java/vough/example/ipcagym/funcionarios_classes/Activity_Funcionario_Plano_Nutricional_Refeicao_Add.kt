package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
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

        findViewById<Button>(R.id.importPhotoMeal).setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //100 é o request code de escolher imagem
            startActivityForResult(intent, 100)
        }

        //TODO: Verificar as horas nao passam dos limites
        findViewById<Button>(R.id.addNewMealButton).setOnClickListener {
            val newIntent = Intent()

            newIntent.putExtra("id_plano_nutricional", 1)
            newIntent.putExtra("id_refeicao", 55)
            newIntent.putExtra("descricao", findViewById<TextView>(R.id.refeicaoDescriptionValue).text.toString())
            newIntent.putExtra("hora_hour", findViewById<TextView>(R.id.refeicaoHourHourValue).text.toString().toInt())
            newIntent.putExtra("hora_minute", findViewById<TextView>(R.id.refeicaoHourMinuteValue).text.toString().toInt())
            newIntent.putExtra("foto_refeicao", newImageValue)

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