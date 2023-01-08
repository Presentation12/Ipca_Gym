package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import java.time.LocalDate
import java.time.Period
import java.time.format.TextStyle
import java.util.*

class Activity_Cliente_Edit_Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_edit_account)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        var id_cliente = intent.getIntExtra("id_cliente", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var id_plano_nutricional : Int? = intent.getIntExtra("id_plano_nutricional", 0)
        var nome = intent.getStringExtra("nome")
        var mail = intent.getStringExtra("mail")
        var telemovel = intent.getIntExtra("telemovel",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var peso : Double? = intent.getDoubleExtra("peso",0.0)
        var altura : Int?  = intent.getIntExtra("altura",-1)
        var gordura : Double?  = intent.getDoubleExtra("gordura",0.0)
        var foto_perfil = intent.getStringExtra("foto_perfil")
        var estado = intent.getStringExtra("estado")

        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }
        findViewById<TextView>(R.id.editTextNomeCliente).hint = nome
        findViewById<TextView>(R.id.editTextMailCliente).hint = mail
        findViewById<TextView>(R.id.editTextContactoCliente).hint = telemovel.toString()

        var passwordEditText = findViewById<EditText>(R.id.editTextPasswordCliente)
        var showHidePassword = findViewById<ImageButton>(R.id.imageButtonSeePassword)

        showHidePassword.setOnClickListener {
            // Verifica se a senha está sendo exibida
            if (passwordEditText.inputType == InputType.TYPE_CLASS_TEXT) {
                // Muda para o tipo de entrada que esconde a senha
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                // Muda para o tipo de entrada que mostra a senha
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        var passwordEditText2 = findViewById<EditText>(R.id.editTextPasswordCliente2)
        var showHidePassword2 = findViewById<ImageButton>(R.id.imageButtonSeePassword2)

        showHidePassword2.setOnClickListener {
            // Verifica se a senha está sendo exibida
            if (passwordEditText2.inputType == InputType.TYPE_CLASS_TEXT) {
                // Muda para o tipo de entrada que esconde a senha
                passwordEditText2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                // Muda para o tipo de entrada que mostra a senha
                passwordEditText2.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        // butao de guardar cliente editado, e volta a página ida lista de clientes
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Edit_Account, Activity_Cliente_Account::class.java)

            //TODO: edit foto
            /*
            if (foto_perfil != null)
            {
                val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(foto_perfil)
                cliente_image_view.setImageURI(imageUri)
            }
            */

            if (findViewById<EditText>(R.id.editTextNomeCliente).text.isEmpty() == false)
            {
                nome = findViewById<EditText>(R.id.editTextNomeCliente).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextMailCliente).text.isEmpty() == false)
            {
                mail = findViewById<EditText>(R.id.editTextMailCliente).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextContactoCliente).text.isEmpty() == false)
            {
                telemovel = findViewById<EditText>(R.id.editTextContactoCliente).text.toString().toInt()
            }

            var newPass : String? = null
            if (findViewById<EditText>(R.id.editTextPasswordCliente).text.isEmpty() == false && findViewById<EditText>(R.id.editTextPasswordCliente).text.toString() == findViewById<EditText>(R.id.editTextPasswordCliente2).text.toString())
            {
                newPass = findViewById<EditText>(R.id.editTextPasswordCliente).text.toString()
            }

            if(id_plano_nutricional == 0) id_plano_nutricional = null
            if(peso.toString() == "NaN") peso = null
            if(altura == 0) altura = null
            if(gordura.toString() == "NaN") gordura = null

            var editCliente = Cliente(id_cliente,id_ginasio,id_plano_nutricional,nome,mail,telemovel,pass_salt,pass_hash,peso,altura,gordura,foto_perfil,estado)
            ClienteRequests.Patch(lifecycleScope,sessionToken,id_cliente, editCliente) { resultEditcliente ->
                if (resultEditcliente == "User not found")
                {
                    Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on editting client account", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
            if(newPass != null)
            {
                ClienteRequests.recoverPasswordCliente(lifecycleScope, mail, newPass) { resultNewPassCliente ->
                    if (resultNewPassCliente == "error")
                    {
                        Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on change client password", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    }
}