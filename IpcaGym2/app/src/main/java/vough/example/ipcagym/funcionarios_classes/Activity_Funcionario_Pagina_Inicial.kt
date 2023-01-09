package vough.example.ipcagym.funcionarios_classes
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.ClassificacaoRequests
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests
import java.time.format.DateTimeFormatter
import kotlin.math.log

class Activity_Funcionario_Pagina_Inicial: AppCompatActivity() {

    var commentsList = arrayListOf<Classificacao>()
    var adapter_comment = commentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_pagina_inicial)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null)
                findViewById<TextView>(R.id.textView_funcionario_nome).text = result.nome
        }

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            FuncionarioRequests.GetAvaliacoesOnGym(lifecycleScope, sessionToken, it?.codigo!!){ result ->
                if(result.isNotEmpty()){
                    commentsList = result
                    adapter_comment.notifyDataSetChanged()
                    var classification_count_view = findViewById<TextView>(R.id.textView_total_count)
                    classification_count_view.text = commentsList.count { it is Classificacao }.toString()

                    var classification_average = findViewById<TextView>(R.id.textView_medium_rating)
                    classification_average.text = averagePrice(commentsList).toString()
                }
            }
        }

        val listCommentsView = findViewById<ListView>(R.id.comentarios_List_View)
        listCommentsView.adapter = adapter_comment


        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
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
    }

    fun averagePrice(commentsList: List<Classificacao>): Double {
        var total = 0.0
        for (classificacao in commentsList) {
            total += classificacao.avaliacao!!
        }
        Log.d("myTag",total.toString())
        return total / commentsList.size
    }

    inner class commentAdapter: BaseAdapter(){
        override fun getCount(): Int {
            return commentsList.size
        }

        override fun getItem(position: Int): Any {
            return commentsList[position]
        }

        override fun getItemId(position: Int): Long {
            return commentsList[position].id_avaliacao?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_comments,parent,false)

            rootView.findViewById<TextView>(R.id.textView_commnets_ginasio).text = commentsList[position].comentario
            rootView.findViewById<TextView>(R.id.textView_stars_rating).text = commentsList[position].avaliacao.toString()
            return rootView
        }
    }
}


















