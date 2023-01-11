package vough.example.ipcagym.funcionarios_classes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R
import vough.example.ipcagym.requests.FuncionarioRequests
import vough.example.ipcagym.requests.RefeicaoRequests
import java.io.ByteArrayOutputStream
import java.io.IOException

class Activity_Funcionario_Plano_Nutricional_Refeicao_Edit: AppCompatActivity() {
    var imageBitmapped : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_plano_nutricional_refeicao_edit)

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

        findViewById<TextView>(R.id.refeicaoEditDescriptionValue).text = intent.getStringExtra("descricao")
        findViewById<TextView>(R.id.refeicaoEditHourHourValue).text = intent.getIntExtra("hora_hora", -1).toString()
        findViewById<TextView>(R.id.refeicaoEditHourMinuteValue).text = intent.getIntExtra("hora_minute", -1).toString()

        RefeicaoRequests.GetByID(lifecycleScope, sessionToken, intent.getIntExtra("id_refeicao", -1)){ result ->
            if(result != null) {
                val pictureByteArray = Base64.decode(result.foto_refeicao, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                findViewById<ImageView>(R.id.refeicaoEditPhotoValue).setImageBitmap(bitmap)
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

        val adapter = MyAdapter(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, options)
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
                            startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Perfil_Edit::class.java))
                            spinner.setSelection(3)
                        }
                    }
                    1 -> {
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Settings::class.java))
                        spinner.setSelection(3)
                    }
                    2 -> {
                        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("session_token", "")

                        editor.apply()
                        finish()
                        startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Login::class.java))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinner.setSelection(3)
            }
        }

        findViewById<Button>(R.id.importEditPhotoMeal).setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
            else{
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        findViewById<Button>(R.id.editMealButton).setOnClickListener{
            val editIntent = Intent()

            var minAuxInt = findViewById<TextView>(R.id.refeicaoEditHourHourValue).text.toString().toInt()
            var secAuxInt = findViewById<TextView>(R.id.refeicaoEditHourMinuteValue).text.toString().toInt()

            var stringFoto : String? = null

            /*if(imageBitmapped != null){
                    val imageAdd = convertBitmapToByteArray(imageBitmapped!!)
                    val aux = Base64.encodeToString(imageAdd, Base64.DEFAULT)
                    stringFoto = aux.replace("\n", "")
                }*/

            var minAuxString : String?
            var secAuxString : String?

            if(minAuxInt < 10)
                minAuxString = "0$minAuxInt"
            else
                minAuxString = minAuxInt.toString()

            if(secAuxInt < 10)
                secAuxString = "0$secAuxInt"
            else
                secAuxString = secAuxInt.toString()

            val tempoToPatch = "$minAuxString:$secAuxString:00"

            val jsonBody = """
                {
                  "id_refeicao": ${intent.getIntExtra("id_refeicao", -1)},
                  "id_plano_nutricional": ${intent.getIntExtra("id_plano_nutricional", -1)},
                  "descricao": "${findViewById<TextView>(R.id.refeicaoEditDescriptionValue).text}",
                  "hora": "$tempoToPatch",
                  "foto_refeicao": "$stringFoto"
                }
            """

            RefeicaoRequests.Patch(lifecycleScope, sessionToken, intent.getIntExtra("id_refeicao", -1) , jsonBody){
                if(it != "User not found")
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, "Meal added sucessfully", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, "Error on adding meal", Toast.LENGTH_LONG).show()
            }

            setResult(RESULT_OK, editIntent)
            finish()
        }

        image_view.setOnClickListener {
            spinner.performClick()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Pagina_Inicial::class.java))
                    finish()

                    true
                }
                R.id.nav_clients -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Clientes_List::class.java))
                    finish()

                    true
                }
                R.id.nav_shopping -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Loja_Pedidos::class.java))
                    finish()

                    true
                }
                R.id.nav_capacity -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Capacity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, Activity_Funcionario_Flux_Control::class.java))
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
                    imageBitmapped = resizeBitmap(imageBitmapped!!, 400)
                    findViewById<ImageView>(R.id.refeicaoEditPhotoValue).setImageBitmap(imageBitmapped)

                }else{
                    imageBitmapped = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, imageNew)
                    imageBitmapped = resizeBitmap(imageBitmapped!!, 400)
                    findViewById<ImageView>(R.id.refeicaoEditPhotoValue).setImageBitmap(imageBitmapped)
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
                    Toast.makeText(this@Activity_Funcionario_Plano_Nutricional_Refeicao_Edit, "Error on image conversion", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) return source

                val aspectRatio = source.width.toDouble() / source.height.toDouble()
                val targetWidth = (maxLength * aspectRatio).toInt()

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
            } else {
                if (source.width <= maxLength) return source

                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
            }
        } catch (e: Exception) { return source }
    }
}