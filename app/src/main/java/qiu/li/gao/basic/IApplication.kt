package qiu.li.gao.basic

import android.Manifest
import cctv5.cha.abc123.basic.BasicApp
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk
import com.tencent.mmkv.MMKV
import qiu.li.gao.R
import java.util.*

class IApplication:BasicApp() {
    override fun getAppId(): String {
        return "507"
    }

    override fun getAppName(): String {
        return ""
    }

    override fun getUrl(): String {
        return ""
    }

    override fun getAesPassword(): String {
        return "U7sDqt5y4C5gYHWu"
    }

    override fun getAesHex(): String {
        return "0Ll7c5PdfHnxJEXT"
    }

    override fun getToken(): String {
        var token = ""
        token = if (MMKV.defaultMMKV()!!.decodeString("token","") == ""){
            UUID.randomUUID().toString()
        }else{
            MMKV.defaultMMKV()!!.decodeString("token","")!!
        }
        return token
    }

    override fun getPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun initt() {
        super.initt()
        MapboxSearchSdk.initialize(
            application = this,
            accessToken = getString(R.string.mapbox_access_token),
            locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        )
    }
}