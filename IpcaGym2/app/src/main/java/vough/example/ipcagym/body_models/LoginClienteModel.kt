package vough.example.ipcagym.body_models

import org.json.JSONObject

object LoginClienteModel {
    var mail: String? = null
    var password: String? = null

    fun toJSON() : JSONObject {
        val jsonObj = JSONObject()

        jsonObj.put("mail", mail)
        jsonObj.put("password", password)

        return jsonObj
    }
}