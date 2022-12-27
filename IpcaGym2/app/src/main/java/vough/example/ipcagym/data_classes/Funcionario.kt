package vough.example.ipcagym.data_classes

class Funcionario {
    var id_funcionario : Int? = null
    var id_ginasio: Int? = null
    var nome : String? = null
    var is_admin : Boolean? = null
    var codigo : Int? = null
    var pass_salt  : String? = null
    var pass_hash  : String? = null
    var estado  : String? = null

    constructor(
        id_funcionario : Int?,
        id_ginasio: Int?,
        nome : String?,
        is_admin : Boolean?,
        codigo : Int?,
        pass_salt  : String?,
        pass_hash  : String?,
        estado  : String?

    ) {
        this.id_funcionario  = id_funcionario
        this.id_ginasio = id_ginasio
        this.nome  = nome
        this.is_admin  = is_admin
        this.codigo  = codigo
        this.pass_salt   = pass_salt
        this.pass_hash   = pass_hash
        this.estado   = estado
    }
}