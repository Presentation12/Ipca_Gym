package vough.example.ipcagym.data_classes

import org.json.JSONObject

class LoginReceiverModel {
    var token : String? = null
    var role: String? = null

    constructor(token : String, role : String){
        this.token = token
        this.role = role
    }

    companion object{
        fun fromJson(jsonObject: JSONObject) : LoginReceiverModel {
            return LoginReceiverModel(
                jsonObject.getString("token"),
                jsonObject.getString("role")
            )
        }
    }
}