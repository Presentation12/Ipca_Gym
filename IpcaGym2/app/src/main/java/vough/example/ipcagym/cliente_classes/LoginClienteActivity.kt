package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        //Caso a token de sessão estiver ativa, passar login a frente
        /*if (sessionToken != "") {
            val intentStart = Intent(this@LoginClienteActivity, ActivitiesClienteActivity::class.java)
            finish()
            startActivity(intentStart)
        }*/

        loginbutton.setOnClickListener{
            if(mail.text.toString() == "" || pass.text.toString() == "")
                Toast.makeText(this@LoginClienteActivity, "Insert all fields!", Toast.LENGTH_SHORT).show()
            else{
                ClienteRequests.login(lifecycleScope, mail.text.toString(), pass.text.toString()){ result ->
                    if(result != "User not found") {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", result)

                        editor.apply()

                        //val intentStart = Intent(this@LoginClienteActivity,PaginaInicialClienteActivity::class.java)
                        val intentStart = Intent(this@LoginClienteActivity, ActivitiesClienteActivity::class.java)
                        finish()
                        startActivity(intentStart)
                    }
                    else {
                        Toast.makeText(this@LoginClienteActivity, result, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.imFuncionario).setOnClickListener {
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("session_token", "")
            finish()
            startActivity(Intent(this@LoginClienteActivity, LoginFuncionarioActivity::class.java))
        }


        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@LoginClienteActivity,
                    Activity_Cliente_RecoverPass::class.java)
            )
        }
    }
}