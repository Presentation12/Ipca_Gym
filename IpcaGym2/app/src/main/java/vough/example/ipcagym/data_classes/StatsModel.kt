package vough.example.ipcagym.data_classes

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.LocalTime

class StatsModel{
    var current: Int? = null
    var exits: Int? = null
    var today: Int? = null
    var dailyAverage: Double? = null
    var monthlyAverage: Double? = null
    var yearTotal: Int? = null
    var maxDay: Int? = null
    var maxMonth: Int? = null

    constructor(
        current : Int?,
        exits: Int?,
        today: Int?,
        dailyAverage : Double?,
        monthlyAverage : Double?,
        yearTotal  : Int?,
        maxDay  : Int?,
        maxMonth  : Int?
    ) {
        this.current  = current
        this.exits = exits
        this.today = today
        this.dailyAverage  = dailyAverage
        this.monthlyAverage  = monthlyAverage
        this.yearTotal   = yearTotal
        this.yearTotal   = yearTotal
        this.maxDay   = maxDay
        this.maxMonth   = maxMonth
    }

    fun toJSON() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("current", current)
        jsonObj.put("exits", exits)
        jsonObj.put("today", today)
        jsonObj.put("dailyAverage", dailyAverage)
        jsonObj.put("monthlyAverage", monthlyAverage)
        jsonObj.put("yearTotal", yearTotal)
        jsonObj.put("maxDay", maxDay)
        jsonObj.put("maxMonth", maxMonth)

        return jsonObj
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : StatsModel {
            return StatsModel(
                jsonObject.getInt("current"),
                jsonObject.getInt("exits"),
                jsonObject.getInt("today"),
                jsonObject.getDouble("dailyAverage"),
                jsonObject.getDouble("monthlyAverage"),
                jsonObject.getInt("yearTotal"),
                jsonObject.getInt("maxDay"),
                jsonObject.getInt("maxMonth"),
            )
        }
    }
}