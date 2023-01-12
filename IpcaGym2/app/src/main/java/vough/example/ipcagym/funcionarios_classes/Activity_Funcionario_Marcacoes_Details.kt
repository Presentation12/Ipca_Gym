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
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Marcacoes_Details: AppCompatActivity() {
    val date_time_formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_marcacoes_details)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_marcacao = intent.getStringExtra("data_marcacao")

        val estado = intent.getStringExtra("estado")
        val descricao = intent.getStringExtra("descricao")

        var data_marcacao_formatado = LocalDateTime.parse(data_marcacao, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) {
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
            }
        }

        findViewById<TextView>(R.id.marcacaoidmarcacaovalue).text = id_marcacao.toString()

        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
            findViewById<TextView>(R.id.marcacaoclientevalue).text = it?.nome.toString()
        }

        FuncionarioRequests.GetByID(lifecycleScope, sessionToken, id_funcionario){
            findViewById<TextView>(R.id.marcacaofuncionariovalue).text = it?.nome.toString()
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

        val adapter = MyAdapter(this@Activity_Funcionario_Marcacoes_Details, options)
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
                                startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Login::class.java))
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
                                startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Login::class.java))
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

        findViewById<TextView>(R.id.marcacaodatavalue).text = data_marcacao_formatado?.format(date_time_formatter)
        findViewById<TextView>(R.id.marcacaodescricaovalue).text = descricao
        findViewById<TextView>(R.id.marcacaoestadovalue).text = estado

        val buttonCancelar = findViewById<Button>(R.id.buttonCancelar)
        val buttonRemarcar = findViewById<Button>(R.id.buttonRemarcar)

        if(estado == "Ativo"){
            buttonCancelar.visibility = View.VISIBLE
            buttonRemarcar.visibility = View.VISIBLE
        }


        buttonCancelar.setOnClickListener{
           MarcacaoRequests.PatchCancelMarcacao(lifecycleScope, sessionToken, id_marcacao, Marcacao(
                id_marcacao,
                id_funcionario,
                id_cliente,
                data_marcacao_formatado,
                descricao,
                estado)){ result ->
               if(result != "Error: Patch Cancel Marcacao Checked Product fails"){
                   Toast.makeText(this@Activity_Funcionario_Marcacoes_Details, "Appointment cancelled successfully", Toast.LENGTH_SHORT).show()
                   finish()
                   startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Marcacoes::class.java))
               }
               else
                   Toast.makeText(this@Activity_Funcionario_Marcacoes_Details, "Error on cancelling appointment", Toast.LENGTH_SHORT).show()
            }
        }

        buttonRemarcar.setOnClickListener{
            var intent = Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Marcacao_Remarcar::class.java)
            intent.putExtra("id_marcacao", id_marcacao)
            intent.putExtra("id_funcionario", id_funcionario)
            intent.putExtra("id_cliente", id_cliente)
            intent.putExtra("data_marcacao", data_marcacao_formatado.toString())
            intent.putExtra("estado", estado)
            intent.putExtra("descricao", descricao)

            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Marcacoes_Details, Activity_Funcionario_Flux_Control::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}