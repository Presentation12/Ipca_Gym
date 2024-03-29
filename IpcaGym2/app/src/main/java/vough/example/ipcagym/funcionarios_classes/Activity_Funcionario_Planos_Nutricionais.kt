package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Activity_Funcionario_Planos_Nutricionais : AppCompatActivity() {
    var listPlanosNutricionais = arrayListOf<Plano_Nutricional>()
    val adapter_nutri = PlanNutriAdapter()
    var newPlanReceiver : ActivityResultLauncher<Intent>? = null
    var deletePlanReceiver : ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_nutricionais)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            if(it != null && it.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(it.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }
        }

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken) {
            if (it != null) {
                PlanoNutricionalRequests.GetAllByGinasioID(
                    lifecycleScope,
                    sessionToken,
                    it.id_ginasio!!
                ) { result ->
                    if (result.isNotEmpty()) {
                        listPlanosNutricionais = result
                        adapter_nutri.notifyDataSetChanged()
                    }
                }
            }
        }

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val sessionRole = preferences.getString("session_role", null)
        var options: List<String>

        if(sessionRole == "Funcionario"){
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Logout", "")
        }
        else{
            options = listOf("Account", "Settings", "Appointments", "Training Plans", "Diet Plans",
                "Product Requests", "Products", "Employees" , "Gym Edit", "Logout", "")
        }

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return if(sessionRole == "Funcionario") {
                    8
                }else{
                    10
                }
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Planos_Nutricionais, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(sessionRole == "Funcionario"){
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(8)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
                else{
                    when (position) {
                        0 -> {
                            if(counter == 0){
                                counter+=1
                                spinner.setSelection(10)
                            }
                            else{
                                startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Login::class.java))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                if(sessionRole == "Funcionario")
                    spinner.setSelection(8)
                else
                    spinner.setSelection(10)
            }
        }

        val listPlanosNutricionaisView = findViewById<ListView>(R.id.listViewPlanosNutricionais)
        listPlanosNutricionaisView.adapter = adapter_nutri

        /*newPlanReceiver = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ tokenResult ->
                    lifecycleScope.launch {
                        delay(10000L)
                    }
                    if(tokenResult != null){
                        PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope, sessionToken, tokenResult?.id_ginasio!!){ result ->
                            if(result.isNotEmpty()){
                                listPlanosNutricionais = result
                                adapter_nutri.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }*/

        /*deletePlanReceiver = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    lifecycleScope.launch {
                        delay(10000L)
                    }
                    PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope, sessionToken, it?.id_ginasio!!){ result ->
                        if(result.isNotEmpty()){
                            listPlanosNutricionais = result

                            adapter_nutri.notifyDataSetChanged()
                        }
                    }
                }
            }
        }*/

        findViewById<Button>(R.id.buttonAddNutriPlan).setOnClickListener{
            //newPlanReceiver?.launch(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Nutricionais_Add::class.java))
            startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Nutricionais_Add::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    inner class PlanNutriAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return listPlanosNutricionais.size
        }

        override fun getItem(position: Int): Any {
            return listPlanosNutricionais[position]
        }

        override fun getItemId(position: Int): Long {
            return listPlanosNutricionais[position].id_plano_nutricional?.toLong()?:-1L
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_plano_nutricional, parent, false)

            rootView.findViewById<TextView>(R.id.textViewPlanoNutricional).text = listPlanosNutricionais[position].tipo

            if(listPlanosNutricionais[position].foto_plano_nutricional.toString() != "null"){
                val pictureByteArray = Base64.decode(listPlanosNutricionais[position].foto_plano_nutricional, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                rootView.findViewById<ImageView>(R.id.imageViewPlanoNutricional).setImageBitmap(bitmap)
            }

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Plano_Nutricional_Refeicoes::class.java)

                intent.putExtra("id_plano_nutricional", listPlanosNutricionais[position].id_plano_nutricional)
                intent.putExtra("id_ginasio", listPlanosNutricionais[position].id_ginasio)
                intent.putExtra("tipo", listPlanosNutricionais[position].tipo)
                intent.putExtra("calorias", listPlanosNutricionais[position].calorias)

                //deletePlanReceiver?.launch(intent)
                startActivity(intent)
            }

            return rootView
        }

    }

}