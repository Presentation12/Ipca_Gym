package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime

class Pedido_Loja {
    var id_pedido : Int? = null
    var id_produto: Int? = null
    var quantidade_pedido : Int? = null

    constructor(
        id_pedido : Int?,
        id_produto: Int?,
        quantidade_pedido : Int?

    ) {
        this.id_pedido  = id_pedido
        this.id_produto = id_produto
        this.quantidade_pedido  = quantidade_pedido
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_pedido", id_pedido)
        jsonObj.put("id_produto", id_produto)
        jsonObj.put("quantidade_pedido", quantidade_pedido)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Pedido_Loja {
            return Pedido_Loja(
                jsonObject.getInt("id_pedido"),
                jsonObject.getInt("id_produto"),
                jsonObject.getInt("quantidade_pedido")
            )
        }
    }
}