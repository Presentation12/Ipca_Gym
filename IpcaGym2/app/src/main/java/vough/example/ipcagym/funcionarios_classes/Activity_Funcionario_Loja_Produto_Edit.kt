package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Loja
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.LojaRequests

class Activity_Funcionario_Loja_Produto_Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produto_edit)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        var id_produto = intent.getIntExtra("id_produto", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var tipo_produto = intent.getStringExtra("tipo_produto")
        var preco = intent.getDoubleExtra("preco",0.0)
        var descricao = intent.getStringExtra("descricao")
        var estado_produto = intent.getStringExtra("estado_produto")
        var foto_produto = intent.getStringExtra("foto_produto")
        var quantidade_produto = intent.getIntExtra("quantidade_produto",-1)

        if (foto_produto != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_produto)
            cliente_image_view.setImageURI(imageUri)
        }

        // butao de guardar produto editado, e volta para a página dos detalhes desse
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Loja_Produto_Edit, Activity_Funcionario_Loja_Produto_Details::class.java)

            if (findViewById<EditText>(R.id.editTextNomeProduto).text.isEmpty() == false)
            {
                nome = findViewById<EditText>(R.id.editTextNomeProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextTipoProduto).text.isEmpty() == false)
            {
                tipo_produto = findViewById<EditText>(R.id.editTextTipoProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextPrecoProduto).text.isEmpty() == false)
            {
                preco = findViewById<EditText>(R.id.editTextPrecoProduto).text.toString().toDouble()
            }
            if (findViewById<EditText>(R.id.editTextDescricaoProduto).text.isEmpty() == false)
            {
                descricao = findViewById<EditText>(R.id.editTextDescricaoProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextQuantidadeProduto).text.isEmpty() == false)
            {
                quantidade_produto = findViewById<EditText>(R.id.editTextQuantidadeProduto).text.toString().toInt()
            }

            var editProduto = Loja(id_produto,id_ginasio,nome,tipo_produto,preco,descricao,estado_produto,foto_produto,quantidade_produto)
            LojaRequests.Patch(lifecycleScope,sessionToken,id_produto,editProduto){ resultProdutoEditado ->
                if (resultProdutoEditado == "User not found")
                {
                    Toast.makeText(this@Activity_Funcionario_Loja_Produto_Edit, "Error: Edit fail", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }

        }
    }
}