package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R

class Activity_Cliente_Activity_Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_atividade_details)

        //fazer na outra activity
        val id_atividade = intent.getIntExtra("id_atividade", -1)
        val data = intent.getStringExtra("data")
        val hora_entrada = intent.getStringExtra("hora_entrada")
        val hora_saida = intent.getStringExtra("hora_saida")

        //Escrever no layout detalhado
        findViewById<TextView>(R.id.activityDetailIDValue).text = id_atividade.toString()
        findViewById<TextView>(R.id.activityDetailHourEntradaValue).text = hora_entrada
        findViewById<TextView>(R.id.activityDetailDate).text = data

        if(hora_saida != null){
            findViewById<TextView>(R.id.activityDetailHourExitValue).text = hora_saida
        }
        else{
            findViewById<TextView>(R.id.activityDetailHourExitValue).text = "Currently on the gym"
        }

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Appointments", "Product Requests", "Rate", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 6
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Activity_Details, options)

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
                            spinner.setSelection(6)
                        } else {
                            startActivity(
                                Intent(
                                    this@Activity_Cliente_Activity_Details,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(6)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Activity_Details,
                                Activity_Cliente_Definitions::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    2 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Activity_Details,
                                Activity_Cliente_Marcacoes::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    3 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Activity_Details,
                                Activity_Cliente_Loja_Pedidos::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    4 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Activity_Details,
                                Activity_Cliente_Avaliar::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    5 -> {
                        val preferences =
                            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Activity_Details,
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

        imageView.setOnClickListener{ spinner.performClick() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Activity_Details, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Activity_Details, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Activity_Details, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Activity_Details, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Activity_Details, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }
}