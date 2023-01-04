package vough.example.ipcagym.cliente_classes

import android.content.Context
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
            if(mail.text.toString() == "" || pass.text.toString() == "")
            {
                Toast.makeText(this@LoginClienteActivity, "Insert all fields!", Toast.LENGTH_SHORT).show()
            }
            else{
                ClienteRequests.login(lifecycleScope, mail.text.toString(), pass.text.toString()){ result ->
                    if(result != "error") {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", result)

                        editor.apply()
                        Toast.makeText(this@LoginClienteActivity, preferences.getString("session_token", null), Toast.LENGTH_LONG).show()

                        startActivity(Intent(this@LoginClienteActivity,PaginaInicialClienteActivity::class.java))
                    }
                    else {
                        Toast.makeText(this@LoginClienteActivity, result, Toast.LENGTH_LONG).show()
                    }
                }
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