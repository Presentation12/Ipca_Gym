package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalTime

class Horario_Funcionario {
    var id_funcionario_horario : Int? = null
    var id_funcionario : Int? = null
    var hora_entrada : LocalTime? = null
    var hora_saida: LocalTime? = null
    var dia_semana: String? = null

    constructor(
        id_funcionario_horario : Int?,
        id_funcionario : Int?,
        hora_entrada : LocalTime?,
        hora_saida : LocalTime?,
        dia_semana: String?

    ) {
        this.id_funcionario_horario  = id_funcionario_horario
        this.id_funcionario  = id_funcionario
        this.hora_entrada  = hora_entrada
        this.hora_saida = hora_saida
        this.dia_semana  = dia_semana
    }

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_funcionario_horario", id_funcionario_horario)
        jsonObj.put("id_funcionario", id_funcionario)
        jsonObj.put("hora_entrada", hora_entrada)
        jsonObj.put("hora_saida", hora_saida)
        jsonObj.put("dia_semana", dia_semana)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : Horario_Funcionario {
            return Horario_Funcionario(
                jsonObject.getInt("id_funcionario_horario"),
                jsonObject.getInt("id_funcionario"),
                LocalTime.parse(jsonObject.getString("hora_entrada")),
                LocalTime.parse(jsonObject.getString("hora_saida")),
                jsonObject.getString("dia_semana")
            )
        }
    }
}