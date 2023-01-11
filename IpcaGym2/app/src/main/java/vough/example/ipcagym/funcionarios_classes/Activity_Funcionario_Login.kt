package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.Activity_Cliente_Login
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Funcionario_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_login)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val sessionRole = preferences.getString("session_role", null)

        if(sessionToken != "" && sessionRole != "Cliente"){
            //val intentStart = Intent(this@Activity_Funcionario_Login, Activity_Funcionario_Pagina_Inicial::class.java)
            val intentStart = Intent(this@Activity_Funcionario_Login, Activity_Funcionario_Capacity::class.java)
            finish()
            startActivity(intentStart)
        }

        //TODO: CERTIFICAR QUE O QUE O CODE NÃO SEJA LONG (VER TAMANHO DO CODE)
        val code = findViewById<AppCompatEditText>(R.id.mail).text
        val pass = findViewById<AppCompatEditText>(R.id.password).text

        findViewById<AppCompatButton>(R.id.button).setOnClickListener {
            if (code.toString() == "" || pass.toString() == "")
                Toast.makeText(
                    this@Activity_Funcionario_Login,
                    "Insert all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                FuncionarioRequests.loginFuncionario(lifecycleScope, code.toString(), pass.toString()) { result ->
                    if (result != null) {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", result.token)
                        editor.putString("session_role", result.role)

                        editor.apply()

                        val intentStart = Intent(this@Activity_Funcionario_Login,Activity_Funcionario_Perfil_Edit::class.java)
                        finish()
                        startActivity(intentStart)
                    } else {
                        Toast.makeText(this@Activity_Funcionario_Login, "Wrong credentials", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.imClient).setOnClickListener {
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("session_token", "")
            editor.putString("session_role", "")

            editor.apply()

            finish()
            startActivity(Intent(this@Activity_Funcionario_Login, Activity_Cliente_Login::class.java))
        }

        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(Intent(this@Activity_Funcionario_Login,Activity_Funcionario_RecoverPass::class.java))
        }
    }

}