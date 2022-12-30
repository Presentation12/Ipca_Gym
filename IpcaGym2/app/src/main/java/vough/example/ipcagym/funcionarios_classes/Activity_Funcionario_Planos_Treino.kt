package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.cliente_classes.PlanoTreinoExerciciosClienteActivity
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Plano_Treino
import java.time.LocalTime

class Activity_Funcionario_Planos_Treino : AppCompatActivity() {
    var planos_treino_list = arrayListOf<Plano_Treino>()
    var plano_adapter = AdapterPlanosTreino()
    var receiverNewData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_treino)

        planos_treino_list.add(Plano_Treino(1,1,"Treino X", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino Y", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino W", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino Z", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino A", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino F", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino E", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino R", "Descrição"))
        planos_treino_list.add(Plano_Treino(1,1,"Treino T", "Descrição"))

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_planos_treino = findViewById<ListView>(R.id.listviewPlanosTreino)
        list_view_planos_treino.adapter = plano_adapter

        receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if(it.resultCode == Activity.RESULT_OK){
                //Buscar variaveis no intent

                //add na lista
                plano_adapter.notifyDataSetChanged()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Planos_Treino, options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.addPlanButton).setOnClickListener{
            receiverNewData?.launch(Intent(this@Activity_Funcionario_Planos_Treino, Activity_Funcionario_Planos_Treino_Add::class.java))
        }
    }

    inner class AdapterPlanosTreino : BaseAdapter(){
        override fun getCount(): Int {
            return planos_treino_list.size
        }

        override fun getItem(position: Int): Any {
            return planos_treino_list[position]
        }

        override fun getItemId(position: Int): Long {
            return planos_treino_list[position].id_ginasio?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_plano_treino, parent, false)

            //Guardar elementos em variaveis
            val plano_treino_view = rootView.findViewById<TextView>(R.id.textViewPlanoTreino)

            //Adicionar os textos
            plano_treino_view.text = planos_treino_list[position].tipo

            //Clicar num rootView abre o plano de treino
            rootView.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Planos_Treino, Activity_Funcionario_Plano_Treino_Exercicios::class.java)

                intent.putExtra("id_plano_treino", planos_treino_list[position].id_plano_treino)
                intent.putExtra("id_ginasio", planos_treino_list[position].id_ginasio)
                intent.putExtra("tipo", planos_treino_list[position].tipo)
                intent.putExtra("foto_plano_treino", planos_treino_list[position].foto_plano_treino)

                startActivity(intent)
            }

            return rootView
        }

    }
}