package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Planos_Nutricionais_Add : AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_nutricionais_add)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Planos_Nutricionais_Add, options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        findViewById<Button>(R.id.buttonImportPhotoPlanNutri).setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //100 é o request code de escolher imagem
            startActivityForResult(intent, 100)
        }

        findViewById<Button>(R.id.CancelAddNewPlanNutriButton).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais_Add, Activity_Funcionario_Planos_Treino::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.addNewPlanNutriButton).setOnClickListener{
            val intent = Intent()

            intent.putExtra("id_plano_nutricional", 55)
            intent.putExtra("id_ginasio", 1)
            intent.putExtra("foto_plano_nutricional", newImageValue)
            intent.putExtra("tipo", findViewById<EditText>(R.id.tipoPlanoNutriValue).text.toString())

            if(findViewById<EditText>(R.id.caloriasPlanoNutriValue).text.toString() == ""){
                Toast.makeText(this@Activity_Funcionario_Planos_Nutricionais_Add, "Insira uma quantia de calorias", Toast.LENGTH_SHORT).show()
            }
            else{
                intent.putExtra("calorias", findViewById<EditText>(R.id.caloriasPlanoNutriValue).text.toString().toInt())
                setResult(RESULT_OK, intent);
                finish()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            newImageValue = data?.data.toString()
            findViewById<ImageView>(R.id.imageLoadedPlanNutri).setImageURI(data?.data)
        }
    }
}