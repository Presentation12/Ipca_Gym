package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Nutricional
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoNutricionalRequests

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
            PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope, sessionToken, it?.id_ginasio!!){ result ->
                if(result.isNotEmpty()){
                    listPlanosNutricionais = result
                    adapter_nutri.notifyDataSetChanged()
                }
            }
        }

        val image_view = findViewById<ImageView>(R.id.profile_pic)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Conta", "Definições", "Sair")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val listPlanosNutricionaisView = findViewById<ListView>(R.id.listViewPlanosNutricionais)
        listPlanosNutricionaisView.adapter = adapter_nutri

        newPlanReceiver = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == android.app.Activity.RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope, sessionToken, it?.id_ginasio!!){ result ->
                        if(result.isNotEmpty()){
                            listPlanosNutricionais = result
                            adapter_nutri.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        deletePlanReceiver = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == android.app.Activity.RESULT_OK){
                FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                    PlanoNutricionalRequests.GetAllByGinasioID(lifecycleScope, sessionToken, it?.id_ginasio!!){ result ->
                        if(result.isNotEmpty()){
                            listPlanosNutricionais = result
                            adapter_nutri.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        findViewById<Button>(R.id.buttonAddNutriPlan).setOnClickListener{
            newPlanReceiver?.launch(Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Planos_Nutricionais_Add::class.java))
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
            rootView.findViewById<ImageView>(R.id.imageViewPlanoNutricional).setImageURI(listPlanosNutricionais[position].foto_plano_nutricional?.toUri())

            rootView.setOnClickListener{
                val intent = Intent(this@Activity_Funcionario_Planos_Nutricionais, Activity_Funcionario_Plano_Nutricional_Refeicoes::class.java)

                intent.putExtra("id_plano_nutricional", listPlanosNutricionais[position].id_plano_nutricional)
                intent.putExtra("id_ginasio", listPlanosNutricionais[position].id_ginasio)
                intent.putExtra("tipo", listPlanosNutricionais[position].tipo)
                intent.putExtra("calorias", listPlanosNutricionais[position].calorias)
                intent.putExtra("foto_plano_nutricional", listPlanosNutricionais[position].foto_plano_nutricional)

                deletePlanReceiver?.launch(intent)
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