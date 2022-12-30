package vough.example.ipcagym.funcionarios_classes

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Ginasio
import vough.example.ipcagym.data_classes.Plano_Treino
import java.util.zip.Inflater

class CapacityManagementActivity : AppCompatActivity() {
    val listAux = arrayListOf<Plano_Treino>()
    val adapterAux = CapacityManagementApapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_capacity)

        val listViewAux = findViewById<ListView>(R.id.auxListView)
        listViewAux.adapter = adapterAux

        listAux.add(Plano_Treino(1,1,"aux","aux"))

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@CapacityManagementActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }
    }

    inner class CapacityManagementApapter : BaseAdapter(){
        override fun getCount(): Int {
            return listAux.size
        }

        override fun getItem(position: Int): Any {
            return listAux[position]
        }

        override fun getItemId(position: Int): Long {
            return listAux[position].id_plano_treino?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_funcionario_capacity_aux,parent,false)

            //TODO: Inserir valores calculados Stats

            return rootView
        }

    }
}
