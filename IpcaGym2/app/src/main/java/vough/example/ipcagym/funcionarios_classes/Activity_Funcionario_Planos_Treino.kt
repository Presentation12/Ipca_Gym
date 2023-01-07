package vough.example.ipcagym.funcionarios_classes

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests

class Activity_Funcionario_Planos_Treino : AppCompatActivity() {
    var planos_treino_list = arrayListOf<Plano_Treino>()
    var plano_adapter = AdapterPlanosTreino()
    var receiverNewData : ActivityResultLauncher<Intent>? = null
    var receiverDeleteData : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_treino)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
            if(it != null){
                PlanoTreinoRequests.GetAllByGinasioID(lifecycleScope, sessionToken, it?.id_ginasio!!){ result ->
                    if(!result.isEmpty()){
                        planos_treino_list = result
                        plano_adapter.notifyDataSetChanged()
                    }
                }
            }
        }

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
                val id_plano_treino = it.data?.getIntExtra("id_plano_treino", 0)
                val id_ginasio = it.data?.getIntExtra("id_ginasio", 0)
                val tipo = it.data?.getStringExtra("tipo")
                var foto_plano_treino = it.data?.getStringExtra("foto_plano_treino")

                if(foto_plano_treino == "")
                    foto_plano_treino = null

                planos_treino_list.add(Plano_Treino(id_plano_treino, id_ginasio, tipo, foto_plano_treino))
                plano_adapter.notifyDataSetChanged()
            }
        }

        receiverDeleteData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){

                val id_remove = it.data?.getIntExtra("id_remove", 0)
                val tipo_remove = it.data?.getStringExtra("tipo_remove")

                for(p in planos_treino_list){
                    if(p.id_plano_treino == id_remove && p.tipo == tipo_remove){
                        planos_treino_list.remove(p)
                        break
                    }
                }

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
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 100)
            }

            //Guardar elementos em variaveis
            val plano_treino_view = rootView.findViewById<TextView>(R.id.textViewPlanoTreino)
            val plano_treino_image = rootView.findViewById<ImageView>(R.id.imageViewPlanoTreino)

            //Adicionar os textos
            plano_treino_view.text = planos_treino_list[position].tipo
            plano_treino_image.setImageURI(planos_treino_list[position].foto_plano_treino?.toUri())

            //Clicar num rootView abre o plano de treino
            rootView.setOnClickListener {
                val intent = Intent(this@Activity_Funcionario_Planos_Treino, Activity_Funcionario_Plano_Treino_Exercicios::class.java)

                intent.putExtra("id_plano_treino", planos_treino_list[position].id_plano_treino)
                intent.putExtra("id_ginasio", planos_treino_list[position].id_ginasio)
                intent.putExtra("tipo", planos_treino_list[position].tipo)
                intent.putExtra("foto_plano_treino", planos_treino_list[position].foto_plano_treino)

                receiverDeleteData?.launch(intent)
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