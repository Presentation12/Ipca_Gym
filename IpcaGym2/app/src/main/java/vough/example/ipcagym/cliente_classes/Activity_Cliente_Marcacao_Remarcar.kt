package vough.example.ipcagym.cliente_classes

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
import vough.example.ipcagym.data_classes.Marcacao
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.MarcacaoRequests
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Marcacao_Remarcar : AppCompatActivity() {

    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_marcacao_remarcar)

        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        var data_marcacao = intent.getStringExtra("data_marcacao")
        val descricao = intent.getStringExtra("descricao")
        val estado = intent.getStringExtra("estado")

        var data_marcacao_formatado = LocalDateTime.parse(data_marcacao, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_remarcacao_page)
        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null){

                if (resultCliente.foto_perfil != null)
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }
            }
        }

        findViewById<TextView>(R.id.textViewOldDate).text = data_marcacao_formatado?.format(date_time_formatter)

        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Marcacao_Remarcar, options)

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
                                    this@Activity_Cliente_Marcacao_Remarcar,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Marcacao_Remarcar,
                                Activity_Cliente_Definitions::class.java
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
                                this@Activity_Cliente_Marcacao_Remarcar,
                                Activity_Cliente_Login::class.java
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

        // butao de remarcar consulta, e volta a página inicial
        findViewById<Button>(R.id.buttonMark).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Marcacoes::class.java)

            var newDateSelected = findViewById<EditText>(R.id.editTextDate).text.toString()
            var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            var newDateSelectedFormated = LocalDateTime.parse(newDateSelected, formatter)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
                if(resultCliente != null){
                    //TODO: devia printar o porque de nao dar para marcar ao cliente
                    var rescheduleMarcacao = Marcacao(id_marcacao, id_funcionario, resultCliente.id_cliente,newDateSelectedFormated,descricao,estado)
                    MarcacaoRequests.PatchRescheduleMarcacao(lifecycleScope, sessionToken,id_marcacao ,rescheduleMarcacao) { resultMarcacaoRemarcada ->
                        if (resultMarcacaoRemarcada == "Error: Patch Reschedule Marcacao Checked Product fails")
                            Toast.makeText(this@Activity_Cliente_Marcacao_Remarcar, "Error on reschedule an appointment", Toast.LENGTH_LONG).show()
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

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Marcacao_Remarcar, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }

}