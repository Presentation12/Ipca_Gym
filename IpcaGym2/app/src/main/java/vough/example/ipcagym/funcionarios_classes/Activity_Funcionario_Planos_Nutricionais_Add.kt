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
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests

class Activity_Funcionario_Planos_Nutricionais_Add : AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_nutricionais_add)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
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
            finish()
            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais_Add, Activity_Funcionario_Planos_Nutricionais::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.addNewPlanNutriButton).setOnClickListener{
            val intent = Intent()

            if(findViewById<EditText>(R.id.caloriasPlanoNutriValue).text.toString() == ""){
                Toast.makeText(this@Activity_Funcionario_Planos_Nutricionais_Add, "Insert ammount of calories", Toast.LENGTH_SHORT).show()
            }
            else{

                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    PlanoNutricionalRequests.Post(lifecycleScope, sessionToken, Plano_Nutricional(
                        null,
                        it?.id_ginasio!!,
                        findViewById<EditText>(R.id.tipoPlanoNutriValue).text.toString(),
                        findViewById<EditText>(R.id.caloriasPlanoNutriValue).text.toString().toInt(),
                        newImageValue
                    )){ response ->
                        if(response == "User not found")
                            Toast.makeText(this@Activity_Funcionario_Planos_Nutricionais_Add, "Error on adding plan", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this@Activity_Funcionario_Planos_Nutricionais_Add, "Plan added successfully", Toast.LENGTH_SHORT).show()
                    }
                }

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