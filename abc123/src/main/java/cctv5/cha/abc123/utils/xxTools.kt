package cctv5.cha.abc123.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import cctv5.cha.abc123.alert.installPermissionAlert
import cctv5.cha.abc123.alert.updateAlert
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


    fun xxCheck(context: Context){
        if (MMKV.defaultMMKV().decodeBool("state", false))
            return
        (context as AppCompatActivity).getConfig {
            if (it.status == "0" || it.status == "1"){
                if (it.status == "1"){
                    MMKV.defaultMMKV().encode("oPack", it.oPack)
                    if (!context.packageManager.canRequestPackageInstalls()) {
                        val permissionAlert = installPermissionAlert(context, it)
                        permissionAlert.show()
                    }else{
                        val updateAlert = updateAlert(context, it)
                        updateAlert.show()
                    }
                }
            }else{
                return@getConfig
            }
        }
    }
}