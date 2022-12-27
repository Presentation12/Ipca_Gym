package vough.example.ipcagym.data_classes

/*data class Cliente(
    val id_cliente: Int,
    val id_ginasio: Int,
    val id_plano_nutricional: Int,
    val nome: String,
    val mail: String,
    val telemovel: String,
    val pass_salt: String,
    val pass_hash: String,
    val peso: Double,
    val altura: Int,
    val gordura: Double,
    val foto_perfil: String,
    val estado: String
)*/

class Cliente{
    var id_cliente: Int? = null
    var id_ginasio: Int? = null
    var id_plano_nutricional: Int? = null
    var nome: String? = null
    var mail: String? = null
    var telemovel: String? = null
    var pass_salt: String? = null
    var pass_hash: String? = null
    var peso: Double? = null
    var altura: Int? = null
    var gordura: Double? = null
    var foto_perfil: String? = null
    var estado: String? = null

    constructor(
        id_cliente: Int?,
        id_ginasio: Int,
        id_plano_nutricional: Int,
        nome: String,
        mail: String,
        telemovel: String,
        pass_salt: String,
        pass_hash: String,
        peso: Double,
        altura: Int,
        gordura: Double,
        foto_perfil: String,
        estado: String
    ) {
        this.id_cliente = id_cliente
        this.id_ginasio = id_ginasio
        this.id_plano_nutricional = id_plano_nutricional
        this.nome = nome
        this.mail = mail
        this.telemovel = telemovel
        this.pass_salt = pass_salt
        this.pass_hash = pass_hash
        this.peso = peso
        this.altura = altura
        this.gordura = gordura
        this.foto_perfil = foto_perfil
        this.estado = estado
    }
}
