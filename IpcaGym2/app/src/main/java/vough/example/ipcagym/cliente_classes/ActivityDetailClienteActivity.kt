package vough.example.ipcagym.cliente_classes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade

class ActivityDetailClienteActivity : AppCompatActivity() {
    private val activityDetailed = Atividade(null,null,null,null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade_details)

        //fazer na outra activity
        val id_atividade = intent.getIntExtra("id_atividade", -1)
        val data = intent.getStringExtra("data")
        val hora_entrada = intent.getStringExtra("hora_entrada")
        val hora_saida = intent.getStringExtra("hora_saida")

        //Escrever no layout detalhado
        findViewById<TextView>(R.id.activityDetailIDValue).text = id_atividade.toString()
        findViewById<TextView>(R.id.activityDetailHourEntradaValue).text = hora_entrada
        findViewById<TextView>(R.id.activityDetailHourExitValue).text = hora_saida
        findViewById<TextView>(R.id.activityDetailDate).text = data

        //TODO: Foto perfil ter dropdown
    }
}