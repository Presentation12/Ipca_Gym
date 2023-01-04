package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime

class Marcacao {
    var id_marcacao : Int? = null
    var id_funcionario: Int? = null
    var id_cliente: Int? = null
    var data_marcacao : LocalDateTime? = null
    var descricao : String? = null
    var estado  : String? = null

    constructor(
        id_marcacao : Int?,
        id_funcionario: Int?,
        id_cliente: Int?,
        data_marcacao : LocalDateTime?,
        descricao : String?,
        estado  : String?

    ) {
        this.id_marcacao  = id_marcacao
        this.id_funcionario = id_funcionario
        this.id_cliente = id_cliente
        this.data_marcacao  = data_marcacao
        this.descricao  = descricao
        this.estado   = estado

    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_marcacao", id_marcacao)
        jsonObj.put("id_funcionario", id_funcionario)
        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("data_marcacao", data_marcacao)
        jsonObj.put("descricao", descricao)
        jsonObj.put("estado", estado)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Marcacao {
            return Marcacao(
                jsonObject.getInt("id_marcacao"),
                jsonObject.getInt("id_funcionario"),
                jsonObject.getInt("id_cliente"),
                LocalDateTime.parse(jsonObject.getString("data_marcacao")),
                jsonObject.getString("descricao"),
                jsonObject.getString("estado")
                )
        }
    }
}