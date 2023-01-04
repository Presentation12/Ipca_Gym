package vough.example.ipcagym.data_classes

import org.json.JSONObject

class Plano_Treino {
    var id_plano_treino : Int? = null
    var id_ginasio: Int? = null
    var tipo : String? = null
    var foto_plano_treino : String? = null

    constructor(
        id_plano_treino : Int?,
        id_ginasio: Int?,
        tipo : String?,
        foto_plano_treino : String?

    ) {
        this.id_plano_treino  = id_plano_treino
        this.id_ginasio = id_ginasio
        this.tipo  = tipo
        this.foto_plano_treino  = foto_plano_treino
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_plano_treino", id_plano_treino)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("tipo", tipo)
        jsonObj.put("foto_plano_treino", foto_plano_treino)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Plano_Treino {
            return Plano_Treino(
                jsonObject.getInt("id_plano_treino"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getString("tipo"),
                jsonObject.getString("foto_plano_treino")
            )
        }
    }
}