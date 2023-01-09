package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests

class Activity_Funcionario_Planos_Nutricionais_Add : AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_nutricionais_add)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Planos_Nutricionais_Add, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais_Add, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais_Add, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais_Add, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
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