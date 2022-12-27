package vough.example.ipcagym.data_classes

import java.time.Duration

class Exercicio {
    var id_exercicio: Int? = null
    var id_plano_treino : Int? = null
    var nome : String? = null
    var descricao : String? = null
    var tipo : String? = null
    var series : Int? = null
    var tempo : Duration? = null
    var repeticoes : Int? = null
    var foto_exercicio : String? = null

    constructor(
        id_exercicio: Int?,
        id_plano_treino : Int?,
        nome : String?,
        descricao : String?,
        tipo : String?,
        series : Int?,
        tempo : Duration?,
        repeticoes : Int?,
        foto_exercicio : String?


    ) {
        this.id_exercicio = id_exercicio
        this.id_plano_treino  = id_plano_treino
        this.nome  = nome
        this.descricao  = descricao
        this.tipo = tipo
        this.series = series
        this.tempo = tempo
        this.repeticoes = repeticoes
        this.foto_exercicio = foto_exercicio
    }
}