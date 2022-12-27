package vough.example.ipcagym.data_classes

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
}