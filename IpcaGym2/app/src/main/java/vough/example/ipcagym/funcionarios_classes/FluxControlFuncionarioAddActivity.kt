package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class FluxControlFuncionarioAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_flux_control_add)

        //Botao de concluir criação
        val buttonAdd = findViewById<Button>(R.id.buttonAddActivity)
        val buttonEntry = findViewById<Button>(R.id.entryButton)
        val buttonExit = findViewById<Button>(R.id.exitButton)
        val id_cliente = findViewById<EditText>(R.id.newIDClienteActivity)
        var state = true

        buttonEntry.setOnClickListener{
            state = true
            findViewById<TextView>(R.id.textViewCurrentState).text = "Entry"
            Toast.makeText(this@FluxControlFuncionarioAddActivity,"Activity marked as entry!", Toast.LENGTH_SHORT).show()
        }

        buttonExit.setOnClickListener{
            state = false
            findViewById<TextView>(R.id.textViewCurrentState).text = "Exit"
            Toast.makeText(this@FluxControlFuncionarioAddActivity,"Activity marked as exit!", Toast.LENGTH_SHORT).show()
        }

        buttonAdd.setOnClickListener{
            val intent = Intent()

            //BUSCAR CLIENTE PELO ID INSERIDO, CASO NAO EXISTA, LANÇAR TOAST
            //CASO EXISTA MANDAR INFORMACAO

            intent.putExtra("id_cliente", id_cliente.text.toString().toInt())
            intent.putExtra("state", state)

            setResult(RESULT_OK, intent)
            finish()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        val imageView = findViewById<ImageView>(R.id.profile_pic_activity)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@FluxControlFuncionarioAddActivity,options[position], Toast.LENGTH_LONG).show()
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