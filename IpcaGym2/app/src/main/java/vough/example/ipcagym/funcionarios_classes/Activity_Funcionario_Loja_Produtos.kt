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
import vough.example.ipcagym.data_classes.Loja

class Activity_Funcionario_Loja_Produtos : AppCompatActivity() {

    var produtos_list = arrayListOf<Loja>()
    var produtos_adapter = AdapterProduto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produtos_list)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Loja_Produtos,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<ListView>(R.id.buttonNewProduto).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Loja_Produto_Add::class.java)
            startActivity(intent)
        }

        val list_view_produtos = findViewById<ListView>(R.id.listviewProdutos)
        list_view_produtos.adapter = produtos_adapter

        val bottom_navigation_view = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottom_navigation_view.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produtos,"Main Menu", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_fitness -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produtos,"Treino", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_shopping -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produtos,"Loja", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_diet -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produtos,"Refeicoes", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produtos,"Atividades", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    inner class AdapterProduto : BaseAdapter(){
        override fun getCount(): Int {
            return produtos_list.size
        }

        override fun getItem(position: Int): Any {
            return produtos_list[position]
        }

        override fun getItemId(position: Int): Long {
            return produtos_list[position].id_produto!!.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root_view = layoutInflater.inflate(R.layout.row_produto,parent,false)

            val nome_produto_view = root_view.findViewById<TextView>(R.id.text_view_nome)
            nome_produto_view.text = produtos_list[position].nome

            if (produtos_list[position].foto_produto != null)
            {
                val produto_image_view = root_view.findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(produtos_list[position].foto_produto)
                produto_image_view.setImageURI(imageUri)
            }

            //Clicar num rootView abre os detalhes do produto
            root_view.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Loja_Produto_Details::class.java)

                intent.putExtra("id_produto", produtos_list[position].id_produto)
                intent.putExtra("id_ginasio", produtos_list[position].id_ginasio)
                intent.putExtra("nome", produtos_list[position].nome)
                intent.putExtra("tipo_produto", produtos_list[position].tipo_produto)
                intent.putExtra("preco", produtos_list[position].preco)
                intent.putExtra("descricao", produtos_list[position].descricao)
                intent.putExtra("estado_produto", produtos_list[position].estado_produto)
                intent.putExtra("foto_produto", produtos_list[position].foto_produto)
                intent.putExtra("quantidade_produto", produtos_list[position].quantidade_produto)

                startActivity(intent)
            }

            return root_view
        }

    }
}