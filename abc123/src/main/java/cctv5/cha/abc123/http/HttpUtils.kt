package cctv5.cha.abc123.http

import androidx.appcompat.app.AppCompatActivity
import cctv5.cha.abc123.basic.BasicApp
import cctv5.cha.abc123.modle.ResultBean
import cctv5.cha.abc123.utils.*
import com.facebook.FacebookSdk
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

fun AppCompatActivity.getMainId() {
    HttpTools.with(this).fromUrl("https://sichuanlucking.xyz/purewallpaper490/fb.php")
        .ofTypeGet().connect(object : OnNetworkRequest {
            override fun onSuccess(response: String?) {
                "id = $response".loges()
                response?.let {
                    FacebookSdk.setApplicationId(it)
                } ?: run {
                    FacebookSdk.setApplicationId("1598409150521518")
                }
                FacebookSdk.sdkInitialize(this@getMainId)
                fetchAppLink()
                ref()
            }

            override fun onFailure(
                responseCode: Int,
                responseMessage: String,
                errorStream: String
            ) {

            }
        })
}


fun AppCompatActivity.getConfig(block: (ResultBean) -> Unit) {

    val appId = BasicApp.instance!!.getAppId()

    val token = BasicApp.instance!!.getToken()

    val first = MMKV.defaultMMKV().decodeInt("f", 1)

    val url = "https://e2aggj2jy4.execute-api.eu-central-1.amazonaws.com/test?p1=$appId&p2=$token&p3=$appLink&p4=$first"

    HttpTools.with(this).fromUrl(url)
        .ofTypeGet().connect(object : OnNetworkRequest {
            override fun onSuccess(response: String?) {
                "config = $response".loges()
                response?.let {
                    val entity = Gson().fromJson(AesEncryptUtil.decrypt(it), ResultBean::class.java)
                    "entity = $entity".loges()
                    block(entity)
                }
            }

            override fun onFailure(
                responseCode: Int,
                responseMessage: String,
                errorStream: String
            ) {

            }

        })
}
