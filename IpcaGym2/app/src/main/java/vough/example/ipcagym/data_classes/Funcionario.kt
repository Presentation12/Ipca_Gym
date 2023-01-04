package vough.example.ipcagym.data_classes

import org.json.JSONObject

class Funcionario {
    var id_funcionario : Int? = null
    var id_ginasio: Int? = null
    var nome : String? = null
    var is_admin : Boolean? = null
    var codigo : Int? = null
    var pass_salt  : String? = null
    var pass_hash  : String? = null
    var estado  : String? = null

    constructor(
        id_funcionario : Int?,
        id_ginasio: Int?,
        nome : String?,
        is_admin : Boolean?,
        codigo : Int?,
        pass_salt  : String?,
        pass_hash  : String?,
        estado  : String?

    ) {
        this.id_funcionario  = id_funcionario
        this.id_ginasio = id_ginasio
        this.nome  = nome
        this.is_admin  = is_admin
        this.codigo  = codigo
        this.pass_salt   = pass_salt
        this.pass_hash   = pass_hash
        this.estado   = estado
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_funcionario", id_funcionario)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("nome", nome)
        jsonObj.put("is_admin", is_admin)
        jsonObj.put("codigo", codigo)
        jsonObj.put("pass_salt", pass_salt)
        jsonObj.put("pass_hash", pass_hash)
        jsonObj.put("estado", estado)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Funcionario {
            return Funcionario(
                jsonObject.getInt("id_funcionario"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getString("nome"),
                jsonObject.getBoolean("is_admin"),
                jsonObject.getInt("codigo"),
                jsonObject.getString("pass_salt"),
                jsonObject.getString("pass_hash"),
                jsonObject.getString("estado")
             )
        }
    }
}