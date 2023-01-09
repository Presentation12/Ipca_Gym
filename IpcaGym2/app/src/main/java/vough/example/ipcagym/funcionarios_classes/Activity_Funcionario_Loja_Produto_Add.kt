package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Loja

class Activity_Funcionario_Loja_Produto_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produto_add)

        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)

        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Loja_Produto_Add, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Add, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Add, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produto_Add, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }
        imageView.setOnClickListener { spinner.performClick() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        // butao de adicionar produto novo, e volta a página ida lista de produtos
        findViewById<Button>(R.id.buttonCriar).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Loja_Produto_Add, Activity_Funcionario_Loja_Produtos::class.java)

            var nomeProduto : String = ""
            var tipoProduto : String = ""
            var precoProduto : Double = 0.0
            var descricaoProduto : String = ""
            var quantidadeProduto : Int = 0
            var fotoProduto : String = ""

            //TODO: trocar por condições de verificacao de campos preenchidos
            if (findViewById<EditText>(R.id.editTextNomeProduto).text.isEmpty() == false)
            {
                nomeProduto = findViewById<EditText>(R.id.editTextNomeProduto).text.toString()
            }
            else Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Error: Fields missing",Toast.LENGTH_LONG)
            if (findViewById<EditText>(R.id.editTextTipoProduto).text.isEmpty() == false)
            {
                tipoProduto = findViewById<EditText>(R.id.editTextTipoProduto).text.toString()
            }
            else Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Error: Fields missing",Toast.LENGTH_LONG)
            if (findViewById<EditText>(R.id.editTextPrecoProduto).text.isEmpty() == false)
            {
                precoProduto = findViewById<EditText>(R.id.editTextPrecoProduto).text.toString().toDouble()
            }
            else Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Error: Fields missing",Toast.LENGTH_LONG)
            if (findViewById<EditText>(R.id.editTextDescricaoProduto).text.isEmpty() == false)
            {
                descricaoProduto = findViewById<EditText>(R.id.editTextDescricaoProduto).text.toString()
            }
            else Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Error: Fields missing",Toast.LENGTH_LONG)
            if (findViewById<EditText>(R.id.editTextQuantidadeProduto).text.isEmpty() == false)
            {
                quantidadeProduto = findViewById<EditText>(R.id.editTextQuantidadeProduto).text.toString().toInt()
            }
            else Toast.makeText(this@Activity_Funcionario_Loja_Produto_Add,"Error: Fields missing",Toast.LENGTH_LONG)

            // TODO: SUBSTITUIR OS NULOS e hardcodes DO OBJETO ABAIXO
            // objeto enviado para o backend
            var id_ginasio = 1
            var newProduto = Loja(null,id_ginasio,nomeProduto,tipoProduto,precoProduto,descricaoProduto,"Ativo",fotoProduto,quantidadeProduto)

            startActivity(intent)
        }
    }
}