package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.requests.ExercicioRequests
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Treino_Exercicios : AppCompatActivity() {
    var listExercicios = arrayListOf<Exercicio>()
    val exercicio_adapter = ExercicioAdapter()
    var receiverNewData : ActivityResultLauncher<Intent>? = null
    var receiverEditData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_exercicios)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            if(it != null){
                val pictureByteArray = Base64.decode(it.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)

                ExercicioRequests.GetAllByPlanoID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_treino", -1)){ result ->
                    if(result.isNotEmpty()) {
                        listExercicios = result
                        exercicio_adapter.notifyDataSetChanged()
                    }
                    else{
                        findViewById<TextView>(R.id.textView9).text = "This Plan is empty\nAdd some exercises!"
                    }
                }
            }
        }

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Treino_Exercicios, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        if(counter == 0){
                            counter+=1
                            spinner.setSelection(3)
                        }
                        else{
                            startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        val list_view_planos_treino = findViewById<ListView>(R.id.listViewExercicios)
        list_view_planos_treino.adapter = exercicio_adapter

        receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if(it.resultCode == Activity.RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    ExercicioRequests.GetAllByPlanoID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_treino", -1)){ result ->
                        if(result.isNotEmpty()) {
                            listExercicios = result

                            exercicio_adapter.notifyDataSetChanged()
                            if(exercicio_adapter.count > 0) findViewById<TextView>(R.id.textView9).text = ""
                        }
                        else{
                            findViewById<TextView>(R.id.textView9).text = "This Plan is empty\nAdd some exercises!"
                        }
                    }
                }
            }
        }

        receiverEditData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    ExercicioRequests.GetAllByPlanoID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_treino", -1)){ result ->
                        if(result.isNotEmpty()) {
                            listExercicios = result

                            exercicio_adapter.notifyDataSetChanged()
                            if(exercicio_adapter.count > 0) findViewById<TextView>(R.id.textView9).text = ""
                        }
                        else{
                            findViewById<TextView>(R.id.textView9).text = "This Plan is empty\nAdd some exercises!"
                        }
                    }
                }
            }
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        findViewById<Button>(R.id.deletePlanoButton).setOnClickListener{
            val deleteIntent = Intent()

            PlanoTreinoRequests.DeleteChecked(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_treino", -1)){
                if(it != "Error: Delete Checked Plano Treino fails") Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios, "Plan removed successfully", Toast.LENGTH_LONG).show()
                else Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios, "Error on removing plan", Toast.LENGTH_LONG).show()
            }

            setResult(RESULT_OK, deleteIntent)
            finish()
        }

        findViewById<Button>(R.id.addExercicioButton).setOnClickListener{
            val newIntent = Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Add::class.java)

            newIntent.putExtra("id_plano_treino", intent.getIntExtra("id_plano_treino", -1))

            receiverNewData?.launch(newIntent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
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
            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            //Guardar elementos em variaveis
            val exercicio_nome_view = rootView.findViewById<TextView>(R.id.textViewNomeExercicios)
            val exercicio_quantity_view = rootView.findViewById<TextView>(R.id.textViewSetsExercicio)
            val exercicio_image = rootView.findViewById<ImageView>(R.id.imageViewPlanoTreino)

            val pictureByteArray = Base64.decode(listExercicios[position].foto_exercicio, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
            exercicio_image.setImageBitmap(bitmap)

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
                ExercicioRequests.Delete(lifecycleScope, sessionToken, listExercicios[position].id_exercicio!!){ result ->
                    if(result == "Error: Delete Exercise fails"){
                        Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios, "Error removing the exercise", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@Activity_Funcionario_Plano_Treino_Exercicios, "Exercise removed successfully", Toast.LENGTH_SHORT).show()
                    }
                }

                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    ExercicioRequests.GetAllByPlanoID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_treino", -1)){ result ->
                        if(result.isNotEmpty()) {
                            listExercicios = result
                            exercicio_adapter.notifyDataSetChanged()
                        }
                        else{
                            listExercicios.clear()
                            exercicio_adapter.notifyDataSetChanged()
                            findViewById<TextView>(R.id.textView9).text = "This Plan is empty\nAdd some exercises!"
                        }
                    }
                }
            }

            rootView.findViewById<Button>(R.id.patchExercicioButton).setOnClickListener{
                val intentEdit = Intent(this@Activity_Funcionario_Plano_Treino_Exercicios, Activity_Funcionario_Plano_Treino_Exercicio_Edit::class.java)
                var aux : String

                intentEdit.putExtra("id_exercicio", listExercicios[position].id_exercicio)
                intentEdit.putExtra("id_plano_treino", listExercicios[position].id_plano_treino)
                intentEdit.putExtra("nome", listExercicios[position].nome)
                intentEdit.putExtra("descricao", listExercicios[position].descricao)
                intentEdit.putExtra("tipo", listExercicios[position].tipo)
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