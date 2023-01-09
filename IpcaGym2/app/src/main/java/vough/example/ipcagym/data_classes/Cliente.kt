package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*data class Cliente(
    val id_cliente: Int,
    val id_ginasio: Int,
    val id_plano_nutricional: Int,
    val nome: String,
    val mail: String,
    val telemovel: String,
    val pass_salt: String,
    val pass_hash: String,
    val peso: Double,
    val altura: Int,
    val gordura: Double,
    val foto_perfil: String,
    val estado: String
)*/

class Cliente{
    var id_cliente: Int? = null
    var id_ginasio: Int? = null
    var id_plano_nutricional: Int? = null
    var nome: String? = null
    var mail: String? = null
    var telemovel: Int? = null
    var pass_salt: String? = null
    var pass_hash: String? = null
    var peso: Double? = null
    var altura: Int? = null
    var gordura: Double? = null
    var foto_perfil: String? = null
    var estado: String? = null

    constructor(
        id_cliente: Int?,
        id_ginasio: Int?,
        id_plano_nutricional: Int?,
        nome: String?,
        mail: String?,
        telemovel: Int?,
        pass_salt: String?,
        pass_hash: String?,
        peso: Double?,
        altura: Int?,
        gordura: Double?,
        foto_perfil: String?,
        estado: String?
    ) {
        this.id_cliente = id_cliente
        this.id_ginasio = id_ginasio
        this.id_plano_nutricional = id_plano_nutricional
        this.nome = nome
        this.mail = mail
        this.telemovel = telemovel
        this.pass_salt = pass_salt
        this.pass_hash = pass_hash
        this.peso = peso
        this.altura = altura
        this.gordura = gordura
        this.foto_perfil = foto_perfil
        this.estado = estado
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("id_plano_nutricional", id_plano_nutricional)
        jsonObj.put("nome", nome)
        jsonObj.put("mail", mail)
        jsonObj.put("telemovel", telemovel)
        jsonObj.put("pass_salt", pass_salt)
        jsonObj.put("pass_hash", pass_hash)
        jsonObj.put("peso", peso)
        jsonObj.put("altura", altura)
        jsonObj.put("gordura", gordura)
        jsonObj.put("foto_perfil", foto_perfil)
        jsonObj.put("estado", estado)


        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Cliente {
            return Cliente(
                jsonObject.getInt("id_cliente"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.optInt("id_plano_nutricional"),
                jsonObject.getString("nome"),
                jsonObject.getString("mail"),
                jsonObject.getInt("telemovel"),
                jsonObject.getString("pass_salt"),
                jsonObject.getString("pass_hash"),
                jsonObject.optDouble("peso"),
                jsonObject.optInt("altura"),
                jsonObject.optDouble("gordura"),
                jsonObject.getString("foto_perfil"),
                jsonObject.getString("estado")
            )
        }
    }
}
