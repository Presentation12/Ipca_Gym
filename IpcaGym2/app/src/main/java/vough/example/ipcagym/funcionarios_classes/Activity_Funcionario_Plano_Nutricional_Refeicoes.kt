package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import org.w3c.dom.Text
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Exercicio
import vough.example.ipcagym.data_classes.Refeicao
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.RefeicaoRequests
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Nutricional_Refeicoes : AppCompatActivity() {
    var listRefeicoes = arrayListOf<Refeicao>()
    val refeicaoAdapter = RefeicoesAdapter()
    var receiverNewData : ActivityResultLauncher<Intent>? = null
    var receiverEditData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicoes)

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        RefeicaoRequests.GetAllByPlanoID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_nutricional", -1)){
            if(it.isNotEmpty()){
                listRefeicoes = it
                refeicaoAdapter.notifyDataSetChanged()
                findViewById<TextView>(R.id.textView11).text = ""
            }
            else{
                findViewById<TextView>(R.id.textView11).text = "Empty plan, add some meals!"
            }
        }

        findViewById<TextView>(R.id.planoNutriTipoTitle).text = intent.getStringExtra("tipo")
        findViewById<TextView>(R.id.planoNutriCaloriasTitle).text = intent.getIntExtra("calorias", -1).toString() + " KCal"

        val listView = findViewById<ListView>(R.id.listViewRefeicoes)
        listView.adapter = refeicaoAdapter

        receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if(it.resultCode == Activity.RESULT_OK){
                //Buscar dados no intent
                val id_refeicao = it.data?.getIntExtra("id_refeicao", -1)
                val id_plano_nutricional = it.data?.getIntExtra("id_plano_nutricional", -1)
                val descricao = it.data?.getStringExtra("descricao")
                val hora_hour = it.data?.getIntExtra("hora_hour", -1)
                val hora_minute = it.data?.getIntExtra("hora_minute", -1)
                var foto_refeicao = it.data?.getStringExtra("foto_refeicao")

                if(foto_refeicao == "")
                    foto_refeicao = null

                //Inserir dados na nova refeicao
                listRefeicoes.add(Refeicao(id_refeicao,id_plano_nutricional,descricao, LocalTime.of(hora_hour!!, hora_minute!!), foto_refeicao))
                refeicaoAdapter.notifyDataSetChanged()
            }
        }

        receiverEditData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if(it.resultCode == Activity.RESULT_OK){
                //Buscar dados no intent
                val id_refeicao = it.data?.getIntExtra("id_refeicao", -1)
                val id_plano_nutricional = it.data?.getIntExtra("id_plano_nutricional", -1)
                val descricao = it.data?.getStringExtra("descricao")
                val hora_hour = it.data?.getIntExtra("hora_hour", -1)
                val hora_minute = it.data?.getIntExtra("hora_minute", -1)
                var foto_refeicao = it.data?.getStringExtra("foto_refeicao")

                if(foto_refeicao == "")
                    foto_refeicao = null

                //Atualizar dados na refeicao
                for(refeicao in listRefeicoes){
                    if(refeicao.id_refeicao == id_refeicao && refeicao.id_plano_nutricional == id_plano_nutricional){
                        refeicao.descricao = descricao
                        refeicao.hora = LocalTime.of(hora_hour!!, hora_minute!!)
                        refeicao.foto_refeicao = foto_refeicao
                        break;
                    }
                }

                refeicaoAdapter.notifyDataSetChanged()
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

        findViewById<Button>(R.id.removePlanButton).setOnClickListener {
            val deleteIntent = Intent()

            deleteIntent.putExtra("id_remove", intent.getIntExtra("id_plano_nutricional", -1))
            deleteIntent.putExtra("tipo_remove", intent.getStringExtra("tipo"))

            setResult(RESULT_OK, deleteIntent)
            finish()
        }

        findViewById<Button>(R.id.addNewRefeicaoButton).setOnClickListener{
            receiverNewData?.launch(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Add::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

    }

    inner class RefeicoesAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return listRefeicoes.size
        }

        override fun getItem(position: Int): Any {
            return listRefeicoes[position]
        }

        override fun getItemId(position: Int): Long {
            return listRefeicoes[position].id_plano_nutricional?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_refeicao, parent, false)

            rootView.findViewById<TextView>(R.id.textViewHoraRefeicao).text = listRefeicoes[position].hora?.format(
                DateTimeFormatter.ofPattern("HH:mm"))
            rootView.findViewById<ImageView>(R.id.imageViewRefeicao).setImageURI(listRefeicoes[position].foto_refeicao?.toUri())

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Details::class.java)

                intent.putExtra("id_plano_nutricional", listRefeicoes[position].id_plano_nutricional)
                intent.putExtra("id_refeicao", listRefeicoes[position].id_refeicao)
                intent.putExtra("descricao", listRefeicoes[position].descricao)
                intent.putExtra("hora", listRefeicoes[position].hora?.format(DateTimeFormatter.ofPattern("HH:mm")))
                intent.putExtra("foto_refeicao", listRefeicoes[position].foto_refeicao)

                startActivity(intent)
            }

            rootView.findViewById<Button>(R.id.buttonDeleteRefeicao).setOnClickListener{
                listRefeicoes.remove(listRefeicoes[position])
                refeicaoAdapter.notifyDataSetChanged()
            }

            rootView.findViewById<Button>(R.id.buttonEditRefeicao).setOnClickListener{
                val intentEdit = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Edit::class.java)

                intentEdit.putExtra("id_plano_nutricional", listRefeicoes[position].id_plano_nutricional)
                intentEdit.putExtra("id_refeicao", listRefeicoes[position].id_refeicao)
                intentEdit.putExtra("descricao", listRefeicoes[position].descricao)
                intentEdit.putExtra("hora_hora", listRefeicoes[position].hora?.hour)
                intentEdit.putExtra("hora_minute", listRefeicoes[position].hora?.minute)
                intentEdit.putExtra("foto_refeicao", listRefeicoes[position].foto_refeicao)

                receiverEditData?.launch(intentEdit)
            }

            return rootView
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Do the read operation.
                } else {
                    // Permission denied. Show a message to the user.
                }
                return
            }
            // Handle other request codes.
        }
    }
}