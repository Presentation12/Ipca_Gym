package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Funcionario
import vough.example.ipcagym.requests.FuncionarioRequests
import java.io.ByteArrayOutputStream
import java.io.IOException

class Activity_Gerente_Funcionario_Edit : AppCompatActivity() {
    var imageBitmapped : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerente_funcionario_edit)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        val id_funcionario = intent.getIntExtra("id_funcionario", -1)
        val id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var nome = intent.getStringExtra("nome")
        var is_admin = intent.getBooleanExtra("is_admin",false)
        var codigo = intent.getIntExtra("codigo",-1)
        val pass_salt = intent.getStringExtra("pass_salt")
        val pass_hash = intent.getStringExtra("pass_hash")
        val estado = intent.getStringExtra("estado")

        val imageView = findViewById<ImageView>(R.id.profile_pic)

        FuncionarioRequests.GetByToken(lifecycleScope, sessionToken){ resultGerente ->
            if(resultGerente != null && resultGerente.foto_funcionario.toString() != "null"){
                val pictureByteArray = Base64.decode(resultGerente.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                imageView.setImageBitmap(bitmap)
            }
        }

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

        val adapter = MyAdapter(this@Activity_Gerente_Funcionario_Edit, options)
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
                                startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(8)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(8)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(8)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(8)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(8)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(8)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(8)
                        }
                        7 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Login::class.java))
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
                                startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Perfil_Edit::class.java))
                                spinner.setSelection(10)
                            }
                        }
                        1 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Settings::class.java))
                            spinner.setSelection(10)
                        }
                        2 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Marcacoes::class.java))
                            spinner.setSelection(10)
                        }
                        3 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Planos_Treino::class.java))
                            spinner.setSelection(10)
                        }
                        4 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Planos_Nutricionais::class.java))
                            spinner.setSelection(10)
                        }
                        5 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Loja_Pedidos::class.java))
                            spinner.setSelection(10)
                        }
                        6 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Loja_Produtos::class.java))
                            spinner.setSelection(10)
                        }
                        7 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Funcionarios_List::class.java))
                            spinner.setSelection(10)
                        }
                        8 -> {
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Ginasio_Patch::class.java))
                            spinner.setSelection(10)
                        }
                        9 -> {
                            val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            val editor = preferences.edit()
                            editor.putString("session_token", "")
                            editor.putString("session_role", "")

                            editor.apply()
                            finish()
                            startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Login::class.java))
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

        imageView.setOnClickListener {
            spinner.performClick()
        }

        val funcionario_editado_image_view = findViewById<ImageView>(R.id.profile_funcionario_pic)

        FuncionarioRequests.GetByID(lifecycleScope, sessionToken, id_funcionario){
            if (it != null && it.foto_funcionario.toString() != "null")
            {
                val pictureByteArray = Base64.decode(it.foto_funcionario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                funcionario_editado_image_view.setImageBitmap(bitmap)
            }
        }


        val editNomeFuncionario = findViewById<TextView>(R.id.editTextNomeFuncionario)
        editNomeFuncionario.hint = nome
        val editCodigoFuncionario = findViewById<TextView>(R.id.editTextCodigoFuncionario)
        editCodigoFuncionario.hint = codigo.toString()
        val editIsAdmin = findViewById<CheckBox>(R.id.checkBoxIsAdminEdit)
        if(is_admin) editIsAdmin.isChecked = true

        findViewById<ImageButton>(R.id.buttonImportPhoto).setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
            else{
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        // butao de guardar funcionário editado, e volta a página ida lista de funcionarios
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Gerente_Funcionarios_List::class.java)

            var stringPhoto : String? = null

            if(imageBitmapped != null){
                val imageAdd = convertBitmapToByteArray(imageBitmapped!!)
                val aux = Base64.encodeToString(imageAdd, Base64.DEFAULT)
                stringPhoto = aux.replace("\n", "")
            }

            if (!findViewById<EditText>(R.id.editTextNomeFuncionario).text.isEmpty())
            {
                nome = editNomeFuncionario.text.toString()
            }
            if (!findViewById<EditText>(R.id.editTextCodigoFuncionario).text.isEmpty())
            {
                codigo = editCodigoFuncionario.text.toString().toInt()
            }
            is_admin = editIsAdmin.isChecked


            val funcionarioEditado = Funcionario(id_funcionario,id_ginasio,nome,is_admin,codigo,pass_salt,pass_hash,estado, stringPhoto)
            FuncionarioRequests.Patch(lifecycleScope,sessionToken,id_funcionario,funcionarioEditado){ resultEditFuncionario ->
                if (resultEditFuncionario == "Error: Patch Employee fails")
                {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit, "Error on edit an employee", Toast.LENGTH_LONG).show()
                }
                else
                {
                    finish()
                    startActivity(intent)
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Gerente_Funcionario_Edit, Activity_Funcionario_Flux_Control::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            val imageNew = data.data

            if(imageNew != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver, imageNew)
                    imageBitmapped = ImageDecoder.decodeBitmap(source)
                    imageBitmapped = Bitmap.createScaledBitmap(imageBitmapped!!, 500, 500, false)
                    findViewById<ImageView>(R.id.profile_funcionario_pic).setImageBitmap(imageBitmapped)

                }else{
                    imageBitmapped = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, imageNew)
                    imageBitmapped = Bitmap.createScaledBitmap(imageBitmapped!!, 500, 500, false)
                    findViewById<ImageView>(R.id.profile_funcionario_pic).setImageBitmap(imageBitmapped)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap) : ByteArray{
        var byteArrayImage : ByteArrayOutputStream? = null

        return try{
            byteArrayImage = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayImage)
            byteArrayImage.toByteArray()
        }
        finally{
            if(byteArrayImage != null)
                try{
                    byteArrayImage.close()
                }
                catch (e: IOException) {
                    Toast.makeText(this@Activity_Gerente_Funcionario_Edit, "Error on image conversion", Toast.LENGTH_SHORT).show()
                }
        }
    }
}