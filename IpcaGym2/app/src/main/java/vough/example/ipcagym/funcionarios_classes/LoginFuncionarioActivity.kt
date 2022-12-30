package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import vough.example.ipcagym.R

class LoginFuncionarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_login)

        //TODO: TRATAR DO LOGIN FUNCIONARIO

        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@LoginFuncionarioActivity,
                    Activity_Funcionario_RecoverPass::class.java)
            )
        }

        findViewById<AppCompatButton>(R.id.button).setOnClickListener{
            Toast.makeText(this@LoginFuncionarioActivity, findViewById<AppCompatEditText>(R.id.mail).text, Toast.LENGTH_LONG).show()
        }
    }

}