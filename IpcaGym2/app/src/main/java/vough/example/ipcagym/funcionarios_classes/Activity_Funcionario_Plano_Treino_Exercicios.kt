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
import vough.example.ipcagym.data_classes.Exercicio
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Treino_Exercicios : AppCompatActivity() {
    val listExercicios = arrayListOf<Exercicio>()
    val exercicio_adapter = ExercicioAdapter()
    var receiverNewData : ActivityResultLauncher<Intent>? = null
    var receiverEditData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_exercicios)

        listExercicios.add(Exercicio(1,1,"Exercicios",
            "hnviousgfiosbfkljsdhbvipdfbvopsdfbokvusdbiohjcbaoilhvcbsodbviasdhvcoiuhsdhfojsdbpiovsouhcvhpisdfjbv+<osdbf <odsihgfloiusdrwhgoipwesrbvojswberopigbsd<pivfh","tipo",3, null,3,null))
        listExercicios.add(Exercicio(2,1,"Exercicios1", "descricao","tipo",4, null,5,null))
        listExercicios.add(Exercicio(3,1,"Exercicios2", "descricao","tipo",3, LocalTime.of(0,30,0),6,null))
        listExercicios.add(Exercicio(4,1,"Exercicios3", "descricao","tipo",null, LocalTime.parse("00:45:00"),null,null))

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

        receiverEditData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val id_exercicio = it.data?.getIntExtra("id_exercicio", 0)
                val id_plano_treino = it.data?.getIntExtra("id_plano_treino", 0)
                val nome = it.data?.getStringExtra("nome")
                Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios, id_exercicio.toString(), Toast.LENGTH_SHORT).show()

                val descricao = it.data?.getStringExtra("descricao")
                val tipo = it.data?.getStringExtra("tipo")
                val series = it.data?.getIntExtra("series", -2)
                val repeticoes = it.data?.getIntExtra("repeticoes", -2)
                val tempoMin = it.data?.getIntExtra("tempoMin", -1)
                val tempoSec = it.data?.getIntExtra("tempoSec", -1)
                val foto_exercicio = it.data?.getStringExtra("foto_exercicio")
                val typeOfSet = it.data?.getStringExtra("aux")

                for(exercicio in listExercicios){
                    if(exercicio.id_exercicio == id_exercicio && exercicio.id_plano_treino == id_plano_treino){
                        exercicio.nome = nome
                        exercicio.descricao = descricao
                        exercicio.tipo = tipo
                        exercicio.foto_exercicio = foto_exercicio

                        if(typeOfSet == "time"){
                            exercicio.tempo = LocalTime.of(0, tempoMin!!.toInt(), tempoSec!!.toInt())
                            exercicio.repeticoes = null
                            exercicio.series = null
                        }
                        else{
                            exercicio.tempo = null
                            exercicio.series = series
                            exercicio.repeticoes = repeticoes
                        }
                    }
                }

                exercicio_adapter.notifyDataSetChanged()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Do nothing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.deletePlanoButton).setOnClickListener{
            val deleteIntent = Intent()

            deleteIntent.putExtra("id_remove", intent.getIntExtra("id_plano_treino", -1))
            deleteIntent.putExtra("tipo_remove", intent.getStringExtra("tipo"))

            setResult(RESULT_OK, deleteIntent)
            finish()
        }

        findViewById<Button>(R.id.addExercicioButton).setOnClickListener{
            receiverNewData?.launch(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Add::class.java))
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
            else exercicio_quantity_view.text = "A set of " + listExercicios[position].tempo?.format(
                DateTimeFormatter.ofPattern("00:mm:ss"))

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Details::class.java)

                intent.putExtra("id_exercicio", listExercicios[position].id_exercicio)
                intent.putExtra("id_plano_treino", listExercicios[position].id_plano_treino)
                intent.putExtra("nome", listExercicios[position].nome)
                intent.putExtra("descricao", listExercicios[position].descricao)
                intent.putExtra("tipo", listExercicios[position].tipo)
                intent.putExtra("foto_exercicio", listExercicios[position].foto_exercicio)

                if (listExercicios[position].tempo == null){
                    intent.putExtra("series_value", listExercicios[position].series.toString())
                    intent.putExtra("repeticoes_value", listExercicios[position].repeticoes.toString())
                    intent.putExtra("series", listExercicios[position].series.toString() + " x " + listExercicios[position].repeticoes.toString() + " sets")
                    intent.putExtra("aux", "series")
                }
                else{
                    intent.putExtra("tempo_min_value", listExercicios[position].tempo?.minute.toString())
                    intent.putExtra("tempo_sec_value", listExercicios[position].tempo?.second.toString())
                    intent.putExtra("series", "A set of " + listExercicios[position].tempo?.format(
                        DateTimeFormatter.ofPattern("00:mm:ss")))
                    intent.putExtra("aux", "tempo")
                }

                startActivity(intent)
            }

            rootView.findViewById<Button>(R.id.apagarExercicioButton).setOnClickListener{
                listExercicios.remove(listExercicios[position])
                exercicio_adapter.notifyDataSetChanged()
            }

            rootView.findViewById<Button>(R.id.patchExercicioButton).setOnClickListener{
                val intentEdit = Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Edit::class.java)
                var aux : String

                intentEdit.putExtra("id_exercicio", listExercicios[position].id_exercicio)
                intentEdit.putExtra("id_plano_treino", listExercicios[position].id_plano_treino)
                intentEdit.putExtra("nome", listExercicios[position].nome)
                intentEdit.putExtra("descricao", listExercicios[position].descricao)
                intentEdit.putExtra("tipo", listExercicios[position].tipo)
                intentEdit.putExtra("foto_exercicio", listExercicios[position].foto_exercicio)
                intentEdit.putExtra("tempo_min", listExercicios[position].tempo?.minute.toString())
                intentEdit.putExtra("tempo_sec", listExercicios[position].tempo?.second.toString())
                intentEdit.putExtra("series", listExercicios[position].series.toString())
                intentEdit.putExtra("repeticoes", listExercicios[position].repeticoes.toString())

                if(listExercicios[position].tempo == null)
                    aux = "series"
                else
                    aux = "tempo"

                intentEdit.putExtra("aux", aux)

                receiverEditData?.launch(intentEdit)
            }

            return rootView
        }
    }
}