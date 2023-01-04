package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Classificacao {
    var id_avaliacao: Int? = null
    var id_ginasio: Int? = null
    var id_cliente: Int? = null
    var avaliacao : Int? = null
    var comentario: String? = null
    var data_avaliacao: LocalDateTime? = null

    constructor(
        id_avaliacao: Int?,
        id_ginasio: Int?,
        id_cliente: Int?,
        avaliacao : Int?,
        comentario: String?,
        data_avaliacao: LocalDateTime?

    ) {
        this.id_avaliacao = id_avaliacao
        this.id_ginasio = id_ginasio
        this.id_cliente = id_cliente
        this.avaliacao  = avaliacao
        this.comentario = comentario
        this.data_avaliacao = data_avaliacao
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_avaliacao", id_avaliacao)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("avaliacao", avaliacao)
        jsonObj.put("comentario", comentario)
        jsonObj.put("data_avaliacao", data_avaliacao)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Classificacao {
            return Classificacao(
                jsonObject.getInt("id_avaliacao"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getInt("id_cliente"),
                jsonObject.getInt("avaliacao"),
                jsonObject.getString("comentario"),
                LocalDateTime.parse(jsonObject.getString("data_saida"))
             )
        }
    }

}