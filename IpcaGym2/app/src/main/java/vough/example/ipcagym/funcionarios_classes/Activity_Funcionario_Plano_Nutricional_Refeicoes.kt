package vough.example.ipcagym.funcionarios_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Refeicao
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests
import vough.example.ipcagym.requests.RefeicaoRequests
import java.time.format.DateTimeFormatter

class Activity_Funcionario_Plano_Nutricional_Refeicoes : AppCompatActivity() {
    var listRefeicoes = arrayListOf<Refeicao>()
    val refeicaoAdapter = RefeicoesAdapter()
    var receiverNewData : ActivityResultLauncher<Intent>? = null
    var receiverEditData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicoes)
        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) {
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
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

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        val listView = findViewById<ListView>(R.id.listViewRefeicoes)
        listView.adapter = refeicaoAdapter

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

        PlanoNutricionalRequests.GetByID(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_nutricional", -1)){
            if(it != null){
                findViewById<TextView>(R.id.planoNutriTipoTitle).text = it.tipo
                findViewById<TextView>(R.id.planoNutriCaloriasTitle).text = it.calorias.toString() + " KCal"
            }
        }


        /*receiverNewData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
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
            }
        }*/

        /*receiverEditData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
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
            }
        }*/

        findViewById<Button>(R.id.removePlanButton).setOnClickListener {
            val deleteIntent = Intent()

            PlanoNutricionalRequests.DeleteChecked(lifecycleScope, sessionToken, intent.getIntExtra("id_plano_nutricional", -1)){
                if(it != "User not found")
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, "Plan removed successfully", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, "Error on removing plan", Toast.LENGTH_LONG).show()
            }

            finish()
            startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Planos_Nutricionais::class.java))
        }

        findViewById<Button>(R.id.addNewRefeicaoButton).setOnClickListener{
            val intentAdd = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Add::class.java)

            intentAdd.putExtra("id_plano_nutricional", intent.getIntExtra("id_plano_nutricional", -1))

            //receiverNewData?.launch(intentAdd)
            startActivity(intentAdd)
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
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
            //Buscar token
            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val sessionToken = preferences.getString("session_token", null)

            rootView.findViewById<TextView>(R.id.textViewHoraRefeicao).text = listRefeicoes[position].hora?.format(
                DateTimeFormatter.ofPattern("HH:mm"))

            if(listRefeicoes[position].foto_refeicao.toString() != "null"){
                val pictureByteArray = Base64.decode(listRefeicoes[position].foto_refeicao, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                rootView.findViewById<ImageView>(R.id.imageViewRefeicao).setImageBitmap(bitmap)
            }

            rootView.setOnClickListener{
                val intent2 = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Details::class.java)

                intent2.putExtra("calorias", intent.getIntExtra("calorias", -1))
                intent2.putExtra("tipo", intent.getStringExtra("tipo"))
                intent2.putExtra("id_plano_nutricional", listRefeicoes[position].id_plano_nutricional)
                intent2.putExtra("id_refeicao", listRefeicoes[position].id_refeicao)
                intent2.putExtra("descricao", listRefeicoes[position].descricao)
                intent2.putExtra("hora", listRefeicoes[position].hora?.format(DateTimeFormatter.ofPattern("HH:mm")))

                startActivity(intent2)
            }

            rootView.findViewById<Button>(R.id.buttonDeleteRefeicao).setOnClickListener{
                RefeicaoRequests.Delete(lifecycleScope, sessionToken, listRefeicoes[position].id_refeicao!!){
                    if(it != "User not found"){
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
                    }
                }
            }

            rootView.findViewById<Button>(R.id.buttonEditRefeicao).setOnClickListener{
                val intentEdit = Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicoes, Activity_Funcionario_Plano_Nutricional_Refeicao_Edit::class.java)

                intentEdit.putExtra("id_plano_nutricional", listRefeicoes[position].id_plano_nutricional)
                intentEdit.putExtra("id_refeicao", listRefeicoes[position].id_refeicao)
                intentEdit.putExtra("descricao", listRefeicoes[position].descricao)
                intentEdit.putExtra("hora_hora", listRefeicoes[position].hora?.hour)
                intentEdit.putExtra("hora_minute", listRefeicoes[position].hora?.minute)

               // receiverEditData?.launch(intentEdit)
                startActivity(intentEdit)
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