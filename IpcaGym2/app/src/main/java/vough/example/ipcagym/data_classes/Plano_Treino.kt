package vough.example.ipcagym.data_classes

class Plano_Treino {
    var id_plano_treino : Int? = null
    var id_ginasio: Int? = null
    var tipo : String? = null
    var foto_plano_treino : String? = null

    constructor(
        id_plano_treino : Int?,
        id_ginasio: Int?,
        tipo : String?,
        foto_plano_treino : String?

    ) {
        this.id_plano_treino  = id_plano_treino
        this.id_ginasio = id_ginasio
        this.tipo  = tipo
        this.foto_plano_treino  = foto_plano_treino
    }
}