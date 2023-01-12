package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ExercicioRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import java.time.LocalDateTime
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

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) {
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }
        }

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val sessionRole = preferences.getString("session_role", null)
        var options: List<String>

        if(sessionRole == "Funcionario"){
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Logout", "")
        }
        else{
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Employees" , "Gym Edit", "Logout", "")
        }

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return if(sessionRole == "Funcionario") {
                    8
                }else{
                    10
                }
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(sessionRole == "Funcionario"){
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(8)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
                else{
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(10)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                if(sessionRole == "Funcionario")
                    spinner.setSelection(8)
                else
                    spinner.setSelection(10)
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
                      "foto_exercicio": null
                    }
                """
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, intent.getIntExtra("id_plano_treino", -1).toString(), Toast.LENGTH_SHORT).show()
                ExercicioRequests.Post(lifecycleScope, sessionToken, jsonBody){ result ->
                    if(result != "Error: Post Exercise fails"){
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

                var tempoToPatch : LocalTime? = null
                var stringTime : String? = null

                if(secAuxInt == 0){
                    tempoToPatch = LocalTime.parse("00:$minAuxString:00", DateTimeFormatter.ofPattern("HH:mm:ss"))
                    stringTime = tempoToPatch.toString()+":00"
                }
                else {
                    tempoToPatch = LocalTime.parse("00:$minAuxString:$secAuxString",DateTimeFormatter.ofPattern("HH:mm:ss"))
                    stringTime = tempoToPatch.toString()
                }

                val jsonBody = """
                    {
                      "id_plano_treino": ${intent.getIntExtra("id_plano_treino", -1)},
                      "nome": "${intentAddIn.getStringExtra("nome")}",
                      "descricao": "${intentAddIn.getStringExtra("descricao")}",
                      "tipo": "${intentAddIn.getStringExtra("tipo")}",
                      "series": null,
                      "tempo": "$stringTime",
                      "repeticoes": null,
                      "foto_exercicio": null
                    }
                """

                ExercicioRequests.Post(lifecycleScope, sessionToken, jsonBody){ result ->
                    if(result != "Error: Post Exercise fails"){
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicio_Add, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
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