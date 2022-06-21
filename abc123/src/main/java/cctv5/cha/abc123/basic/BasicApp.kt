package cctv5.cha.abc123.basic

import android.app.Application
import com.tencent.mmkv.MMKV

abstract class BasicApp :Application(){
    companion object {
        var instance: BasicApp? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        instance = this
        initt()
    }

    open fun initt(){}

    abstract fun getAppId(): String
    abstract fun getAppName(): String
    abstract fun getUrl(): String
    abstract fun getAesPassword(): String
    abstract fun getAesHex(): String
    abstract fun getToken(): String
    abstract fun getPermissions(): Array<String>
}