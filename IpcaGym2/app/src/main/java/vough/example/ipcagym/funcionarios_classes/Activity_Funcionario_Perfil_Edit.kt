package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Perfil_Edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_perfil_edit)

        findViewById<Button>(R.id.changePasswordButton).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_Perfil_Edit, Activity_Funcionario_Change_Password::class.java))
        }
    }
}