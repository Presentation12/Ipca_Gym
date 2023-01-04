package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.LoginClienteActivity
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Funcionario_RecoverPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_recover_password)

        findViewById<Button>(R.id.submitrecoverpasswordFuncionario).setOnClickListener{
            var passNew = findViewById<EditText>(R.id.newpassFuncionario).text
            var passNewRepeated = findViewById<EditText>(R.id.newpassrepeatFuncionario).text
            //TODO: CERTIFICAR QUE O QUE O emailRecover NÃO SEJA LONG (VER TAMANHO DO CODE)
            var emailRecover = findViewById<TextView>(R.id.coderecoverFuncionario).text

            if(passNew.toString() != passNewRepeated.toString()){
                Toast.makeText(this@Activity_Funcionario_RecoverPass, "Passwords don't match!", Toast.LENGTH_LONG).show()
            }
            else if(passNew.toString() == passNewRepeated.toString()){
                if(passNew.toString() == "" || passNewRepeated.toString() == "" || emailRecover.toString() == "")
                    Toast.makeText(this@Activity_Funcionario_RecoverPass, "Insert all fields!", Toast.LENGTH_LONG).show()
                else
                    FuncionarioRequests.recoverPasswordFuncionario(lifecycleScope,emailRecover.toString() , passNew.toString()){ result ->
                        if(result != "error") {
                            Toast.makeText(this@Activity_Funcionario_RecoverPass, "Password changed!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Activity_Funcionario_RecoverPass,LoginFuncionarioActivity::class.java))
                        }
                        else Toast.makeText(this@Activity_Funcionario_RecoverPass, "User not found", Toast.LENGTH_LONG).show()
                    }
            }
        }

        findViewById<Button>(R.id.cancelpasswordFuncionario).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_RecoverPass,LoginFuncionarioActivity::class.java))
        }
    }
}