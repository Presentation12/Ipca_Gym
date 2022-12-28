package vough.example.ipcagym.funcionarios_classes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import vough.example.ipcagym.R

class Activity_Funcionario_Marcacoes_Details: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_marcacoes_details)

        val id_marcacao = intent.getIntExtra("id_marcacao", -1)
        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_cliente = intent.getIntExtra("id_cliente", -1)
        val data_marcacao = intent.getStringExtra("data_marcacao")
        val estado = intent.getStringExtra("estado")
        val descricao = intent.getStringExtra("descricao")

        val buttonCancelar = findViewById<Button>(R.id.buttonCancelar)

        findViewById<TextView>(R.id.marcacaoidmarcacaovalue).text = id_marcacao.toString()
        findViewById<TextView>(R.id.marcacaoclientevalue).text = id_cliente.toString()
        findViewById<TextView>(R.id.marcacaofuncionariovalue).text = id_funcionario.toString()
        findViewById<TextView>(R.id.marcacaodatavalue).text = data_marcacao
        findViewById<TextView>(R.id.marcacaodescricaovalue).text = descricao
        findViewById<TextView>(R.id.marcacaoestadovalue).text = estado

        if(estado != "Ativo"){
            buttonCancelar.isVisible = false
        }

        buttonCancelar.setOnClickListener{
            Toast.makeText(this@Activity_Funcionario_Marcacoes_Details, "Cancelar Consultar", Toast.LENGTH_LONG).show()
        }
    }
}