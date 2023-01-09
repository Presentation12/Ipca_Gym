package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

class Pedido {
    var id_pedido : Int? = null
    var id_cliente: Int? = null
    var data_pedido : LocalDateTime? = null
    var estado_pedido : String? = null

    constructor(
        id_pedido: Int?,
        id_cliente: Int?,
        data_pedido: LocalDateTime,
        estado_pedido: String?

    ) {
        this.id_pedido  = id_pedido
        this.id_cliente = id_cliente
        this.data_pedido  = data_pedido
        this.estado_pedido  = estado_pedido
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_pedido", id_pedido)
        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("data_pedido", data_pedido)
        jsonObj.put("estado_pedido", estado_pedido)


        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Pedido {
            return Pedido(
                jsonObject.getInt("id_pedido"),
                jsonObject.getInt("id_cliente"),
                LocalDateTime.parse(jsonObject.getString("data_pedido")),
                jsonObject.getString("estado_pedido")
            )
        }
    }
}