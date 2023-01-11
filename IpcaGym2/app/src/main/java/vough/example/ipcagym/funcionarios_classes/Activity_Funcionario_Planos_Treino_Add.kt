package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Plano_Treino
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.PlanoTreinoRequests

class Activity_Funcionario_Planos_Treino_Add : AppCompatActivity() {
    var newImageValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_planos_treino_add)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)
        val image_view = findViewById<ImageView>(R.id.profile_pic)
        var counter = 0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = listOf("Account", "Settings", "Logout", "")

        class MyAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
            override fun getCount(): Int {
                return 3
            }
        }

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ result ->
            if(result != null) {
                val pictureByteArray = Base64.decode(result.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.profile_pic).setImageBitmap(bitmap)
            }
        }

        val adapter = MyAdapter(this@Activity_Funcionario_Planos_Treino_Add, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        findViewById<Button>(R.id.addPhotoPlanNew).setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //100 é o request code de escolher imagem
            startActivityForResult(intent, 100)
        }

        findViewById<Button>(R.id.addNewPlanButton).setOnClickListener{
            val intent = Intent()

            FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){
                if(it != null){
                    PlanoTreinoRequests.Post(lifecycleScope, sessionToken, Plano_Treino(
                        null,
                        it?.id_ginasio!!,
                        findViewById<EditText>(R.id.typePlanoValue).text.toString(),
                        newImageValue
                    )){
                    }
                }
            }

            setResult(RESULT_OK, intent);
            finish()
        }

        findViewById<Button>(R.id.CancelAddNewPlanButton).setOnClickListener{
            finish()
            startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Planos_Treino::class.java))
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Planos_Treino_Add, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            newImageValue = data?.data.toString()
            findViewById<ImageView>(R.id.photoPlanoValue).setImageURI(data?.data)
        }
    }
}