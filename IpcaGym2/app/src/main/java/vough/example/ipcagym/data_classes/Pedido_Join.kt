package vough.example.ipcagym.data_classes

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

    //TODO: FAZER FROMJSON() E TOJSON()
}