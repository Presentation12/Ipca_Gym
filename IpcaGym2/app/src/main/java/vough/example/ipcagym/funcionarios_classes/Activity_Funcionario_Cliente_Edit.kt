package vough.example.ipcagym.funcionarios_classes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente

class Activity_Funcionario_Cliente_Edit: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_cliente_edit)

        var id_cliente = intent.getIntExtra("id_cliente", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var id_plano_nutricional = intent.getIntExtra("id_plano_nutricional", -1)
        var nome = intent.getStringExtra("nome")
        var mail = intent.getStringExtra("mail")
        var telemovel = intent.getIntExtra("telemovel",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var peso = intent.getDoubleExtra("peso",0.0)
        var altura = intent.getIntExtra("altura",-1)
        var gordura = intent.getDoubleExtra("gordura",0.0)
        var foto_perfil = intent.getStringExtra("foto_perfil")
        var estado = intent.getStringExtra("estado")

        if (foto_perfil != null)
        {
            val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)
            val imageUri: Uri = Uri.parse(foto_perfil)
            cliente_image_view.setImageURI(imageUri)
        }
        findViewById<TextView>(R.id.textViewNomeCliente).text = nome

        // butao de guardar cliente editado, e volta a página ida lista de clientes
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Funcionario_Cliente_Edit, Activity_Funcionario_Clientes_List::class.java)

            //TODO: trocar por condições de verificacao de campos preenchidos
            if (findViewById<EditText>(R.id.editTextWeight).text.isEmpty() == false)
            {
                peso = findViewById<EditText>(R.id.editTextWeight).text.toString().toDouble()
            }
            if (findViewById<EditText>(R.id.editTextHeight).text.isEmpty() == false)
            {
                altura = findViewById<EditText>(R.id.editTextHeight).text.toString().toInt()
            }
            if (findViewById<EditText>(R.id.editTextFat).text.isEmpty() == false)
            {
                gordura = findViewById<EditText>(R.id.editTextFat).text.toString().toDouble()
            }

            // TODO: Manda robjeto com novas mudanças para o patch do backend
            var clienteEditado = Cliente(id_cliente,id_ginasio,id_plano_nutricional,nome,mail,telemovel,pass_salt,pass_hash,peso,altura,gordura,foto_perfil,estado)
            startActivity(intent)
        }
    }
}