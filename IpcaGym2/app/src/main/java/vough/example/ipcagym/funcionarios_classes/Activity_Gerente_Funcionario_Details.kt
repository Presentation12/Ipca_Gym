package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests

class Activity_Gerente_Funcionario_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_details)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val nome = intent.getStringExtra("nome")
        val is_admin = intent.getBooleanExtra("is_admin",false)
        val codigo = intent.getIntExtra("codigo",-1)
        val pass_salt = intent.getStringExtra("pass_salt")
        val pass_hash = intent.getStringExtra("pass_hash")
        val estado = intent.getStringExtra("estado")

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null)
            {
                if (resultGerente.foto_funcionario  != null && resultGerente.foto_funcionario != "null")
                {
                    val pictureByteArray = Base64.decode(resultGerente.foto_funcionario, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }
            }
        }

        GinasioRequests.GetByID(lifecycleScope,sessionToken,id_ginasio){ resultGinasio ->
            val nomeGinasio = findViewById<TextView>(R.id.GinasioNome)
            nomeGinasio.text = resultGinasio?.instituicao.toString()
        }
        val nomeFuncionario = findViewById<TextView>(R.id.nomeFuncionario)
        nomeFuncionario.text = nome
        val idFuncionario = findViewById<TextView>(R.id.IdFuncionario)
        idFuncionario.text = id_funcionario.toString()
        val codigoFuncionario = findViewById<TextView>(R.id.Codigo)
        codigoFuncionario.text = codigo.toString()
        val estadoFuncionario = findViewById<TextView>(R.id.Estado)
        estadoFuncionario.text = estado

        if(is_admin) findViewById<Button>(R.id.buttonRemoverBtn).isVisible = false

        val funcionario_editado_image_view = findViewById<ImageView>(R.id.imageView6)

        FuncionarioRequests.GetByID(lifecycleScope, sessionToken, id_funcionario){
            if(it != null && it.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(it.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                funcionario_editado_image_view.setImageBitmap(bitmap)
            }
        }

        // TODO: quando houver linkagem alterar aqui para remover funcionario
        findViewById<Button>(R.id.buttonRemoverBtn).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Funcionarios_List::class.java)
            // ele altera o estado no backend
            FuncionarioRequests.DeleteFuncionario(lifecycleScope,sessionToken,id_funcionario){ resultRemoveFuncionario ->
                if (resultRemoveFuncionario == "Error: Delete Employee fails")
                    Toast.makeText(this@Activity_Gerente_Funcionario_Details, "Cannot remove admins", Toast.LENGTH_LONG).show()
                else
                {
                    finish()
                    startActivity(intent)
                }
            }

            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonEditar).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Funcionario_Edit::class.java)

            intent.putExtra("id_funcionario", id_funcionario)
            intent.putExtra("id_ginasio", id_ginasio)
            intent.putExtra("nome", nome)
            intent.putExtra("is_admin", is_admin)
            intent.putExtra("codigo", codigo)
            intent.putExtra("pass_salt", pass_salt)
            intent.putExtra("pass_hash", pass_hash)
            intent.putExtra("estado", estado)

            startActivity(intent)
        }

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

        val adapter = MyAdapter(this@Activity_Gerente_Funcionario_Details, options)
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
                                startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Login::class.java))
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
                                startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Login::class.java))
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

        imageView.setOnClickListener{ spinner.performClick() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

    }
}