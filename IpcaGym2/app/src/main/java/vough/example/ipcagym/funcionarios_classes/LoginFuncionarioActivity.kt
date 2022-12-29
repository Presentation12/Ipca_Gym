package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.Activity_Cliente_RecoverPass

class LoginFuncionarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_login)

        //TODO: TRATAR DO LOGIN FUNCIONARIO

        findViewById<TextView>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@LoginFuncionarioActivity,
                    Activity_Funcionario_RecoverPass::class.java)
            )
        }
    }

}