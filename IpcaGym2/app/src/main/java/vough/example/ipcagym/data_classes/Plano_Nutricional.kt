package vough.example.ipcagym.data_classes

import org.json.JSONObject

class Plano_Nutricional {
    var id_plano_nutricional : Int? = null
    var id_ginasio: Int? = null
    var tipo : String? = null
    var calorias : Int? = null
    var foto_plano_nutricional  : String? = null

    constructor(
        id_plano_nutricional : Int?,
        id_ginasio: Int?,
        tipo : String?,
        calorias : Int?,
        foto_plano_nutricional : String?

    ) {
        this.id_plano_nutricional  = id_plano_nutricional
        this.id_ginasio = id_ginasio
        this.tipo  = tipo
        this.calorias  = calorias
        this.foto_plano_nutricional  = foto_plano_nutricional
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_plano_nutricional", id_plano_nutricional)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("tipo", tipo)
        jsonObj.put("calorias", calorias)
        jsonObj.put("foto_plano_nutricional", foto_plano_nutricional)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Plano_Nutricional {
            return Plano_Nutricional(
                jsonObject.getInt("id_plano_nutricional"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getString("tipo"),
                jsonObject.getInt("calorias"),
                jsonObject.getString("foto_plano_nutricional")
            )
        }
    }
}