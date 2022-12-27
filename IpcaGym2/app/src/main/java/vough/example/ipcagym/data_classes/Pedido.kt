package vough.example.ipcagym.data_classes

import java.time.LocalDateTime

class Pedido {
    var id_pedido : Int? = null
    var id_cliente: Int? = null
    var data_pedido : LocalDateTime? = null
    var estado_pedido : String? = null

    constructor(
        id_pedido : Int?,
        id_cliente: Int?,
        data_pedido : LocalDateTime?,
        estado_pedido : String?

    ) {
        this.id_pedido  = id_pedido
        this.id_cliente = id_cliente
        this.data_pedido  = data_pedido
        this.estado_pedido  = estado_pedido
    }
}