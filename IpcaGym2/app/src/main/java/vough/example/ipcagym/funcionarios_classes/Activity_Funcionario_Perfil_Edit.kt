package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.GinasioRequests

class Activity_Funcionario_Perfil_Edit : AppCompatActivity() {
    var funcionarioRefresh : Funcionario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_perfil_edit)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultFuncionario ->
            if(resultFuncionario?.id_funcionario != null) funcionarioRefresh = resultFuncionario

            GinasioRequests.GetByID(lifecycleScope, sessionToken, resultFuncionario?.id_ginasio){ resultGym ->
                //findViewById<ImageView>(R.id.profile_pic).setImageURI(funcionarioRefresh.foto_perfil?.toUri())
                findViewById<TextView>(R.id.funcionarioProfileNameValue).text = funcionarioRefresh?.nome
                findViewById<TextView>(R.id.funcionarioProfileEditCodeValue).text = funcionarioRefresh?.codigo.toString()
                findViewById<TextView>(R.id.funcionarioProfileEditInstitutionValue).text = resultGym?.instituicao
            }
        }

        findViewById<Button>(R.id.changePasswordButton).setOnClickListener{
            startActivity(Intent(this@Activity_Funcionario_Perfil_Edit, Activity_Funcionario_Change_Password::class.java))
        }
    }
}