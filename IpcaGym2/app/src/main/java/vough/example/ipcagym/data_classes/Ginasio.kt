package vough.example.ipcagym.data_classes

import org.json.JSONObject

class Ginasio {
    var id_ginasio: Int? = null
    var instituicao: String? = null
    var estado : String? = null
    var foto_ginasio : String? = null
    var contacto  : Int? = null
    var lotacao  : Int? = null
    var lotacaoMax  : Int? = null

    constructor(
        id_ginasio: Int?,
        instituicao: String?,
        estado : String?,
        foto_ginasio : String?,
        contacto : Int?,
        lotacao : Int?,
        lotacaoMax : Int?

    ) {
        this.id_ginasio = id_ginasio
        this.instituicao = instituicao
        this.estado  = estado
        this.foto_ginasio  = foto_ginasio
        this.contacto  = contacto
        this.lotacao  = lotacao
        this.lotacaoMax  = lotacaoMax
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("instituicao", instituicao)
        jsonObj.put("estado", estado)
        jsonObj.put("foto_ginasio", foto_ginasio)
        jsonObj.put("contacto", contacto)
        jsonObj.put("lotacao", lotacao)
        jsonObj.put("lotacaoMax", lotacaoMax)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Ginasio {
            return Ginasio(
                jsonObject.getInt("id_ginasio"),
                jsonObject.getString("instituicao"),
                jsonObject.getString("estado"),
                jsonObject.getString("foto_ginasio"),
                jsonObject.getInt("contacto"),
                jsonObject.getInt("lotacao"),
                jsonObject.getInt("lotacaoMax")
            )
        }
    }
}