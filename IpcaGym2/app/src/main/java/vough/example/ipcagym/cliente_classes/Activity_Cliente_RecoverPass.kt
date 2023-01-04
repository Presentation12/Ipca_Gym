package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.ClienteRequests

class Activity_Cliente_RecoverPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_recover_password)

        findViewById<Button>(R.id.submitrecoverpassword).setOnClickListener{
            var passNew = findViewById<EditText>(R.id.newpass).text
            var passNewRepeated = findViewById<EditText>(R.id.newpassrepeat).text
            var emailRecover = findViewById<TextView>(R.id.emailrecover).text

            if(passNew.toString() != passNewRepeated.toString()){
                Toast.makeText(this@Activity_Cliente_RecoverPass, "Passwords don't match!", Toast.LENGTH_LONG).show()
            }
            else if(passNew.toString() == passNewRepeated.toString()){
                if(passNew.toString() == "" || passNewRepeated.toString() == "" || emailRecover.toString() == "")
                    Toast.makeText(this@Activity_Cliente_RecoverPass, "Insert all fields!", Toast.LENGTH_LONG).show()
                else
                    ClienteRequests.recoverPasswordCliente(lifecycleScope, emailRecover.toString(), passNew.toString()){ result ->
                        if(result != "error") {
                            Toast.makeText(this@Activity_Cliente_RecoverPass, "Password changed!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Activity_Cliente_RecoverPass,LoginClienteActivity::class.java))
                        }
                        else Toast.makeText(this@Activity_Cliente_RecoverPass, "User not found", Toast.LENGTH_LONG).show()
                    }
            }
        }

        findViewById<Button>(R.id.cancelpassword).setOnClickListener{
            startActivity(Intent(this@Activity_Cliente_RecoverPass,LoginClienteActivity::class.java))
        }
    }
}