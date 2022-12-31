package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Plano_Treino
import java.sql.Time
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.typeOf

class Activity_Funcionario_Plano_Treino_Exercicios : AppCompatActivity() {
    val listExercicios = arrayListOf<Exercicio>()
    val exercicio_adapter = ExercicioAdapter()
    var receiverNewData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_exercicios)

        listExercicios.add(Exercicio(1,1,"Exercicios",
            "hnviousgfiosbfkljsdhbvipdfbvopsdfbokvusdbiohjcbaoilhvcbsodbviasdhvcoiuhsdhfojsdbpiovsouhcvhpisdfjbv+<osdbf <odsihgfloiusdrwhgoipwesrbvojswberopigbsd<pivfh","tipo",3, null,3,null))
        listExercicios.add(Exercicio(1,1,"Exercicios", "descricao","tipo",4, null,5,null))
        listExercicios.add(Exercicio(1,1,"Exercicios", "descricao","tipo",3, LocalTime.of(0,30,0),6,null))
        listExercicios.add(Exercicio(1,1,"Exercicios", "descricao","tipo",null, LocalTime.parse("00:45:00"),null,null))

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val list_view_planos_treino = findViewById<ListView>(R.id.listViewExercicios)
        list_view_planos_treino.adapter = exercicio_adapter

        receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if(it.resultCode == Activity.RESULT_OK){
                val id_exercicio = it.data?.getIntExtra("id_exercicio", 0)
                val id_plano_treino = it.data?.getIntExtra("id_plano_treino", 0)
                val nome = it.data?.getStringExtra("nome")
                val descricao = it.data?.getStringExtra("descricao")
                val tipo = it.data?.getStringExtra("tipo")
                val series = it.data?.getIntExtra("series", 0)
                val repeticoes = it.data?.getIntExtra("repeticoes", 0)
                val tempoMin = it.data?.getStringExtra("tempoMin")
                val tempoSec = it.data?.getStringExtra("tempoSec")
                val foto_exercicio = it.data?.getStringExtra("foto_exercicio")
                val typeOfSet = it.data?.getStringExtra("aux")

                if(typeOfSet == "time"){
                    listExercicios.add(Exercicio(id_exercicio, id_plano_treino, nome, descricao, tipo, null,
                        LocalTime.of(0, tempoMin.toString().toInt(), tempoSec.toString().toInt()), null, foto_exercicio))
                }
                else{
                    listExercicios.add(Exercicio(id_exercicio, id_plano_treino, nome, descricao, tipo, series, null, repeticoes, foto_exercicio))
                }

                exercicio_adapter.notifyDataSetChanged()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios,options[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.addExercicioButton).setOnClickListener{
            receiverNewData?.launch(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Add::class.java))
        }

        findViewById<Button>(R.id.deletePlanoButton).setOnClickListener{
            val intentDelete = Intent()

            intentDelete.putExtra("id_remove", intent.getIntExtra("id_plano_treino", 0))

            setResult(RESULT_OK, intentDelete);
            finish()
        }
    }

    inner class ExercicioAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return listExercicios.size
        }

        override fun getItem(position: Int): Any {
            return listExercicios[position]
        }

        override fun getItemId(position: Int): Long {
            return listExercicios[position].id_exercicio?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_exercicio, parent, false)

            //Guardar elementos em variaveis
            val exercicio_nome_view = rootView.findViewById<TextView>(R.id.textViewNomeExercicios)
            val exercicio_quantity_view = rootView.findViewById<TextView>(R.id.textViewSetsExercicio)

            //Adicionar os textos
            exercicio_nome_view.text = listExercicios[position].nome
            if (listExercicios[position].tempo == null)
            {
                var seriesRepeticoes = listExercicios[position].series.toString() + " x " + listExercicios[position].repeticoes.toString() + " sets"
                exercicio_quantity_view.text = seriesRepeticoes
            }
            else exercicio_quantity_view.text = "A set of " + listExercicios[position].tempo.toString()

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Details::class.java)

                intent.putExtra("id_exercicio", listExercicios[position].id_exercicio)
                intent.putExtra("id_plano_treino", listExercicios[position].id_plano_treino)
                intent.putExtra("nome", listExercicios[position].nome)
                intent.putExtra("descricao", listExercicios[position].descricao)
                intent.putExtra("tipo", listExercicios[position].tipo)
                intent.putExtra("foto_exercicio", listExercicios[position].foto_exercicio)

                if (listExercicios[position].tempo == null)
                    intent.putExtra("series", listExercicios[position].series.toString() + " x " + listExercicios[position].repeticoes.toString() + " sets")
                else
                    intent.putExtra("series", "A set of " + listExercicios[position].tempo?.format(
                        DateTimeFormatter.ofPattern("00:mm:ss")))


                startActivity(intent)
            }

            return rootView
        }

    }
}