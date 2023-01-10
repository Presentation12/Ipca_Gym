package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests

class Activity_Cliente_Nutricao_Planos : AppCompatActivity() {
    var list_planos_nutricionais = arrayListOf<Plano_Nutricional>()
    var planos_adapter = AdapterPlanoNutricao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_nutricao_planos)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic_cliente_planos_nutricionais_page)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

            if (resultCliente?.foto_perfil  != null && resultCliente.foto_perfil != "null")
            {
                val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageView.setImageBitmap(bitmap)
            }

            PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope,sessionToken,resultCliente?.id_ginasio){resultPlanosNutricionais ->
                list_planos_nutricionais = resultPlanosNutricionais
                planos_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Cliente_Nutricao_Planos, options)

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
                                    this@Activity_Cliente_Nutricao_Planos,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Nutricao_Planos,
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
                                this@Activity_Cliente_Nutricao_Planos,
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

        val list_view_planos_nutricionais = findViewById<ListView>(R.id.listviewPlanosNutricionais)
        list_view_planos_nutricionais.adapter = planos_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_diet);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterPlanoNutricao : BaseAdapter(){
        override fun getCount(): Int {
            return list_planos_nutricionais.size
        }

        override fun getItem(position: Int): Any {
            return list_planos_nutricionais[position]
        }

        override fun getItemId(position: Int): Long {
            return list_planos_nutricionais[position].id_plano_nutricional!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_plano_nutricional,parent,false)

            val tipo_plano_nutricional_text_view = root_view.findViewById<TextView>(R.id.textViewPlanoNutricional)
            tipo_plano_nutricional_text_view.text = list_planos_nutricionais[position].tipo

            val plano_nutricional_image_view = root_view.findViewById<ImageView>(R.id.imageViewPlanoNutricional)
            if (list_planos_nutricionais[position].foto_plano_nutricional  != null && list_planos_nutricionais[position].foto_plano_nutricional != "null")
            {
                val pictureByteArray = Base64.decode(list_planos_nutricionais[position].foto_plano_nutricional, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                plano_nutricional_image_view.setImageBitmap(bitmap)
            }

            //Clicar num rootView abre os detalhes do plano nutricional
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Nutricao_Planos, Activity_Cliente_Nutricao_Plano_Details::class.java)

                intent.putExtra("id_plano_nutricional", list_planos_nutricionais[position].id_plano_nutricional)
                intent.putExtra("id_ginasio", list_planos_nutricionais[position].id_ginasio)
                intent.putExtra("tipo", list_planos_nutricionais[position].tipo)
                intent.putExtra("calorias", list_planos_nutricionais[position].calorias)
                intent.putExtra("foto_plano_nutricional", list_planos_nutricionais[position].foto_plano_nutricional)

                startActivity(intent)
            }

            return root_view
        }

    }
}