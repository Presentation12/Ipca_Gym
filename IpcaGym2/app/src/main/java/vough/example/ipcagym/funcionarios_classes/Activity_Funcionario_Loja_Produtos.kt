package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Loja
import vough.example.ipcagym.requests.ClienteRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.LojaRequests

class Activity_Funcionario_Loja_Produtos : AppCompatActivity() {

    var produtos_list = arrayListOf<Loja>()
    var produtos_adapter = AdapterProduto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produtos_list)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken) { resultFuncionario ->
            if(resultFuncionario != null)
            {
                if (resultFuncionario.foto_funcionario.toString() != "null")
                {
                    val pictureByteArray = Base64.decode(resultFuncionario.foto_funcionario, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                    findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
                }

                LojaRequests.GetAllByGinasioID(lifecycleScope,sessionToken,resultFuncionario.id_ginasio){ resultProdutosGinasio ->
                    if(resultProdutosGinasio.isNotEmpty()){
                        produtos_list = resultProdutosGinasio
                        produtos_adapter.notifyDataSetChanged()
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

        val adapter = MyAdapter(this@Activity_Funcionario_Loja_Produtos, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Login::class.java))
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

        findViewById<Button>(R.id.buttonNewProduto).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Loja_Produto_Add::class.java)
            startActivity(intent)
        }

        val list_view_produtos = findViewById<ListView>(R.id.listviewProdutos)
        list_view_produtos.adapter = produtos_adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setSelectedItemId(R.id.nav_shopping);
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Loja_Produtos, Activity_Funcionario_Flux_Control::class.java))
                    finish()

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

            val preco_produto_view = root_view.findViewById<TextView>(R.id.text_view_preco)
            preco_produto_view.text = String.format("%.2f", produtos_list[position].preco) + " €"

            if (produtos_list[position].foto_produto.toString() != "null")
            {
                val pictureByteArray = Base64.decode(produtos_list[position].foto_produto, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                root_view.findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
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
                intent.putExtra("quantidade_produto", produtos_list[position].quantidade_produto)

                startActivity(intent)
            }

            return root_view
        }

    }
}