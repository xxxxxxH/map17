package cctv5.cha.abc123.http

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cctv5.cha.abc123.basic.BasicApp
import cctv5.cha.abc123.modle.RequestBean
import cctv5.cha.abc123.modle.ResultBean
import cctv5.cha.abc123.utils.*
import com.facebook.FacebookSdk
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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
    OkGo.post<String>("https://smallfun.xyz/worldweather361/weather2.php")
        .params("data", requestData()).execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>?) {
                "config = $response".loges()
                lifecycleScope.launch(Dispatchers.Main) {
                    response?.let {
                        it.body()?.let { body ->
                            val entity:ResultBean = Gson().fromJson(AesEncryptUtil.decrypt(body.toString()),ResultBean::class.java)
                            "entity = $entity".loges()
                            block(entity)
                        }
                    }
                }
            }

            override fun onError(response: Response<String>?) {
                super.onError(response)
                "error = $response".loges()
            }
        })
}

fun AppCompatActivity.getData(url: String,s:(String)->Unit, f:()->Unit){
    HttpTools.with(this).fromUrl(url)
        .ofTypeGet().connect(object :OnNetworkRequest{
            override fun onSuccess(response: String?) {
               response?.let {
                   s(it)
               }
            }

            override fun onFailure(
                responseCode: Int,
                responseMessage: String,
                errorStream: String
            ) {
                f()
            }

        })
}

fun download(context: Context, url:String, block: (Int) -> Unit){
    val file = File(filePath + fileName)
    if (file.exists())file.delete()
    OkGo.get<File>(url).execute(object : FileCallback(filePath, fileName){
        override fun onSuccess(response: Response<File>?) {

        }

        override fun downloadProgress(progress: Progress?) {
            super.downloadProgress(progress)
            val current = progress?.currentSize
            val total = progress?.totalSize
            val pro = ((current!! *100) / total!!).toInt()
            block(pro)
            "progress = ${progress?.fraction}".loges()
        }

        override fun onError(response: Response<File>?) {
            super.onError(response)
            response?.exception.toString().loges()
        }

        override fun onFinish() {
            super.onFinish()
        }
    })
}
