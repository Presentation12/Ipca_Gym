package vough.example.ipcagym.data_classes

class Loja {
    var id_produto : Int? = null
    var id_ginasio: Int? = null
    var nome : String? = null
    var tipo_produto : String? = null
    var preco : Double? = null
    var descricao  : String? = null
    var estado_produto : String? = null
    var foto_produto : String? = null
    var quantidade_produto : Int? = null

    constructor(
        id_produto: Int?,
        id_ginasio: Int?,
        nome : String?,
        tipo_produto : String?,
        preco : Double?,
        descricao  : String?,
        estado_produto : String?,
        foto_produto : String?,
        quantidade_produto : Int?

    ) {
        this.id_produto = id_produto
        this.id_ginasio = id_ginasio
        this.nome = nome
        this.tipo_produto = tipo_produto
        this.preco = preco
        this.descricao = descricao
        this.estado_produto = estado_produto
        this.foto_produto = foto_produto
        this.quantidade_produto = quantidade_produto
    }
}