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
import vough.example.ipcagym.cliente_classes.Activity_Cliente_Activity_Details
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class Activity_Funcionario_Cliente_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_detalhes_cliente)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val id_plano_nutricional : Int? = intent.getIntExtra("id_plano_nutricional", 0)
        val nome = intent.getStringExtra("nome")
        val mail = intent.getStringExtra("mail")
        val telemovel = intent.getIntExtra("telemovel",-1)
        val pass_salt = intent.getStringExtra("pass_salt")
        val pass_hash = intent.getStringExtra("pass_hash")
        val peso : Double? = intent.getDoubleExtra("peso",0.0)
        val altura : Int?  = intent.getIntExtra("altura",-1)
        val gordura : Double?  = intent.getDoubleExtra("gordura",0.0)
        val estado = intent.getStringExtra("estado")

        var imageView = findViewById<ImageView>(R.id.profile_pic_funcionario)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken) { resultFuncionario ->
            if(resultFuncionario != null){

                if(resultFuncionario.foto_funcionario != null && resultFuncionario.foto_funcionario.toString() == "null"){
                    val pictureByteArray = Base64.decode(resultFuncionario.foto_funcionario, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                AtividadeRequests.GetAllByClienteID(lifecycleScope,sessionToken,id_cliente){ resultAtividades ->
                    if(resultAtividades.isNotEmpty()){
                        val mes_atual_view = findViewById<TextView>(R.id.textView_month)

                        mes_atual_view.text = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault()).toString()

                        val media_entradas_anual_view = findViewById<TextView>(R.id.textViewMediaEntradasAnuais)
                        val media_entradas_mensal_view = findViewById<TextView>(R.id.textViewMediaEntradasMensais)
                        val entradas_mes_atual_view = findViewById<TextView>(R.id.textView_mes_dias)

                        var countEntradasMesAtual = 0
                        for (x in resultAtividades)
                        {
                            if (x.data_entrada?.month == LocalDate.now().month)
                            {
                                countEntradasMesAtual++
                            }
                        }

                        entradas_mes_atual_view.text = countEntradasMesAtual.toString()

                        val diffAux = Period.between(resultAtividades.first().data_entrada?.toLocalDate(), LocalDate.now())
                        val diffDaysMonths = diffAux.toTotalMonths()
                        var countMesesDesdeEntrada = diffDaysMonths / 30.44
                        var countAnosDesdeEntrada = countMesesDesdeEntrada / 365

                        if(countMesesDesdeEntrada < 1) countMesesDesdeEntrada++
                        if(countAnosDesdeEntrada < 1) countAnosDesdeEntrada++

                        media_entradas_anual_view.text = (resultAtividades.count()/countAnosDesdeEntrada).toString()
                        media_entradas_mensal_view.text = (resultAtividades.count()/countMesesDesdeEntrada).toString()
                    }
                }
            }
        }

        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){ clientResponse ->
            if(clientResponse != null && clientResponse.toString() == "null"){
                val pictureByteArray = Base64.decode(clientResponse.foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }
        }

        val nome_view = findViewById<TextView>(R.id.textViewNomeCliente)
        nome_view.text = nome

        val mail_view = findViewById<TextView>(R.id.textViewMail)
        mail_view.text = mail

        val contact_view = findViewById<TextView>(R.id.textViewContacto)
        contact_view.text = telemovel.toString()

        val peso_view = findViewById<TextView>(R.id.textViewPeso)
        if (peso.toString() != "NaN")
            peso_view.text = peso.toString()
        else
            peso_view.text = "---"

        val altura_view = findViewById<TextView>(R.id.textViewAltura)

        if (altura != 0)
            altura_view.text = altura.toString()
        else
            altura_view.text = "---"

        val gordura_view = findViewById<TextView>(R.id.textViewGordura)
        if (gordura.toString() != "NaN")
            gordura_view.text = gordura.toString()
        else
            gordura_view.text = "---"


        findViewById<Button>(R.id.button_alterar).setOnClickListener{
            ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){ clientResponse ->
                if(clientResponse != null){
                    val intent = Intent(this@Activity_Funcionario_Cliente_Details,Activity_Funcionario_Cliente_Edit::class.java)

                    intent.putExtra("id_cliente", id_cliente)
                    intent.putExtra("id_ginasio", id_ginasio)
                    intent.putExtra("id_plano_nutricional", id_plano_nutricional)
                    intent.putExtra("nome", nome)
                    intent.putExtra("mail", mail)
                    intent.putExtra("telemovel", telemovel)
                    intent.putExtra("pass_salt", pass_salt)
                    intent.putExtra("pass_hash", pass_hash)
                    intent.putExtra("peso", peso)
                    intent.putExtra("altura",altura)
                    intent.putExtra("gordura", gordura)
                    intent.putExtra("estado", estado)

                    startActivity(intent)
                }
                else
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details, "Error on editing client!", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.button_apagar).setOnClickListener{
            ClienteRequests.DeleteCliente(lifecycleScope,sessionToken,id_cliente){ resultClienteRemovido ->
                if(resultClienteRemovido == "Error: Delete Client fails")
                {
                    Toast.makeText(this@Activity_Funcionario_Cliente_Details,"Error : Removing Client",Toast.LENGTH_LONG).show()
                }
                else{
                    finish()
                    startActivity( Intent(this@Activity_Funcionario_Cliente_Details,Activity_Funcionario_Clientes_List::class.java))
                }
            }
        }

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Cliente_Details, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Login::class.java))
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
        bottomNavigationView.setSelectedItemId(R.id.nav_clients);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Cliente_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}