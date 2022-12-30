package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Planos_Treino_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_treino_add)

        findViewById<Button>(R.id.addNewPlanButton).setOnClickListener{
            val intent = Intent()

            intent.putExtra("id_plano_treino", 1)
            intent.putExtra("id_ginasio", 1)
            intent.putExtra("tipo", findViewById<TextView>(R.id.typePlanoValue).text)
            intent.putExtra("foto_plano_treino", 1)

            setResult(RESULT_OK, intent);
            finish()
        }
    }
}