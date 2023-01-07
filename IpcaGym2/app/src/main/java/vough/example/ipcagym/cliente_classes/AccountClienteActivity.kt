package vough.example.ipcagym.cliente_classes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Cliente
import java.time.format.DateTimeFormatter

class AccountClienteActivity : AppCompatActivity(){

        @RequiresApi(Build.VERSION_CODES.O)
        val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        //TODO: Substituir hardcode
        var cliente = Cliente(1,1,1,"Pedro Crista","pedrinho@gmail.com",933933933,"null","null",70.4,187,20.5,null,"Ativo")

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cliente_account)

            if (cliente.foto_perfil != null)
            {
                val image_view = findViewById<ImageView>(R.id.profile_pic)
                val imageUri: Uri = Uri.parse(cliente.foto_perfil)
                image_view.setImageURI(imageUri)
            }
            val name_view = findViewById<TextView>(R.id.textNome)
            name_view.text = cliente.nome
            val mail_view = findViewById<TextView>(R.id.textMail)
            mail_view.text = cliente.mail
            val peso_view = findViewById<TextView>(R.id.textViewValueWeight)
            peso_view.text = cliente.peso.toString()
            val altura_view = findViewById<TextView>(R.id.textViewValueHeight)
            altura_view.text = cliente.altura.toString()
            val gordura_view = findViewById<TextView>(R.id.textViewValueFat)
            gordura_view.text = cliente.gordura.toString()
            val media_entradas_anual_view = findViewById<TextView>(R.id.textViewValueAnnualEntries)
            val media_entradas_mensal_view = findViewById<TextView>(R.id.textViewValueMontlyEntries)
            val data_ultima_entrada_view = findViewById<TextView>(R.id.textViewDateLastWorkout)
            val tipo_treino_view = findViewById<TextView>(R.id.textViewTipoTreino)

            findViewById<Button>(R.id.buttonMarcarConsulta).setOnClickListener {
                val intent = Intent(this@AccountClienteActivity, Activity_Cliente_Marcacao_Consulta::class.java)
                startActivity(intent)
            }
            findViewById<Button>(R.id.changeDetails).setOnClickListener {
                //TODO: butao para detalhes das medidas do cliente ?Fazer?
            }
            findViewById<Button>(R.id.buttonLastWorkoutDetails).setOnClickListener {
                //TODO: butao para detalhes da ultima atividade do cliente
            }
        }
}