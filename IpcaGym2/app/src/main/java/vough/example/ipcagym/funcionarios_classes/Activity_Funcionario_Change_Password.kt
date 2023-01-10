package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests

class Activity_Funcionario_Change_Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_change_password)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)


        findViewById<Button>(R.id.cancelChangePassword).setOnClickListener{
            finish()
            startActivity(Intent(this@Activity_Funcionario_Change_Password, Activity_Funcionario_Perfil_Edit::class.java))
        }

        findViewById<Button>(R.id.changePassword).setOnClickListener{
            var passCurrent = findViewById<EditText>(R.id.editTextCurrentPassword).text

            FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                FuncionarioRequests.loginFuncionario(lifecycleScope, it?.codigo!!.toString(), passCurrent.toString()){ response ->
                    if(response != null){
                        var passNew = findViewById<EditText>(R.id.editTextNewPassword).text
                        var passNewRepeated = findViewById<EditText>(R.id.editTextRepeatPassword).text

                        if(passNew.toString() != passNewRepeated.toString()){
                            Toast.makeText(this@Activity_Funcionario_Change_Password, "Passwords don't match", Toast.LENGTH_LONG).show()
                        }
                        else if(passNew.toString() == "" || passNewRepeated.toString() == ""){
                            Toast.makeText(this@Activity_Funcionario_Change_Password, "Fill in all the fields", Toast.LENGTH_LONG).show()
                        }
                        else{
                            FuncionarioRequests.recoverPasswordFuncionario(lifecycleScope, it?.codigo!!.toString(), passNew.toString()){ newPassResponse ->
                                if(newPassResponse != "error"){
                                    Toast.makeText(this@Activity_Funcionario_Change_Password, "Password changed successfully", Toast.LENGTH_LONG).show()
                                    finish()
                                    startActivity(Intent(this@Activity_Funcionario_Change_Password, Activity_Funcionario_Perfil_Edit::class.java))
                                }
                                else{
                                    Toast.makeText(this@Activity_Funcionario_Change_Password, "Error on changing password", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@Activity_Funcionario_Change_Password, "Incorrect password in Current Password", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}