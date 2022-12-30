package vough.example.ipcagym.funcionarios_classes

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Plano_Treino_Exercicio_Add : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_treino_exercicio_add)

        //Guardar variaveis

        findViewById<Button>(R.id.addNewPlanButton).setOnClickListener{

        }
    }
}