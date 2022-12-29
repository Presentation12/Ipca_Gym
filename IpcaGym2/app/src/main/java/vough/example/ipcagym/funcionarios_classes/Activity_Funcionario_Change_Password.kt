package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R

class Activity_Funcionario_Change_Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_change_password)

        findViewById<Button>(R.id.cancelChangePassword).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_Change_Password, Activity_Funcionario_Perfil_Edit::class.java))
        }

        findViewById<Button>(R.id.changePassword).setOnClickListener{
            var passCurrent = findViewById<EditText>(R.id.editTextCurrentPassword).text

            //VERIFICAR SE PASSWORD INSIRA É A PASSWORD DO FUNCIONARIO EM QUESTAO

            var passNew = findViewById<EditText>(R.id.editTextNewPassword).text
            var passNewRepeated = findViewById<EditText>(R.id.editTextRepeatPassword).text

            if(passNew.toString() != passNewRepeated.toString()){
                Toast.makeText(this@Activity_Funcionario_Change_Password, "Palavras passe não coincidem", Toast.LENGTH_LONG).show()
            }
        }
    }
}