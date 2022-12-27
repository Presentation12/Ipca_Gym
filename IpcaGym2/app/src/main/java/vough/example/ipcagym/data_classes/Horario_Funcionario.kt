package vough.example.ipcagym.data_classes

import java.time.LocalTime

class Horario_Funcionario {
    var id_funcionario_horario : Int? = null
    var id_funcionario : Int? = null
    var hora_entrada : Int? = null
    var hora_saida: LocalTime? = null
    var dia_semana: LocalTime? = null

    constructor(
        id_funcionario_horario : Int?,
        id_funcionario : Int?,
        hora_entrada : Int?,
        hora_saida : LocalTime?,
        dia_semana: LocalTime?

    ) {
        this.id_funcionario_horario  = id_funcionario_horario
        this.id_funcionario  = id_funcionario
        this.hora_entrada  = hora_entrada
        this.hora_saida = hora_saida
        this.dia_semana  = dia_semana
    }
}