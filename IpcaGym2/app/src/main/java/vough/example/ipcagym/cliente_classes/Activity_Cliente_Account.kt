package vough.example.ipcagym.cliente_classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class Activity_Cliente_Account : AppCompatActivity(){

        @RequiresApi(Build.VERSION_CODES.O)
        val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val date_formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val time_formatter = DateTimeFormatter.ofPattern("hh:mm")

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cliente_account)

            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)
            val imageView = findViewById<ImageView>(R.id.profile_pic)
            val editButton = findViewById<ImageButton>(R.id.editButton)

            ClienteRequests.GetByToken(lifecycleScope, sessionToken){ resultCliente ->

                if (resultCliente?.foto_perfil != null)
                {
                    val imageUri: Uri = Uri.parse(resultCliente.foto_perfil)
                    imageView.setImageURI(imageUri)
                }
                val name_view = findViewById<TextView>(R.id.textNome)
                name_view.text = resultCliente?.nome
                val mail_view = findViewById<TextView>(R.id.textMail)
                mail_view.text = resultCliente?.mail

                val peso_view = findViewById<TextView>(R.id.textViewValueWeight)
                if(resultCliente?.peso.toString() != "NaN")
                    peso_view.text = resultCliente?.peso.toString()
                else
                    peso_view.text = "---"

                val altura_view = findViewById<TextView>(R.id.textViewValueHeight)
                if(resultCliente?.altura != 0)
                    altura_view.text = resultCliente?.altura.toString()
                else
                    altura_view.text = "---"

                val gordura_view = findViewById<TextView>(R.id.textViewValueFat)
                if(resultCliente?.gordura.toString() != "NaN")
                    gordura_view.text = resultCliente?.gordura.toString()
                else
                    gordura_view.text = "---"

                var lastWorkout : Atividade? = null
                AtividadeRequests.GetAllByClienteID(lifecycleScope,sessionToken, resultCliente?.id_cliente){ resultAtividades ->
                    lastWorkout = resultAtividades.last()

                    //TODO: calculo
                    val mes_atual_view = findViewById<TextView>(R.id.textViewMonth)
                    mes_atual_view.text = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault()).toString()

                    val media_entradas_anual_view = findViewById<TextView>(R.id.textViewValueAnnualEntries)
                    val media_entradas_mensal_view = findViewById<TextView>(R.id.textViewValueMontlyEntries)
                    val entradas_mes_atual_view = findViewById<TextView>(R.id.textViewValueMonth)

                    var countEntradasMesAtual = 0
                    for (x in resultAtividades)
                    {
                        if (x.data_entrada?.month == LocalDate.now().month)
                        {
                            countEntradasMesAtual++
                        }
                    }

                    entradas_mes_atual_view.text = countEntradasMesAtual.toString()

                    val diffAux = Period.between(resultAtividades.first().data_entrada?.toLocalDate(), LocalDate.now())
                    val diffDaysMonths = diffAux.toTotalMonths()
                    var countMesesDesdeEntrada = diffDaysMonths / 30.44
                    var countAnosDesdeEntrada = countMesesDesdeEntrada / 365

                    if(countMesesDesdeEntrada < 1) countMesesDesdeEntrada++
                    if(countAnosDesdeEntrada < 1) countAnosDesdeEntrada++

                    media_entradas_anual_view.text = (resultAtividades.count()/countAnosDesdeEntrada).toString()
                    media_entradas_mensal_view.text = (resultAtividades.count()/countMesesDesdeEntrada).toString()

                }
                val data_ultima_entrada_view = findViewById<TextView>(R.id.textViewDateLastWorkout)
                data_ultima_entrada_view.text = lastWorkout?.data_entrada?.format(date_time_formatter)

                findViewById<Button>(R.id.buttonLastWorkoutDetails).setOnClickListener {
                    AtividadeRequests.GetByID(lifecycleScope,sessionToken,lastWorkout?.id_atividade){ resultLastActivity ->
                        val intent = Intent(this@Activity_Cliente_Account, ActivityDetailClienteActivity::class.java)

                        intent.putExtra("id_atividade", lastWorkout?.id_atividade)
                        intent.putExtra("data", lastWorkout?.data_saida?.format(date_formatter))
                        intent.putExtra("hora_entrada", lastWorkout?.data_entrada?.format(time_formatter))
                        intent.putExtra("hora_saida", lastWorkout?.data_saida?.format(time_formatter))

                        startActivity(intent)
                    }
                }

            }

            editButton.setOnClickListener{
                val intentNew = Intent(this@Activity_Cliente_Account, Activity_Cliente_Edit_Account::class.java)

                ClienteRequests.GetByToken(lifecycleScope, sessionToken) { resultCliente ->
                    intentNew.putExtra("id_cliente", resultCliente?.id_cliente!!)

                    intentNew.putExtra("id_ginasio", resultCliente?.id_ginasio!!)
                    intentNew.putExtra("id_plano_nutricional", resultCliente?.id_plano_nutricional!!)
                    intentNew.putExtra("nome", resultCliente?.nome!!)
                    intentNew.putExtra("mail", resultCliente?.mail!!)
                    intentNew.putExtra("pass_hash", resultCliente?.pass_hash!!)
                    intentNew.putExtra("pass_salt", resultCliente?.pass_salt!!)
                    intentNew.putExtra("foto_perfil", resultCliente?.foto_perfil!!)
                    intentNew.putExtra("altura", resultCliente?.altura!!)
                    intentNew.putExtra("estado", resultCliente?.estado!!)
                    intentNew.putExtra("telemovel", resultCliente?.telemovel!!)
                    intentNew.putExtra("gordura", resultCliente?.gordura!!)
                    intentNew.putExtra("peso", resultCliente?.peso!!)

                    startActivity(intentNew)
                }
            }

            findViewById<Button>(R.id.buttonMarcarConsulta).setOnClickListener {
                val intent = Intent(this@Activity_Cliente_Account, Activity_Cliente_Marcacao_Consulta::class.java)
                startActivity(intent)
            }
        }
}