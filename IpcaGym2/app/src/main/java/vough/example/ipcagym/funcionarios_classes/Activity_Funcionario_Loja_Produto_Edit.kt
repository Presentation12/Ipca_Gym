package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Loja

class Activity_Funcionario_Loja_Produto_Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_loja_produto_edit)

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

            var nomeProduto : String? = null
            var tipoProduto : String? = null
            var precoProduto : Double? = null
            var descricaoProduto : String? = null
            var quantidadeProduto : Int? = null
            var fotoProduto : String? = null

            //TODO: trocar por condições de verificacao de campos preenchidos
            if (findViewById<EditText>(R.id.editTextNomeProduto).text.isEmpty() == false)
            {
                nomeProduto = findViewById<EditText>(R.id.editTextNomeProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextTipoProduto).text.isEmpty() == false)
            {
                tipoProduto = findViewById<EditText>(R.id.editTextTipoProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextPrecoProduto).text.isEmpty() == false)
            {
                precoProduto = findViewById<EditText>(R.id.editTextPrecoProduto).text.toString().toDouble()
            }
            if (findViewById<EditText>(R.id.editTextDescricaoProduto).text.isEmpty() == false)
            {
                descricaoProduto = findViewById<EditText>(R.id.editTextDescricaoProduto).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextQuantidadeProduto).text.isEmpty() == false)
            {
                quantidadeProduto = findViewById<EditText>(R.id.editTextQuantidadeProduto).text.toString().toInt()
            }


            // TODO: SUBSTITUIR OS NULOS e hardcodes DO OBJETO ABAIXO
            // objeto enviado para o backend
            var id_ginasio = 1
            var editProduto = Loja(null,id_ginasio,nomeProduto,tipoProduto,precoProduto,descricaoProduto,"Ativo",fotoProduto,quantidadeProduto)
            startActivity(intent)
        }
    }
}