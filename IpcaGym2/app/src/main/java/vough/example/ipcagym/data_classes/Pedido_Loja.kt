package vough.example.ipcagym.data_classes

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
}