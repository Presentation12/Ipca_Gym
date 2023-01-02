package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

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


        findViewById<Button>(R.id.button_avaliar_ginasio).setOnClickListener{
            val intent = Intent()
            intent.putExtra("id_avaliacao", 7)
            intent.putExtra("id_ginasio", 1)
            intent.putExtra("id_cliente",1)
            //TODO : Fazer condição para avaliação nao ser maior que 5
            intent.putExtra("avaliacao",findViewById<EditText>(R.id.typePlanoValue_campo_estrelas).text.toString().toInt())
            intent.putExtra("comentario", findViewById<EditText>(R.id.typePlanoValue_campo_comentario).text.toString())

            setResult(RESULT_OK, intent);
            finish()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Cliente_classificacao_activity, options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        findViewById<Button>(R.id.buttom_starts_gym).setOnClickListener{
            startActivity(Intent(this@Cliente_classificacao_activity, PaginaInicialClienteActivity::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }
    }
}