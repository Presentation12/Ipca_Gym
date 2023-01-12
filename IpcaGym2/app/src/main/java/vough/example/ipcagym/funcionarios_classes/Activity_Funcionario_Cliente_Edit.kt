package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Funcionario_Cliente_Edit: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_cliente_edit)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        var id_cliente = intent.getIntExtra("id_cliente", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var id_plano_nutricional : Int? = intent.getIntExtra("id_plano_nutricional", 0)
        var nome = intent.getStringExtra("nome")
        var mail = intent.getStringExtra("mail")
        var telemovel = intent.getIntExtra("telemovel",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var peso : Double? = intent.getDoubleExtra("peso",0.0)
        var altura : Int?  = intent.getIntExtra("altura",-1)
        var gordura : Double?  = intent.getDoubleExtra("gordura",0.0)
        var estado = intent.getStringExtra("estado")

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")
        var imageView = findViewById<ImageView>(R.id.profile_pic_funcionario)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultFuncionario ->
            // foto funcionario
            if(resultFuncionario?.foto_funcionario != null && resultFuncionario?.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(resultFuncionario.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageView.setImageBitmap(bitmap)
            }
        }

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Cliente_Edit, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        if (counter == 0) {
                            counter += 1
                            spinner.setSelection(3)
                        } else {
                            startActivity(
                                Intent(
                                    this@Activity_Funcionario_Cliente_Edit,
                                    Activity_Funcionario_Perfil_Edit::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Funcionario_Cliente_Edit,
                                Activity_Funcionario_Settings::class.java
                            )
                        )
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences =
                            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(
                            Intent(
                                this@Activity_Funcionario_Cliente_Edit,
                                Activity_Funcionario_Login::class.java
                            )
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        findViewById<TextView>(R.id.textViewNomeCliente).text = nome
        findViewById<TextView>(R.id.editTextWeight).hint = "${peso} Kg"
        findViewById<TextView>(R.id.editTextHeight).hint = "${altura} Cm"
        findViewById<TextView>(R.id.editTextFat).hint = "${gordura} %"


        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
            if(it != null && it.foto_perfil.toString() != "null"){
                val pictureByteArray = Base64.decode(it.foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }
        }

        // butao de guardar cliente editado, e volta a página ida lista de clientes
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(
                this@Activity_Funcionario_Cliente_Edit,
                Activity_Funcionario_Clientes_List::class.java
            )

            if (findViewById<EditText>(R.id.editTextWeight).text.isEmpty() == false) {
                peso = findViewById<EditText>(R.id.editTextWeight).text.toString().toDouble()
            }
            if (findViewById<EditText>(R.id.editTextHeight).text.isEmpty() == false) {
                altura = findViewById<EditText>(R.id.editTextHeight).text.toString().toInt()
            }
            if (findViewById<EditText>(R.id.editTextFat).text.isEmpty() == false) {
                gordura = findViewById<EditText>(R.id.editTextFat).text.toString().toDouble()
            }

        if(id_plano_nutricional == 0) id_plano_nutricional = null
        if(peso.toString() == "NaN") peso = null
        if(altura == 0) altura = null
        if(gordura.toString() == "NaN") gordura = null

            ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){ response ->
                if(response != null){
                    val editCliente = Cliente(id_cliente,id_ginasio,id_plano_nutricional,nome,mail,telemovel,pass_salt,pass_hash,peso,altura,gordura,response.foto_perfil,estado)
                    ClienteRequests.Patch(lifecycleScope,sessionToken,id_cliente, editCliente) { resultEditcliente ->
                        if (resultEditcliente == "Error: Patch Client fails")
                        {
                            Toast.makeText(this@Activity_Funcionario_Cliente_Edit, "Error on editting cliente measurements", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            finish()
                            startActivity(intent)
                        }
                    }
                }
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_clients);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}