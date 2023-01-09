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
import vough.example.ipcagym.requests.ClienteRequests

class Activity_Funcionario_Flux_Control_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_flux_control_add)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        //Botao de concluir criação
        val buttonAdd = findViewById<Button>(R.id.buttonAddActivity)
        val buttonEntry = findViewById<Button>(R.id.entryButton)
        val buttonExit = findViewById<Button>(R.id.exitButton)
        val id_cliente = findViewById<EditText>(R.id.newIDClienteActivity)
        var state = true

        buttonEntry.setOnClickListener{
            state = true
            findViewById<TextView>(R.id.textViewCurrentState).text = "Entry"
            Toast.makeText(this@Activity_Funcionario_Flux_Control_Add,"Activity marked as entry!", Toast.LENGTH_SHORT).show()
        }

        buttonExit.setOnClickListener{
            state = false
            findViewById<TextView>(R.id.textViewCurrentState).text = "Exit"
            Toast.makeText(this@Activity_Funcionario_Flux_Control_Add,"Activity marked as exit!", Toast.LENGTH_SHORT).show()
        }

        buttonAdd.setOnClickListener{
            val intent = Intent()

            ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente.text.toString().toInt()){ response ->
                if (response == null) Toast.makeText(this@Activity_Funcionario_Flux_Control_Add, "User not found!", Toast.LENGTH_SHORT).show()
                else{
                    intent.putExtra("id_cliente", id_cliente.text.toString().toInt())
                    intent.putExtra("state", state)

                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        }
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Flux_Control_Add, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}