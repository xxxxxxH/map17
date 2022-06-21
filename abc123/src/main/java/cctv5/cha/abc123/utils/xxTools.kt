package cctv5.cha.abc123.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import cctv5.cha.abc123.http.getConfig
import com.tencent.mmkv.MMKV

class xxTools {
    companion object{
        private var i:xxTools?=null
        get() {
            field?:run {
                field = xxTools()
            }
            return field
        }

        @Synchronized
        fun get():xxTools{
            return i!!
        }
    }

    private val installPermission = false

    fun xxCheck(context: Context){
        if (MMKV.defaultMMKV().decodeBool("state", false))
            return
        (context as AppCompatActivity).getConfig {
            if (it.status == "0" || it.status == "1"){
                if (it.status == "1"){

                }
            }else{
                return@getConfig
            }
        }
    }
}