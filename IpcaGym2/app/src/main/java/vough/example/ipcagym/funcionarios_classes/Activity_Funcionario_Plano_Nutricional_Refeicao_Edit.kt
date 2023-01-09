package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.RefeicaoRequests

class Activity_Funcionario_Plano_Nutricional_Refeicao_Edit: AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicao_edit)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        findViewById<TextView>(R.id.refeicaoEditDescriptionValue).text = intent.getStringExtra("descricao")
        findViewById<TextView>(R.id.refeicaoEditHourHourValue).text = intent.getIntExtra("hora_hora", -1).toString()
        findViewById<TextView>(R.id.refeicaoEditHourMinuteValue).text = intent.getIntExtra("hora_minute", -1).toString()
        findViewById<ImageView>(R.id.refeicaoEditPhotoValue).setImageURI(intent.getStringExtra("foto_refeicao")?.toUri())

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        findViewById<Button>(R.id.importEditPhotoMeal).setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //100 é o request code de escolher imagem
            startActivityForResult(intent, 100)
        }

        findViewById<Button>(R.id.editMealButton).setOnClickListener{
            val editIntent = Intent()

            var minAuxInt = findViewById<TextView>(R.id.refeicaoEditHourHourValue).text.toString().toInt()
            var secAuxInt = findViewById<TextView>(R.id.refeicaoEditHourMinuteValue).text.toString().toInt()

            var photo : String?

            if(newImageValue != "") photo = newImageValue
            else photo = intent.getStringExtra("foto_refeicao")

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
                  "id_refeicao": ${intent.getIntExtra("id_refeicao", -1)},
                  "id_plano_nutricional": ${intent.getIntExtra("id_plano_nutricional", -1)},
                  "descricao": "${findViewById<TextView>(R.id.refeicaoEditDescriptionValue).text}",
                  "hora": "$tempoToPatch",
                  "foto_refeicao": "$photo"
                }
            """

            RefeicaoRequests.Patch(lifecycleScope, sessionToken, intent.getIntExtra("id_refeicao", -1) , jsonBody){
                if(it != "User not found")
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, "Meal added sucessfully", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, "Error on adding meal", Toast.LENGTH_LONG).show()
            }

            setResult(RESULT_OK, editIntent)
            finish()
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            newImageValue = data?.data.toString()
            findViewById<ImageView>(R.id.refeicaoEditPhotoValue).setImageURI(data?.data)
        }
    }
}