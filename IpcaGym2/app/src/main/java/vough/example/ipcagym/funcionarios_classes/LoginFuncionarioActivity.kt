package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.PaginaInicialClienteActivity
import vough.example.ipcagym.requests.FuncionarioRequests

class LoginFuncionarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_login)

        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        if(sessionToken != "") {
            //val intentStart = Intent(this@LoginFuncionarioActivity, PaginaInicialFuncionarioActivity::class.java)
            val intentStart = Intent(this@LoginFuncionarioActivity, CapacityManagementActivity::class.java)
            finish()
            startActivity(intentStart)
        }

        //TODO: CERTIFICAR QUE O QUE O CODE NÃO SEJA LONG (VER TAMANHO DO CODE)
        val code = findViewById<AppCompatEditText>(R.id.mail).text
        val pass = findViewById<AppCompatEditText>(R.id.password).text

        findViewById<AppCompatButton>(R.id.button).setOnClickListener {
            if (code.toString() == "" || pass.toString() == "")
                Toast.makeText(
                    this@LoginFuncionarioActivity,
                    "Insert all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                FuncionarioRequests.loginFuncionario(lifecycleScope,code.toString(),pass.toString()) { result ->
                    if (result != "User not found") {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", result)

                        editor.apply()

                        //val intentStart = Intent(this@LoginFuncionarioActivity, PaginaInicialFuncionarioActivity::class.java)
                        val intentStart = Intent(this@LoginFuncionarioActivity, FluxControlFuncionarioActivity::class.java)
                        finish()
                        startActivity(intentStart)
                    } else {
                        Toast.makeText(this@LoginFuncionarioActivity, result, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.forgetpassword).setOnClickListener {
            startActivity(Intent(this@LoginFuncionarioActivity,Activity_Funcionario_RecoverPass::class.java))
        }
    }

}