package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario

class Activity_Gerente_Funcionarios_List : AppCompatActivity() {

    var list_funcionario = arrayListOf<Funcionario>()
    var funcionarios_adapter = AdapterFuncionario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionarios_list)

        //list_funcionario.add(Cliente(1,1,1,"Joaquim","gtgtgt@tgtg.tgtg",253440656,"","",76.6,176,54.4,null,"Ativo"))

        val image_view = findViewById<ImageView>(R.id.profile_pic)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Gerente_Funcionarios_List,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        image_view.setOnClickListener {
            spinner.performClick()
        }

        //TODO: Search view
        var searchFuncionarioBar = findViewById<SearchView>(R.id.searchView)

        //TODO: Enviar lista?
        findViewById<Button>(R.id.buttonAddFuncionario).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionarios_List, Activity_Gerente_Funcionario_Add::class.java)
            startActivity(intent)
        }

        val list_view_funcionario = findViewById<ListView>(R.id.listviewFuncionarios)
        list_view_funcionario.adapter = funcionarios_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Gerente_Funcionarios_List,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Gerente_Funcionarios_List,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Gerente_Funcionarios_List,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Gerente_Funcionarios_List,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Gerente_Funcionarios_List,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterFuncionario : BaseAdapter(){
        override fun getCount(): Int {
            return list_funcionario.size
        }

        override fun getItem(position: Int): Any {
            return list_funcionario[position]
        }

        override fun getItemId(position: Int): Long {
            return list_funcionario[position].id_funcionario!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_funcionario,parent,false)

            //TODO: funcionario não tem atributo foto
            /*
            if (list_funcionario[position].foto_funcionario != null)
            {
                val funcionario_image_view = findViewById<ImageView>(R.id.profile_pic_activity)
                val imageUri: Uri = Uri.parse(list_funcionario[position].foto_funcionario)
                funcionario_image_view.setImageURI(imageUri)
            }
            */
            val nome_view = root_view.findViewById<TextView>(R.id.funcionarioName)
            nome_view.text = list_funcionario[position].nome
            val id_view = root_view.findViewById<TextView>(R.id.idFuncionario)
            id_view.text = list_funcionario[position].id_funcionario.toString()

            //Clicar num rootView abre os detalhes do cliente
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Gerente_Funcionarios_List, Activity_Gerente_Funcionario_Details::class.java)

                intent.putExtra("id_funcionario", list_funcionario[position].id_funcionario)
                intent.putExtra("id_ginasio", list_funcionario[position].id_ginasio)
                intent.putExtra("nome", list_funcionario[position].nome)
                intent.putExtra("is_admin", list_funcionario[position].is_admin)
                intent.putExtra("codigo", list_funcionario[position].codigo)
                intent.putExtra("pass_salt", list_funcionario[position].pass_salt)
                intent.putExtra("pass_hash", list_funcionario[position].pass_hash)
                intent.putExtra("estado", list_funcionario[position].estado)

                startActivity(intent)
            }

            return root_view
        }

    }
}