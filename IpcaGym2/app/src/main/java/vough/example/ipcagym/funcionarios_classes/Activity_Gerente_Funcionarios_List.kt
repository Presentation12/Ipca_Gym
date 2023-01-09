package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Gerente_Funcionarios_List : AppCompatActivity() {

    var list_funcionario = arrayListOf<Funcionario>()
    var funcionarios_adapter = AdapterFuncionario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionarios_list)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)
        var listFuncAux = arrayListOf<Funcionario>()

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null){
                if (resultGerente.foto_funcionario != null)
                {
                    val imageUri: Uri = Uri.parse(resultGerente.foto_funcionario)
                    imageView.setImageURI(imageUri)
                }

                FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, resultGerente.id_ginasio) { resultFuncionarios ->
                    if(resultFuncionarios.isNotEmpty()){
                        list_funcionario = resultFuncionarios
                        listFuncAux = resultFuncionarios

                        funcionarios_adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Gerente_Funcionarios_List, options)
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
                            startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Login::class.java))
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

        val list_view_funcionario = findViewById<ListView>(R.id.listviewFuncionarios)
        list_view_funcionario.adapter = funcionarios_adapter

        //TODO: Search view, quando se carrega fora do teclado, crasha
        val searchFuncionarioBar = findViewById<SearchView>(R.id.searchView)
        searchFuncionarioBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                var searchResults = ArrayList<Funcionario>()

                for (func in list_funcionario) {
                    if (func.nome?.contains(query ?: "") == true) {
                        searchResults.add(func)
                    }
                }

                if(query.toString() == ""){
                    searchResults = listFuncAux
                }

                list_funcionario = searchResults
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                var searchResults = ArrayList<Funcionario>()

                for (func in list_funcionario) {
                    if (func.nome?.contains(newText ?: "") == true) {
                        searchResults.add(func)
                    }
                }

                if(newText.toString() == ""){
                    searchResults = listFuncAux
                }

                list_funcionario = searchResults
                return true
            }
        })

        findViewById<Button>(R.id.buttonAddFuncionario).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionarios_List, Activity_Gerente_Funcionario_Add::class.java)
            startActivity(intent)
        }

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

            if (list_funcionario[position].foto_funcionario != null)
            {
                val funcionario_image_view = findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(list_funcionario[position].foto_funcionario)
                funcionario_image_view.setImageURI(imageUri)
            }

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
                intent.putExtra("foto_funcionario", list_funcionario[position].foto_funcionario)

                startActivity(intent)
            }

            return root_view
        }

    }
}