package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Gerente_Ginasio_Patch, options)
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
                            startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
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
                                    startActivity(Intent(this@Activity_Gerente_Ginasio_Patch, Activity_Funcionario_Pagina_Inicial::class.java))
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