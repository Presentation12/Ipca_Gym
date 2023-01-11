package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
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
        var ListTotal: ArrayList<Funcionario>

        ListTotal = list_funcionario

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null){
                if (resultGerente.foto_funcionario  != null && resultGerente.foto_funcionario != "null")
                {
                    val pictureByteArray = Base64.decode(resultGerente.foto_funcionario, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    imageView.setImageBitmap(bitmap)
                }

                FuncionarioRequests.GetAllByGym(lifecycleScope, sessionToken, resultGerente.id_ginasio) { resultFuncionarios ->
                    if(resultFuncionarios.isNotEmpty()){
                        list_funcionario = resultFuncionarios
                        ListTotal = resultFuncionarios

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

        var listAux = list_funcionario
        findViewById<EditText>(R.id.editText).addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var searchResults = ArrayList<Funcionario>()
                for (func in ListTotal) {
                    if (func.nome?.lowercase()?.contains(s.toString().lowercase()) == true) {
                        searchResults.add(func)
                    }
                }

                if(count == 0 && before > 0){
                    searchResults = ArrayList<Funcionario>()
                    for (func in ListTotal) {
                        if (func.nome?.lowercase()?.contains(s.toString().lowercase()) == true) {
                            searchResults.add(func)
                        }
                    }
                }

                list_funcionario = searchResults
                listAux = list_funcionario
                funcionarios_adapter.notifyDataSetChanged()
            }

            // overrides vazios necessário para implementar TextWatcher
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Switch>(R.id.switch1).setOnClickListener{

            if(findViewById<Switch>(R.id.switch1).isChecked)
            {
                var adminResults = ArrayList<Funcionario>()
                for (func in list_funcionario) {
                    if (func.is_admin == true) {
                        adminResults.add(func)
                    }
                }

                list_funcionario = adminResults
                funcionarios_adapter.notifyDataSetChanged()
            }
            else
            {
                list_funcionario = listAux
                funcionarios_adapter.notifyDataSetChanged()
            }
        }

        findViewById<ImageButton>(R.id.buttonAddFuncionario).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionarios_List, Activity_Gerente_Funcionario_Add::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionarios_List, Activity_Funcionario_Flux_Control::class.java))
                    finish()
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

            val funcionario_image_view = findViewById<ImageView>(R.id.profile_pic)
            if (list_funcionario[position].foto_funcionario  != null && list_funcionario[position].foto_funcionario != "null")
            {
                val pictureByteArray = Base64.decode(list_funcionario[position].foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                funcionario_image_view.setImageBitmap(bitmap)
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