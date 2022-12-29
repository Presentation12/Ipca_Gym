package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import vough.example.ipcagym.R
import vough.example.ipcagym.funcionarios_classes.LoginFuncionarioActivity
import vough.example.ipcagym.requests.ClienteRequests

class LoginClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_login)

        val loginbutton = findViewById<Button>(R.id.button)
        val mail = findViewById<EditText>(R.id.mail)
        val pass = findViewById<EditText>(R.id.password)

        loginbutton.setOnClickListener{
            ClienteRequests.login(GlobalScope, mail.text.toString(), pass.text.toString()){ result ->
                if(result != ""){
                    Toast.makeText(this@LoginClienteActivity, result, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@LoginClienteActivity, "erro", Toast.LENGTH_SHORT).show()
                }

            }


            //startActivity(Intent(this@LoginClienteActivity, PaginaInicialClienteActivity::class.java))
        }

        findViewById<TextView>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@LoginClienteActivity,
                    Activity_Cliente_RecoverPass::class.java)
            )
        }
    }
}