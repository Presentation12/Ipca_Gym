package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Exercicio {
    var id_exercicio: Int? = null
    var id_plano_treino : Int? = null
    var nome : String? = null
    var descricao : String? = null
    var tipo : String? = null
    var series : Int? = null
    var tempo : LocalTime? = null
    var repeticoes : Int? = null
    var foto_exercicio : String? = null

    constructor(
        id_exercicio: Int?,
        id_plano_treino : Int?,
        nome : String?,
        descricao : String?,
        tipo : String?,
        series : Int?,
        tempo : LocalTime?,
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

    fun toJson() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("id_exercicio", id_exercicio)
        jsonObj.put("id_plano_treino", id_plano_treino)
        jsonObj.put("nome", nome)
        jsonObj.put("descricao", descricao)
        jsonObj.put("tipo", tipo)
        jsonObj.put("series", series)
        jsonObj.put("tempo", tempo)
        jsonObj.put("repeticoes", repeticoes)
        jsonObj.put("foto_exercicio", foto_exercicio)

        return jsonObj
    }

    //TODO: VERIFICAR TEMPO
    companion object{
        fun fromJson(jsonObject: JSONObject) : Exercicio {
            return Exercicio(
                jsonObject.getInt("id_exercicio"),
                jsonObject.getInt("id_plano_treino"),
                jsonObject.getString("nome"),
                jsonObject.getString("descricao"),
                jsonObject.getString("tipo"),
                jsonObject.optInt("series"),
                jsonObject["tempo"]?.let { LocalTime.parse(it.toString()) } ?: null,
                jsonObject.optInt("repeticoes"),
                jsonObject.getString("foto_exercicio")
            )
        }
    }
}