package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Clientes_List
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Planos_Treino
import vough.example.ipcagym.funcionarios_classes.PaginaInicialFuncionarioActivity

class Cliente_classificacao_activity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_avaliar)

        val image_view = findViewById<ImageView>(R.id.profile_pic_activity)
        val spinner = findViewById<Spinner>(R.id.spinner_avalicao)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Cliente_classificacao_activity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        findViewById<Button>(R.id.buttom_starts_gym).setOnClickListener() {
            startActivity(Intent(this@Cliente_classificacao_activity, AccountClienteActivity::class.java))
        }


        findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener {
            val intent = Intent(this@Cliente_classificacao_activity, PaginaInicialFuncionarioActivity::class.java)

            var comentario : String = ""
            var estrelas : Int = 0

            if (findViewById<EditText>(R.id.campo_comentario).text.isEmpty() == false)
            {
                comentario = findViewById<EditText>(R.id.campo_comentario).text.toString()
            }
            if (findViewById<EditText>(R.id.campo_rating).text.isEmpty() == false)
            {
                estrelas = findViewById<EditText>(R.id.campo_rating).text.toString().toInt()
            }

            //Todo acrescentar condições de campos nulos

            // TODO: SUBSTITUIR OS NULOS e hardcodes DO OBJETO ABAIXO
            // objeto enviado para o backend
            var newAvaliation = Classificacao(8,1,1,estrelas,comentario,null)

            startActivity(intent)
        }
    }
}