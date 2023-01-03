package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests

class LoginClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_login)

        val loginbutton = findViewById<Button>(R.id.button)
        val mail = findViewById<EditText>(R.id.mail)
        val pass = findViewById<EditText>(R.id.password)

        loginbutton.setOnClickListener{
            ClienteRequests.login(lifecycleScope, mail.text.toString(), pass.text.toString()){ result ->
                if(result != "error") startActivity(
                    Intent(this@LoginClienteActivity,
                        PaginaInicialClienteActivity::class.java)
                )
            }
        }

        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@LoginClienteActivity,
                    Activity_Cliente_RecoverPass::class.java)
            )
        }
    }
}