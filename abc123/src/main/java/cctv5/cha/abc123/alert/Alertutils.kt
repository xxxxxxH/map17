package cctv5.cha.abc123.alert

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cctv5.cha.abc123.R
import cctv5.cha.abc123.modle.ResultBean
import com.bumptech.glide.Glide

fun installPermissionAlert(context: Context, data: ResultBean): AlertDialog {
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    dialog.setView(v)
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
                updateAlert(context, data)
            }
        }
    }
    return dialog
}

fun updateAlert(context: Context, data: ResultBean): AlertDialog {
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
    dialog.setView(v)
    v.findViewById<TextView>(R.id.dialogTitle).apply {
        text = "New Version"
    }
    v.findViewById<TextView>(R.id.dialogContent).apply {
        text = formatString(data.ikey)
    }
    v.findViewById<TextView>(R.id.dialogBtn).apply {
        text = "Download"
        setOnClickListener {
            progressAlert(context)
        }
    }
    return dialog
}

fun progressAlert(context: Context):AlertDialog{
    val dialog = AlertDialog.Builder(context).create()
    val v = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
    dialog.setView(v)
    v.findViewById<TextView>(R.id.title).apply {
        text = "Download"
    }
    v.findViewById<TextView>(R.id.current).apply {

    }
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