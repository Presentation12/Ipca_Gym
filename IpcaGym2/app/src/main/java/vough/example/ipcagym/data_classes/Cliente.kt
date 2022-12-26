package vough.example.ipcagym.data_classes

data class Cliente(
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
)
