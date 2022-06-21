package cctv5.cha.abc123.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tencent.mmkv.MMKV

class MyReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_PACKAGE_ADDED) {
            val data = intent.dataString.toString()
            data.let {
                val oPack = MMKV.defaultMMKV().decodeString("oPack","")
                oPack?.let {
                    if (data.contains(it)){
                        MMKV.defaultMMKV().encode("state",true)
                    }
                }
            }
        }
    }
}