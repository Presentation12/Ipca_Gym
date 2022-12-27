package vough.example.ipcagym.data_classes

import java.time.LocalDateTime

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
}