package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalTime

class Refeicao {
    var id_refeicao : Int? = null
    var id_plano_nutricional : Int? = null
    var descricao : String? = null
    var hora : LocalTime? = null
    var foto_refeicao : String? = null

    constructor(
        id_refeicao: Int?,
        id_plano_nutricional: Int?,
        descricao: String?,
        hora: LocalTime?,
        foto_refeicao: String?

    ) {
        this.id_refeicao = id_refeicao
        this.id_plano_nutricional = id_plano_nutricional
        this.descricao = descricao
        this.hora = hora
        this.foto_refeicao = foto_refeicao
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_refeicao", id_refeicao)
        jsonObj.put("id_plano_nutricional", id_plano_nutricional)
        jsonObj.put("descricao", descricao)
        jsonObj.put("hora", hora)
        jsonObj.put("foto_refeicao", foto_refeicao)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Refeicao {
            return Refeicao(
                jsonObject.getInt("id_refeicao"),
                jsonObject.getInt("id_plano_nutricional"),
                jsonObject.getString("descricao"),
                LocalTime.parse(jsonObject.getString("hora")),
                jsonObject.getString("foto_refeicao")
            )
        }
    }
}