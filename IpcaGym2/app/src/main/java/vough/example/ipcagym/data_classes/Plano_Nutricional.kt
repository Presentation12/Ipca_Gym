package vough.example.ipcagym.data_classes

class Plano_Nutricional {
    var id_plano_nutricional : Int? = null
    var id_ginasio: Int? = null
    var tipo : String? = null
    var calorias : Int? = null
    var foto_plano_nutricional  : Int? = null

    constructor(
        id_plano_nutricional : Int?,
        id_ginasio: Int?,
        tipo : String?,
        calorias : Int?,
        foto_plano_nutricional : Int?

    ) {
        this.id_plano_nutricional  = id_plano_nutricional
        this.id_ginasio = id_ginasio
        this.tipo  = tipo
        this.calorias  = calorias
        this.foto_plano_nutricional  = foto_plano_nutricional
    }
}