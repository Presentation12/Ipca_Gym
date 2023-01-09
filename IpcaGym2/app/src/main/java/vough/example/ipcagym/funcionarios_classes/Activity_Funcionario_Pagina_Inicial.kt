package vough.example.ipcagym.funcionarios_classes
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Funcionario
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Pagina_Inicial: AppCompatActivity() {

    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var adapter_comment = commentAdapter()
    var funcionario = Funcionario(4,2,"Frederico Botelho",null,126789,"null","null","ativo", "photo")
    val listComments = arrayListOf<Classificacao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_pagina_inicial)

        listComments.add(Classificacao(7, 1, 1, 5, "Adorei", null))
        listComments.add(Classificacao(8, 2, 1, 1, "Pessimo", null))
        listComments.add(Classificacao(9, 2, 3, 3, "meio bom", null))
        listComments.add(Classificacao(1, 3, 7, 4, "caca", null))
        listComments.add(Classificacao(7, 1, 3, 9, "teste1", null))
        listComments.add(Classificacao(8, 2, 4, 1, "teste2", null))

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner2)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Pagina_Inicial, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        imageView.setOnClickListener{ spinner.performClick() }

        val listComentariosnView = findViewById<ListView>(R.id.comentarios_List_View)
        listComentariosnView.adapter = adapter_comment

        val name_view = findViewById<TextView>(R.id.textView5)
        name_view.text = funcionario.nome

        var classification_average_view = findViewById<TextView>(R.id.textView_medium_rating)
        classification_average_view.text = averagePrice(listComments).toString()


        var classification_count_view = findViewById<TextView>(R.id.textView_total_count)
        classification_count_view.text = listComments.count { it is Classificacao }.toString()
    }

    fun averagePrice(listComments: List<Classificacao>): Double {
        var total = 0.0
        for (classificacao in listComments) {
            total += classificacao.avaliacao!!
        }
        return total / listComments.size
    }

    inner class commentAdapter: BaseAdapter(){
        override fun getCount(): Int {
            return listComments.size
        }

        override fun getItem(position: Int): Any {
            return listComments[position]
        }

        override fun getItemId(position: Int): Long {
            return listComments[position].id_avaliacao?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_comments,parent,false)

            rootView.findViewById<TextView>(R.id.textView_commnets_ginasio).text = listComments[position].comentario

            rootView.findViewById<TextView>(R.id.textView_stars_rating).text = listComments[position].avaliacao.toString()

            return rootView
        }
    }
}


















