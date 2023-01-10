package vough.example.ipcagym.cliente_classes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.data_classes.Refeicao
import vough.example.ipcagym.requests.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Cliente_Nutricao_Atual : AppCompatActivity() {

        @RequiresApi(Build.VERSION_CODES.O)
        val time_formatter = DateTimeFormatter.ofPattern("HH:mm")
        var list_refeicoes = arrayListOf<Refeicao>()
        var refeicao_adapter = AdapterRefeicao()

        //api para tempo
        @SuppressLint("CutPasteId")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cliente_nutricao)

            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_nutricao)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

                if (resultCliente?.foto_perfil != null && resultCliente?.foto_perfil != "null")
                {
                    val pictureByteArray = Base64.decode(resultCliente?.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                if (resultCliente?.id_plano_nutricional != 0 && resultCliente?.id_plano_nutricional != null)
                {
                    PlanoNutricionalRequests.GetByID(lifecycleScope, sessionToken, resultCliente?.id_plano_nutricional) { resultPlanoAtual ->

                        findViewById<TextView>(R.id.textViewCalorias).text = resultPlanoAtual?.calorias.toString()
                        findViewById<TextView>(R.id.textViewTipoNutricao).text = resultPlanoAtual?.tipo

                        RefeicaoRequests.GetAllByPlanoID(lifecycleScope,sessionToken,resultPlanoAtual?.id_plano_nutricional){ resultRefeicoes ->

                            list_refeicoes = resultRefeicoes
                            refeicao_adapter.notifyDataSetChanged()
                        }
                    }
                }
                else{
                    findViewById<TextView>(R.id.textViewCalorias).isInvisible = true
                    findViewById<TextView>(R.id.textViewTipoNutricao).isInvisible = true
                    findViewById<TextView>(R.id.textView13).isInvisible = false
                    findViewById<Button>(R.id.buttonPlanosNutricao).setText("Add")

                    findViewById<TextView>(R.id.textViewMeal).isInvisible = true
                    findViewById<ListView>(R.id.listviewRefeicoes).isInvisible = true
                }
            }

            val spinner = findViewById<Spinner>(R.id.spinner)
            var counter = 0
            val options = listOf("Account", "Settings", "Logout", "")

            class MyAdapter(context: Context, items: List<String>) :
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
                override fun getCount(): Int {
                    return 3
                }
            }

            val adapter = MyAdapter(this@Activity_Cliente_Nutricao_Atual, options)

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
                                        this@Activity_Cliente_Nutricao_Atual,
                                        Activity_Cliente_Account::class.java
                                    )
                                )
                                spinner.setSelection(3)
                            }
                        }
                        1 -> {
                            startActivity(
                                Intent(
                                    this@Activity_Cliente_Nutricao_Atual,
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
                                    this@Activity_Cliente_Nutricao_Atual,
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
            imageView.setOnClickListener {
                spinner.performClick()
            }

            val list_view_refeicoes = findViewById<ListView>(R.id.listviewRefeicoes)
            list_view_refeicoes.adapter = refeicao_adapter

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

            bottomNavigationView.setSelectedItemId(R.id.nav_diet);
            bottomNavigationView.setOnItemSelectedListener{ item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Pagina_Inicial::class.java))
                        finish()

                        true
                    }
                    R.id.nav_fitness -> {
                        startActivity(Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Planos_Treino::class.java))
                        finish()

                        true
                    }
                    R.id.nav_shopping -> {
                        startActivity(Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Loja_Produtos::class.java))
                        finish()

                        true
                    }
                    R.id.nav_diet -> {
                        finish()

                        true
                    }
                    R.id.nav_history -> {
                        startActivity(Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Activities::class.java))
                        finish()

                        true
                    }
                    else -> false
                }
            }

            findViewById<Button>(R.id.buttonPlanosNutricao).setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Nutricao_Planos::class.java)
                startActivity(intent)
            }
        }

        inner class AdapterRefeicao : BaseAdapter(){
            override fun getCount(): Int {
                return list_refeicoes.size
            }

            override fun getItem(position: Int): Any {
                return list_refeicoes[position]
            }

            override fun getItemId(position: Int): Long {
                return list_refeicoes[position].id_refeicao!!.toLong()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val root_view = layoutInflater.inflate(R.layout.row_refeicao_cliente,parent,false)

                val refeicao_text_view = root_view.findViewById<TextView>(R.id.textViewHoraRefeicao)
                refeicao_text_view.text = list_refeicoes[position].hora.toString()

                val refeicao_image_view = root_view.findViewById<ImageView>(R.id.imageViewRefeicao)
                if (list_refeicoes[position].foto_refeicao  != null && list_refeicoes[position].foto_refeicao != "null")
                {
                    val pictureByteArray = Base64.decode(list_refeicoes[position].foto_refeicao, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    refeicao_image_view.setImageBitmap(bitmap)
                }

                //Clicar num rootView abre o plano de treino
                root_view.setOnClickListener {
                    val intent = Intent(this@Activity_Cliente_Nutricao_Atual, Activity_Cliente_Nutricao_Atual_Refeicao::class.java)

                    intent.putExtra("id_refeicao", list_refeicoes[position].id_refeicao)
                    intent.putExtra("id_plano_nutricional", list_refeicoes[position].id_plano_nutricional)
                    intent.putExtra("descricao", list_refeicoes[position].descricao)
                    intent.putExtra("hora", list_refeicoes[position].hora?.format(time_formatter))
                    intent.putExtra("foto_refeicao", list_refeicoes[position].foto_refeicao)

                    startActivity(intent)
                }

                return root_view
            }

        }
}