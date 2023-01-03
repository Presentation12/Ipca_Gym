package vough.example.ipcagym.funcionarios_classes
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Classificacao
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.data_classes.Plano_Nutricional
import java.time.format.DateTimeFormatter

class PaginaInicialFuncionarioActivity: AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    val date_time_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val adapter_nutri = CommentAdapter()
    var newPlanReceiver : ActivityResultLauncher<Intent>? = null
    var funcionario = Funcionario(4,2,"Frederico Botelho",null,126789,"null","null","ativo")
    var classificacao = Classificacao(4,2,1,5,"Perfeito",null)
    val listComments = arrayListOf<Classificacao>()
    val classifications_avaliation = classificacao.avaliacao?.let { intArrayOf(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_pagina_inicial)

        listComments.add(Classificacao(7,1,1,5,"Adorei",null))
        listComments.add(Classificacao(8,2,1,1,"Pessimo",null))
        listComments.add(Classificacao(9,2,3,3,"meio bom",null))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        val spinner = findViewById<Spinner>(R.id.spinner2)
        val options = arrayOf("Conta", "Definições", "Sair")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listComentariosnView = findViewById<ListView>(R.id.comentarios_List_View)
        listComentariosnView.adapter = adapter_nutri

        val name_view = findViewById<TextView>(R.id.textView5)
        name_view.text = funcionario.nome

        val classification_view = findViewById<TextView>(R.id.textView8)
        classification_view.text = classifications_avaliation?.average().toString()

        val classification_numbers = findViewById<TextView>(R.id.textView9)
        classification_numbers.text = classifications_avaliation?.sum().toString()

        newPlanReceiver = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == android.app.Activity.RESULT_OK) {
                //buscar dados no intent
                val id_plano_nutricional = it.data?.getIntExtra("id_plano_treino", -1)
                val id_ginasio = it.data?.getIntExtra("id_ginasio", -1)
                val tipo = it.data?.getStringExtra("tipo")
                val calorias = it.data?.getIntExtra("calorias", -1)
                var foto_plano_nutricional = it.data?.getStringExtra("foto_plano_nutricional")

                //criar plano_nutricional
                listPlanosNutricionais.add(
                    Plano_Nutricional(
                        id_plano_nutricional,
                        id_ginasio,
                        tipo,
                        calorias,
                        foto_plano_nutricional
                    )
                )

                adapter_nutri.notifyDataSetChanged()
            }
        }


















    }
}

