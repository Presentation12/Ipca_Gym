package vough.example.ipcagym.data_classes

import java.time.LocalDateTime

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
}