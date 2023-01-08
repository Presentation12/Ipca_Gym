package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Ginasio
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests

class Activity_Gerente_Ginasio_Patch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_ginasio_edit)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)

        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Gerente_Ginasio_Patch,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            GinasioRequests.GetByID(lifecycleScope, sessionToken, it?.id_ginasio!!){ gymResponse ->
                if(gymResponse != null){
                    findViewById<EditText>(R.id.editTextNumber2).setText(gymResponse.contacto.toString())
                    findViewById<EditText>(R.id.editTextNumber3).setText(gymResponse.lotacaoMax.toString())
                }
            }
        }

        findViewById<Button>(R.id.buttonEdit).setOnClickListener{
            if(findViewById<EditText>(R.id.editTextNumber2).text.toString() == "" || findViewById<EditText>(R.id.editTextNumber3).text.toString() == ""){
                Toast.makeText(this@Activity_Gerente_Ginasio_Patch, "Fill in all fields", Toast.LENGTH_LONG).show()
            }
            else{
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    GinasioRequests.GetByID(lifecycleScope, sessionToken, it?.id_ginasio!!){ gymResponse ->
                        if(gymResponse != null){
                            GinasioRequests.Patch(lifecycleScope, sessionToken, it?.id_ginasio!!, Ginasio(
                                gymResponse?.id_ginasio!!,
                                gymResponse?.instituicao!!,
                                gymResponse?.estado!!,
                                gymResponse?.foto_ginasio!!,
                                findViewById<EditText>(R.id.editTextNumber2).text.toString().toInt(),
                                gymResponse?.lotacao!!,
                                findViewById<EditText>(R.id.editTextNumber3).text.toString().toInt(),
                            )){ editGymResponse ->
                                if(editGymResponse != "User not found"){
                                    Toast.makeText(this@Activity_Gerente_Ginasio_Patch, "Gym data edited successfully", Toast.LENGTH_LONG).show()
                                    finish()
                                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, PaginaInicialFuncionarioActivity::class.java))
                                }
                                else{
                                    Toast.makeText(this@Activity_Gerente_Ginasio_Patch, "Error on editing the gym data", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}