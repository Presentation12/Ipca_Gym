package vough.example.ipcagym.funcionarios_classes

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import vough.example.ipcagym.R

class FuncionarioManagementDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_details_funcionario)

        //fazer na outra activity
        val id_atividade = intent.getIntExtra("id_atividade", -1)
        val id_ginasio = intent.getIntExtra("id_atividade", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data = intent.getStringExtra("data")
        val data_E = intent.getStringExtra("data_E")
        val hora_entrada = intent.getStringExtra("hora_entrada")
        val hora_saida = intent.getStringExtra("hora_saida")
        val state = intent.getBooleanExtra("state", true)

        //Escrever no layout detalhado
        findViewById<TextView>(R.id.idAtividadeAtividadeDetailFuncionario).text = id_atividade.toString()
        findViewById<TextView>(R.id.idGinasioAtividadeDetailFuncionario).text = id_ginasio.toString()
        findViewById<TextView>(R.id.idClienteAtividadeDetailFuncionario).text = id_cliente.toString()
        val dataView = findViewById<TextView>(R.id.dataAtividadeDetailFuncionario)
        val hora = findViewById<TextView>(R.id.horaAtividadeDetailFuncionario)

        if(state == true){
            hora.text = hora_entrada
            hora.setTextColor(Color.GREEN)
            findViewById<TextView>(R.id.textViewHourDetailFuncionario).text = "  Entry Hour"
            dataView.text = data
        }
        else{
            hora.text = hora_saida
            hora.setTextColor(Color.RED)
            findViewById<TextView>(R.id.textViewHourDetailFuncionario).text = "  Exit Hour"
            dataView.text = data_E
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@FuncionarioManagementDetailsActivity,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        imageView.setOnClickListener {
            spinner.performClick()
        }
    }
}