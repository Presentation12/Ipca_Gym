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
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests
import androidx.lifecycle.lifecycleScope

class Activity_Cliente_Planos_Treino : AppCompatActivity() {

    var planos_treino_list = arrayListOf<Plano_Treino>()
    var plano_adapter = AdapterPlanosTreino()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_treino_planos)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->
            if(resultCliente != null)
            {
                if (resultCliente.foto_perfil != null)
                {
                    val pictureByteArray = Base64.decode(resultCliente.foto_perfil, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }
                else
                {
                    imageView.setImageResource(R.drawable.defaultProfilePic)
                }

                PlanoTreinoRequests.GetAllByGinasioID(lifecycleScope, sessionToken, resultCliente.id_ginasio) { resultGym ->
                    planos_treino_list = resultGym
                    plano_adapter.notifyDataSetChanged()
                }
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

        val adapter = MyAdapter(this@Activity_Cliente_Planos_Treino, options)

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
                                    this@Activity_Cliente_Planos_Treino,
                                    Activity_Cliente_Account::class.java
                                )
                            )
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@Activity_Cliente_Planos_Treino,
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
                                this@Activity_Cliente_Planos_Treino,
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

        val list_view_planos_treino = findViewById<ListView>(R.id.listviewPlanosTreino)
        list_view_planos_treino.adapter = plano_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Cliente_Planos_Treino,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Cliente_Planos_Treino,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Cliente_Planos_Treino,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Cliente_Planos_Treino,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Cliente_Planos_Treino,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterPlanosTreino : BaseAdapter(){
        override fun getCount(): Int {
            return planos_treino_list.size
        }

        override fun getItem(position: Int): Any {
            return planos_treino_list[position]
        }

        override fun getItemId(position: Int): Long {
            return planos_treino_list[position].id_plano_treino!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_plano_treino,parent,false)

            val plano_treino_view = root_view.findViewById<TextView>(R.id.textViewPlanoTreino)
            plano_treino_view.text = planos_treino_list[position].tipo

            val plano_treino_image_view = root_view.findViewById<ImageView>(R.id.imageViewPlanoTreino)
            if (planos_treino_list[position].foto_plano_treino != null)
            {
                val pictureByteArray = Base64.decode(planos_treino_list[position].foto_plano_treino, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                plano_treino_image_view.setImageBitmap(bitmap)
            }
            else
            {
                plano_treino_image_view.setImageResource(R.drawable.defaultProfilePic)
            }

            //Clicar num rootView abre o plano de treino
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Planos_Treino, Activity_Cliente_Plano_Treino_Exercicios::class.java)

                intent.putExtra("id_plano_treino", planos_treino_list[position].id_plano_treino)
                intent.putExtra("id_ginasio", planos_treino_list[position].id_ginasio)
                intent.putExtra("tipo", planos_treino_list[position].tipo)
                intent.putExtra("foto_plano_treino", planos_treino_list[position].foto_plano_treino)

                startActivity(intent)
            }

            return root_view
        }

    }
}