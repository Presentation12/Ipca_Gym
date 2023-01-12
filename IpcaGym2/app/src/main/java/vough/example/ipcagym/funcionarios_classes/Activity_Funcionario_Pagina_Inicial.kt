package vough.example.ipcagym.funcionarios_classes
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
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

        findViewById<ImageView>(R.id.full1).isInvisible = true
        findViewById<ImageView>(R.id.full2).isInvisible = true
        findViewById<ImageView>(R.id.full3).isInvisible = true
        findViewById<ImageView>(R.id.full4).isInvisible = true
        findViewById<ImageView>(R.id.full5).isInvisible = true

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            if(it != null && it.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(it.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic_activity).setImageBitmap(bitmap)
            }
            FuncionarioRequests.GetAvaliacoesOnGym(lifecycleScope, sessionToken, it?.codigo!!){ result ->
                if(result.isNotEmpty()){
                    commentsList = result
                    adapter_comment.notifyDataSetChanged()
                    var classification_count_view = findViewById<TextView>(R.id.textView_total_count)
                    classification_count_view.text = commentsList.count { it is Classificacao }.toString()

                    var classification_average = findViewById<TextView>(R.id.textView_medium_rating)

                    val media = averagePrice(commentsList)

                    classification_average.text = media.toString()

                    if(media < 2.0F){
                        findViewById<ImageView>(R.id.full1).isInvisible = false
                    }
                    if(media >= 2.0F && media < 3.0F){
                        findViewById<ImageView>(R.id.full1).isInvisible = false
                    }
                    if(media >= 3.0F && media < 4.0F){
                        findViewById<ImageView>(R.id.full1).isInvisible = false
                    }
                    if(media >= 4.0F && media <= 5.0F){
                        findViewById<ImageView>(R.id.full1).isInvisible = false
                    }
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Pagina_Inicial, Activity_Funcionario_Flux_Control::class.java))
                    finish()

                    true
                }
                else -> false
            }
        }
    }

    fun averagePrice(commentsList: List<Classificacao>): Double {
        var total = 0.0
        for (classificacao in commentsList) {
            total += classificacao.avaliacao!!
        }

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

            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)
            rootView.findViewById<TextView>(R.id.textView_commnets_ginasio).text = commentsList[position].comentario
            rootView.findViewById<TextView>(R.id.textView_stars_rating).text = commentsList[position].avaliacao.toString()

            ClienteRequests.GetByID(lifecycleScope, sessionToken, commentsList[position].id_cliente){
                if(it != null && it.foto_perfil.toString() != "null"){
                    val pictureByteArray = Base64.decode(it.foto_perfil
                        , Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    rootView.findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
                }
            }

            return rootView
        }
    }
}


















