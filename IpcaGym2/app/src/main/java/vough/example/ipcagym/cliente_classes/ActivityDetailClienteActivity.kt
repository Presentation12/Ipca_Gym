package vough.example.ipcagym.cliente_classes

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import vough.example.ipcagym.R

class ActivityDetailClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_atividade_details)

        //fazer na outra activity
        val id_atividade = intent.getIntExtra("id_atividade", -1)
        val data = intent.getStringExtra("data")
        val hora_entrada = intent.getStringExtra("hora_entrada")
        val hora_saida = intent.getStringExtra("hora_saida")

        //Escrever no layout detalhado
        findViewById<TextView>(R.id.activityDetailIDValue).text = id_atividade.toString()
        findViewById<TextView>(R.id.activityDetailHourEntradaValue).text = hora_entrada
        findViewById<TextView>(R.id.activityDetailDate).text = data

        if(hora_saida != null){
            findViewById<TextView>(R.id.activityDetailHourExitValue).text = hora_saida
        }
        else{
            findViewById<TextView>(R.id.activityDetailHourExitValue).text = "Currently on the gym"
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@ActivityDetailClienteActivity,options[position], Toast.LENGTH_LONG).show()
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