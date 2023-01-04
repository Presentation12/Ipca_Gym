package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime

class Pedido_Join {
    var id_pedido : Int? = null
    var id_cliente: Int? = null
    var id_ginasio: Int? = null
    var id_produto : Int? = null
    var nome : String? = null
    var tipo_produto : String? = null
    var preco : Double? = null
    var descricao  : String? = null
    var estado_produto : String? = null
    var foto_produto : String? = null
    var quantidade_produto : Int? = null
    var quantidade_pedido : Int? = null
    var data_pedido : LocalDateTime? = null
    var estado_pedido : String? = null

    constructor(
        id_pedido : Int?,
        id_cliente: Int?,
        data_pedido : LocalDateTime?,
        estado_pedido : String?,
        id_produto: Int?,
        id_ginasio: Int?,
        nome : String?,
        tipo_produto : String?,
        preco : Double?,
        descricao  : String?,
        estado_produto : String?,
        foto_produto : String?,
        quantidade_produto : Int?,
        quantidade_pedido : Int?

    ) {
        this.id_pedido  = id_pedido
        this.id_cliente = id_cliente
        this.data_pedido  = data_pedido
        this.estado_pedido  = estado_pedido
        this.id_produto = id_produto
        this.id_ginasio = id_ginasio
        this.nome = nome
        this.tipo_produto = tipo_produto
        this.preco = preco
        this.descricao = descricao
        this.estado_produto = estado_produto
        this.foto_produto = foto_produto
        this.quantidade_produto = quantidade_produto
        this.quantidade_pedido  = quantidade_pedido
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_pedido", id_pedido)
        jsonObj.put("id_cliente", id_cliente)
        jsonObj.put("data_pedido", data_pedido)
        jsonObj.put("estado_pedido", estado_pedido)
        jsonObj.put("id_produto", id_produto)
        jsonObj.put("id_ginasio", id_ginasio)
        jsonObj.put("nome", nome)
        jsonObj.put("tipo_produto", tipo_produto)
        jsonObj.put("preco", preco)
        jsonObj.put("descricao", descricao)
        jsonObj.put("estado_produto", estado_produto)
        jsonObj.put("foto_produto", foto_produto)
        jsonObj.put("quantidade_produto", quantidade_produto)
        jsonObj.put("quantidade_pedido", quantidade_pedido)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Pedido_Join {
            return Pedido_Join(
                jsonObject.getInt("id_pedido"),
                jsonObject.getInt("id_cliente"),
                LocalDateTime.parse(jsonObject.getString("data_pedido")),
                jsonObject.getString("estado_pedido"),
                jsonObject.getInt("id_produto"),
                jsonObject.getInt("id_ginasio"),
                jsonObject.getString("nome"),
                jsonObject.getString("tipo_produto"),
                jsonObject.getDouble("preco"),
                jsonObject.getString("descricao"),
                jsonObject.getString("estado_produto"),
                jsonObject.getString("foto_produto"),
                jsonObject.getInt("quantidade_produto"),
                jsonObject.getInt("quantidade_pedido")
            )
        }
    }
}