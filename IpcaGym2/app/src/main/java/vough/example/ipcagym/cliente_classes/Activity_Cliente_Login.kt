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
import vough.example.ipcagym.data_classes.Loja
import vough.example.ipcagym.funcionarios_classes.Activity_Funcionario_Login
import vough.example.ipcagym.requests.ClienteRequests

class Activity_Cliente_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_login)

        val loginbutton = findViewById<Button>(R.id.button)
        val mail = findViewById<EditText>(R.id.mail)
        val pass = findViewById<EditText>(R.id.password)
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val sessionRole = preferences.getString("session_role", null)

        //Caso a token de sessão estiver ativa, passar login a frente
        if (sessionToken != "" && sessionRole == "Cliente") {
            val intentStart = Intent(this@Activity_Cliente_Login, Activity_Cliente_Pagina_Inicial::class.java)
            finish()
            startActivity(intentStart)
        }

        loginbutton.setOnClickListener{
            if(mail.text.toString() == "" || pass.text.toString() == "")
                Toast.makeText(this@Activity_Cliente_Login, "Insert all fields!", Toast.LENGTH_SHORT).show()
            else{
                ClienteRequests.login(lifecycleScope, mail.text.toString(), pass.text.toString()){ result ->
                    if(result != null && result.role != "Wrong") {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", result.token)
                        editor.putString("session_role", result.role)

                        editor.apply()

                        val intentStart = Intent(this@Activity_Cliente_Login, Activity_Cliente_Pagina_Inicial::class.java)
                        finish()
                        startActivity(intentStart)
                    }
                    else if(result.role == "Wrong"){
                        Toast.makeText(this@Activity_Cliente_Login, "Wrong credentials", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this@Activity_Cliente_Login, "Wrong credentials", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.imFuncionario).setOnClickListener {
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("session_token", "")
            editor.putString("session_role", "")

            //editor.apply()
            finish()
            startActivity(Intent(this@Activity_Cliente_Login, Activity_Funcionario_Login::class.java))
        }


        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(
                Intent(this@Activity_Cliente_Login,
                    Activity_Cliente_RecoverPass::class.java)
            )
        }
    }
}