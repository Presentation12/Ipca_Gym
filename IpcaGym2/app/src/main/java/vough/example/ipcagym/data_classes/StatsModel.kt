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
    var averageMonday: Double? = null
    var averageTuesday: Double? = null
    var averageWednesday: Double? = null
    var averageThursday: Double? = null
    var averageFriday: Double? = null
    var averageSaturday: Double? = null

    constructor(
        current : Int?,
        exits: Int?,
        today: Int?,
        dailyAverage : Double?,
        monthlyAverage : Double?,
        yearTotal  : Int?,
        maxDay  : Int?,
        averageMonday  : Double?,
        averageTuesday  : Double?,
        averageWednesday  : Double?,
        averageThursday  : Double?,
        averageFriday  : Double?,
        averageSaturday  : Double?
    ) {
        this.current  = current
        this.exits = exits
        this.today = today
        this.dailyAverage = dailyAverage
        this.monthlyAverage = monthlyAverage
        this.yearTotal = yearTotal
        this.yearTotal = yearTotal
        this.maxDay = maxDay
        this.maxMonth = maxMonth
        this.averageMonday = averageMonday
        this.averageTuesday = averageTuesday
        this.averageWednesday = averageWednesday
        this.averageThursday = averageThursday
        this.averageFriday = averageFriday
        this.averageSaturday = averageSaturday
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
        jsonObj.put("averageMonday", averageMonday)
        jsonObj.put("averageTuesday", averageTuesday)
        jsonObj.put("averageWednesday", averageWednesday)
        jsonObj.put("averageThursday", averageThursday)
        jsonObj.put("averageFriday", averageFriday)
        jsonObj.put("averageSaturday", averageSaturday)

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
                jsonObject.optDouble("averageMonday", 0.0),
                jsonObject.optDouble("averageTuesday", 0.0),
                jsonObject.optDouble("averageWednesday", 0.0),
                jsonObject.optDouble("averageThursday", 0.0),
                jsonObject.optDouble("averageFriday", 0.0),
                jsonObject.optDouble("averageSaturday", 0.0)
            )
        }
    }
}