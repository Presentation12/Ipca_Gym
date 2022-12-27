package vough.example.ipcagym.data_classes

class Ginasio {
    var id_ginasio: Int? = null
    var instituicao: String? = null
    var estado : String? = null
    var foto_ginasio : String? = null
    var contacto  : Int? = null
    var lotacao  : Int? = null
    var lotacaoMax  : Int? = null

    constructor(
        id_ginasio: Int?,
        instituicao: String?,
        estado : String?,
        foto_ginasio : String?,
        contacto : Int?,
        lotacao : Int?,
        lotacaoMax : Int?

    ) {
        this.id_ginasio = id_ginasio
        this.instituicao = instituicao
        this.estado  = estado
        this.foto_ginasio  = foto_ginasio
        this.contacto  = contacto
        this.lotacao  = lotacao
        this.lotacaoMax  = lotacaoMax
    }
}