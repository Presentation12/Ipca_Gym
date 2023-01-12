package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests

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
        var state : Boolean? = null

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null){
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
            }
        }


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
            if(state != null){
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
            else{
                Toast.makeText(this@Activity_Funcionario_Flux_Control_Add,"Select if the client is making an entry or exit!", Toast.LENGTH_SHORT).show()
            }
        }
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

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

        val adapter = MyAdapter(this@Activity_Funcionario_Flux_Control_Add, options)
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
                                startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Login::class.java))
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
                                startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Login::class.java))
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
                    startActivity(Intent(this@Activity_Funcionario_Flux_Control_Add, Activity_Funcionario_Loja_Pedidos::class.java))
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