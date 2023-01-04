package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Atividade {
    var id_atividade: Int? = null
    var id_ginasio: Int? = null
    var id_cliente: Int? = null
    var data_entrada: LocalDateTime? = null
    var data_saida: LocalDateTime? = null

    constructor(
        id_atividade: Int?,
        id_ginasio: Int?,
        id_cliente: Int?,
        data_entrada: LocalDateTime?,
        data_saida: LocalDateTime?
    ) {
        this.id_atividade = id_atividade
        this.id_ginasio = id_ginasio
        this.id_cliente = id_cliente
        this.data_entrada = data_entrada
        this.data_saida = data_saida
    }

    fun toJson() : JSONObject{
        val jsonObj = JSONObject()

        jsonObj.put("id_atividade", id_atividade)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("data_entrada", data_entrada)
        jsonObj.put("data_saida", data_saida)

        return jsonObj
    }

    //TODO: VER O PARSE DE DATA_SAIDA (PODE SER NULL)
    companion object{
        fun fromJson(jsonObject: JSONObject) : Atividade {
            return Atividade(
                jsonObject.getInt("id_atividade"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getInt("id_cliente"),
                LocalDateTime.parse(jsonObject.getString("data_entrada")),
                LocalDateTime.parse(jsonObject.getString("data_saida"))
            )
        }
    }
}
