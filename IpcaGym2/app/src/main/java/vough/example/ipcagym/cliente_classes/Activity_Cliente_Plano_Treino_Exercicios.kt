package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.requests.ExercicioRequests

class Activity_Cliente_Plano_Treino_Exercicios : AppCompatActivity() {

    var exercicios_plano_list = arrayListOf<Exercicio>()
    var exercicio_adapter = AdapterExercicios()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_treino_plano_exercicios)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_plano_treino = intent.getIntExtra("id_plano_treino", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        val tipo = intent.getStringExtra("tipo")
        val foto_plano_treino = intent.getStringExtra("foto_plano_treino")
        findViewById<TextView>(R.id.textView31).isInvisible = true
        val imageView = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente?.id_cliente != null)
            {
                if (resultCliente.foto_perfil != null && resultCliente?.foto_perfil != "null")
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                ExercicioRequests.GetAllByPlanoID(lifecycleScope, sessionToken, id_plano_treino) { resultExercicio ->
                    if(resultExercicio.isNotEmpty()){
                        exercicios_plano_list = resultExercicio
                        exercicio_adapter.notifyDataSetChanged()
                    }
                    else{
                        findViewById<TextView>(R.id.textView31).text = "This plan is empty!"
                        findViewById<TextView>(R.id.textView31).isInvisible = false
                    }


                }
            }
        }
        findViewById<TextView>(R.id.textViewType).text = tipo

        val spinner = findViewById<Spinner>(R.id.spinner)
        var counter = 0
        val options = listOf("Account", "Settings", "Appointments", "Product Requests", "Rate", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 6
            }
        }

        val adapter = MyAdapter(this@Activity_Cliente_Plano_Treino_Exercicios, options)

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
                                    this@Activity_Cliente_Plano_Treino_Exercicios,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(6)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Plano_Treino_Exercicios,
                                Activity_Cliente_Definitions::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    2 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Plano_Treino_Exercicios,
                                Activity_Cliente_Marcacoes::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    3 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Plano_Treino_Exercicios,
                                Activity_Cliente_Loja_Pedidos::class.java
                            )
                        )
                        spinner.setSelection(6)
                    }
                    4 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Plano_Treino_Exercicios,
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
                                this@Activity_Cliente_Plano_Treino_Exercicios,
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

        val list_view_exercicios = findViewById<ListView>(R.id.listview_exercicios)
        list_view_exercicios.adapter = exercicio_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_fitness);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Cliente_Plano_Treino_Exercicios, Activity_Cliente_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_fitness -> {
                    startActivity(Intent(this@Activity_Cliente_Plano_Treino_Exercicios, Activity_Cliente_Planos_Treino::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Cliente_Plano_Treino_Exercicios, Activity_Cliente_Loja_Produtos::class.java))
                    finish()

                    true
                }
                R.id.nav_diet -> {
                    startActivity(Intent(this@Activity_Cliente_Plano_Treino_Exercicios, Activity_Cliente_Nutricao_Atual::class.java))
                    finish()

                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Cliente_Plano_Treino_Exercicios, Activity_Cliente_Activities::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterExercicios : BaseAdapter() {
        override fun getCount(): Int {
            return exercicios_plano_list.size
        }

        override fun getItem(position: Int): Any {
            return exercicios_plano_list[position]
        }

        override fun getItemId(position: Int): Long {
            return exercicios_plano_list[position].id_exercicio!!.toLong()
        }

        //api para usar o Duration.ZERO
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_exercicio_cliente,parent,false)

            val exercicio_nome_view = rootView.findViewById<TextView>(R.id.textViewNomeExercicios)
            exercicio_nome_view.text = exercicios_plano_list[position].nome

            val exercicio_quantity_view = rootView.findViewById<TextView>(R.id.textViewSetsExercicio)

            if (exercicios_plano_list[position].tempo == null)
            {
                var seriesRepeticoes = exercicios_plano_list[position].series.toString() + " Series / " + exercicios_plano_list[position].repeticoes.toString() + " Reps"
                exercicio_quantity_view.text = seriesRepeticoes
            }
            else exercicio_quantity_view.text = exercicios_plano_list[position].tempo.toString()

            val exercicio_image_view = rootView.findViewById<ImageView>(R.id.imageViewPlanoTreino)
            if (exercicios_plano_list[position].foto_exercicio != null && exercicios_plano_list[position].foto_exercicio != "null")
            {
                val pictureByteArray = Base64.decode(exercicios_plano_list[position].foto_exercicio, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                exercicio_image_view.setImageBitmap(bitmap)
            }

            return rootView
        }
    }
}