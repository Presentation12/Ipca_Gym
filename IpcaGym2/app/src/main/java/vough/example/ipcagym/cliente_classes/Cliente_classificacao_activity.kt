package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Planos_Treino

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

        // butao de adicionar avaliação nova, e volta para o perfil do cliente
        findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener {
            val intent = Intent(this@Cliente_classificacao_activity,AccountClienteActivity::class.java)

            //TODO: trocar por condições de verificacao de campos preenchidos
            var rating : Int
            var comentary : String

            if (findViewById<EditText>(R.id.campo_comentario).text.isEmpty() == false )
            {
                comentary = findViewById<EditText>(R.id.campo_comentario).text.toString()
            }
            else comentary = "Default"

            if (findViewById<EditText>(R.id.campo_rating).text.isEmpty() == false)
            {
                rating = findViewById<EditText>(R.id.campo_rating).text.toString().toInt()
            }
            else rating = 0

            // TODO: Manda objeto com novas mudanças para o patch do backend e trocar os nulos
            var classificacao = Classificacao(null,null,null,rating,comentary,null)
            startActivity(intent)
        }
    }
}