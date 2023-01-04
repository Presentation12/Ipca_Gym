package vough.example.ipcagym.requests

import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import vough.example.ipcagym.data_classes.Atividade
import vough.example.ipcagym.data_classes.Classificacao

object ClassificacaoRequests {
    private val client = OkHttpClient()

    fun GetAll(scope: CoroutineScope, token : String?, callback: (ArrayList<Classificacao>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun GetByID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Classificacao) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Post(scope: CoroutineScope, token : String?, jsonRequestBody : String?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Patch(scope: CoroutineScope, token : String?, jsonRequestBody : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun Delete(scope: CoroutineScope, token : String?, targetID : Int?, callback: (Boolean) -> Unit){
        //TODO: POR IMPLEMENTAR
    }

    fun GetAllClassificationsByGinasioID(scope: CoroutineScope, token : String?, targetID : Int?, callback: (ArrayList<Classificacao>) -> Unit){
        //TODO: POR IMPLEMENTAR
    }
}