package vough.example.ipcagym.data_classes

import java.time.LocalTime

class Refeicao {
    var id_refeicao : Int? = null
    var id_plano_nutricional : Int? = null
    var descricao : String? = null
    var hora : LocalTime? = null
    var foto_refeicao : String? = null

    constructor(
        id_refeicao: Int?,
        id_plano_nutricional: Int?,
        descricao: String?,
        hora: LocalTime?,
        foto_refeicao: String?

    ) {
        this.id_refeicao = id_refeicao
        this.id_plano_nutricional = id_plano_nutricional
        this.descricao = descricao
        this.hora = hora
        this.foto_refeicao = foto_refeicao
    }
}