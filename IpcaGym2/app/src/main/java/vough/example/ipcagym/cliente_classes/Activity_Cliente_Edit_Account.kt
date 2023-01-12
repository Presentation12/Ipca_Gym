package vough.example.ipcagym.cliente_classes

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
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import vough.example.ipcagym.R
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Cliente
import vough.example.ipcagym.requests.AtividadeRequests
import vough.example.ipcagym.requests.ClienteRequests
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.time.format.TextStyle
import java.util.*

class Activity_Cliente_Edit_Account : AppCompatActivity() {
    var imageBitmapped : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_edit_account)

        //Buscar token
        val preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val sessionToken = preferences.getString("session_token", null)

        var id_cliente = intent.getIntExtra("id_cliente", -1)
        var id_ginasio = intent.getIntExtra("id_ginasio", -1)
        var id_plano_nutricional : Int? = intent.getIntExtra("id_plano_nutricional", 0)
        var nome = intent.getStringExtra("nome")
        var mail = intent.getStringExtra("mail")
        var telemovel = intent.getIntExtra("telemovel",-1)
        var pass_salt = intent.getStringExtra("pass_salt")
        var pass_hash = intent.getStringExtra("pass_hash")
        var peso : Double? = intent.getDoubleExtra("peso",0.0)
        var altura : Int?  = intent.getIntExtra("altura",-1)
        var gordura : Double?  = intent.getDoubleExtra("gordura",0.0)
        var estado = intent.getStringExtra("estado")

        val cliente_image_view = findViewById<ImageView>(R.id.profile_pic)

        ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
            if(it != null && it.foto_perfil.toString() != "null"){
                val pictureByteArray = Base64.decode(it.foto_perfil, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                cliente_image_view.setImageBitmap(bitmap)
            }
        }

        findViewById<TextView>(R.id.editTextNomeCliente).hint = nome
        findViewById<TextView>(R.id.editTextMailCliente).hint = mail
        findViewById<TextView>(R.id.editTextContactoCliente).hint = telemovel.toString()

        var passwordEditText = findViewById<EditText>(R.id.editTextPasswordCliente)
        var showHidePassword = findViewById<ImageButton>(R.id.imageButtonSeePassword)

        showHidePassword.setOnClickListener {
            // Verifica se a senha está sendo exibida
            if (passwordEditText.inputType == InputType.TYPE_CLASS_TEXT) {
                // Muda para o tipo de entrada que esconde a senha
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                // Muda para o tipo de entrada que mostra a senha
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        var passwordEditText2 = findViewById<EditText>(R.id.editTextPasswordCliente2)
        var showHidePassword2 = findViewById<ImageButton>(R.id.imageButtonSeePassword2)

        showHidePassword2.setOnClickListener {
            // Verifica se a senha está sendo exibida
            if (passwordEditText2.inputType == InputType.TYPE_CLASS_TEXT) {
                // Muda para o tipo de entrada que esconde a senha
                passwordEditText2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                // Muda para o tipo de entrada que mostra a senha
                passwordEditText2.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        findViewById<ImageButton>(R.id.buttonImportPhoto).setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
            else{
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        // butao de guardar cliente editado, e volta a página ida lista de clientes

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = Intent(this@Activity_Cliente_Edit_Account, Activity_Cliente_Account::class.java)

            if (findViewById<EditText>(R.id.editTextNomeCliente).text.isEmpty() == false)
            {
                nome = findViewById<EditText>(R.id.editTextNomeCliente).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextMailCliente).text.isEmpty() == false)
            {
                mail = findViewById<EditText>(R.id.editTextMailCliente).text.toString()
            }
            if (findViewById<EditText>(R.id.editTextContactoCliente).text.isEmpty() == false)
            {
                telemovel = findViewById<EditText>(R.id.editTextContactoCliente).text.toString().toInt()
            }

            var newPass : String? = null
            if (findViewById<EditText>(R.id.editTextPasswordCliente).text.isEmpty() == false && findViewById<EditText>(R.id.editTextPasswordCliente).text.toString() == findViewById<EditText>(R.id.editTextPasswordCliente2).text.toString())
            {
                newPass = findViewById<EditText>(R.id.editTextPasswordCliente).text.toString()
            }

            if(id_plano_nutricional == 0) id_plano_nutricional = null
            if(peso.toString() == "NaN") peso = null
            if(altura == 0) altura = null
            if(gordura.toString() == "NaN") gordura = null

            var stringPhoto : String? = null

            if(imageBitmapped != null){
                val imageAdd = convertBitmapToByteArray(imageBitmapped!!)
                val aux = Base64.encodeToString(imageAdd, Base64.DEFAULT)
                stringPhoto = aux.replace("\n", "")

                var editCliente = Cliente(id_cliente,id_ginasio,id_plano_nutricional,nome,mail,telemovel,pass_salt,pass_hash,peso,altura,gordura,stringPhoto,estado)
                ClienteRequests.Patch(lifecycleScope,sessionToken,id_cliente, editCliente) { resultEditcliente ->
                    if (resultEditcliente == "Error: Patch Client fails")
                    {
                        Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on editting client account", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        finish()
                        startActivity(intent)
                    }
                }
            }
            else{
                ClienteRequests.GetByID(lifecycleScope, sessionToken, id_cliente){
                    if(it != null){
                        if(it.foto_perfil != "null" && it.foto_perfil != null)
                            stringPhoto = it.foto_perfil
                        else
                            stringPhoto = null

                        var editCliente = Cliente(id_cliente,id_ginasio,id_plano_nutricional,nome,mail,telemovel,pass_salt,pass_hash,peso,altura,gordura,stringPhoto,estado)
                        ClienteRequests.Patch(lifecycleScope,sessionToken,id_cliente, editCliente) { resultEditcliente ->
                            if (resultEditcliente == "Error: Patch Client fails")
                                Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on editting client account", Toast.LENGTH_LONG).show()
                            else
                            {
                                finish()
                                startActivity(intent)
                            }
                        }
                    }
                }
            }

            //TODO VERIFICAR PASS
            if(newPass != null)
            {
                ClienteRequests.recoverPasswordCliente(lifecycleScope, mail, newPass) { resultNewPassCliente ->
                    if (resultNewPassCliente == "error")
                    {
                        Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on change client password", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        finish()
                        startActivity(intent)
                    }
                }
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
                    findViewById<ImageView>(R.id.profile_pic).setImageBitmap(imageBitmapped)

                }else{
                    imageBitmapped = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, imageNew)
                    imageBitmapped = Bitmap.createScaledBitmap(imageBitmapped!!, 500, 500, false)
                    findViewById<ImageView>(R.id.profile_pic).setImageBitmap(imageBitmapped)
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
                    Toast.makeText(this@Activity_Cliente_Edit_Account, "Error on image conversion", Toast.LENGTH_SHORT).show()
                }
        }
    }
}