package vough.example.ipcagym.funcionarios_classes
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Plano_Nutricional
import java.time.format.DateTimeFormatter

class PaginaInicialFuncionarioActivity: AppCompatActivity() {

    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var adapter_comment = commentAdapter()
    var funcionario = Funcionario(4,2,"Frederico Botelho",null,126789,"null","null","ativo")
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val spinner = findViewById<Spinner>(R.id.spinner2)
        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

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


















