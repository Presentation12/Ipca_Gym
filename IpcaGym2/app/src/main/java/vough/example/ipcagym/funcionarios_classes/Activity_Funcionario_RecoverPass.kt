package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_RecoverPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        findViewById<Button>(R.id.submitrecoverpassword).setOnClickListener{
            var passNew = findViewById<EditText>(R.id.newpass).text
            var passNewRepeated = findViewById<EditText>(R.id.newpassrepeat).text

            if(passNew.toString() != passNewRepeated.toString()){
                Toast.makeText(this@Activity_Funcionario_RecoverPass, "Palavras passe não coincidem", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.cancelpassword).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_RecoverPass,LoginFuncionarioActivity::class.java))
        }
    }
}