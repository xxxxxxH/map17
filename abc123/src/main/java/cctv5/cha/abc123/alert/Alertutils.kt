//package cctv5.cha.abc123.alert
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Handler
//import android.os.Message
//import android.provider.Settings
//import android.view.LayoutInflater
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import cctv5.cha.abc123.R
//import cctv5.cha.abc123.http.download
//import cctv5.cha.abc123.modle.ResultBean
//import cctv5.cha.abc123.utils.install
//import cctv5.cha.abc123.utils.path
//import cctv5.cha.abc123.utils.xxTools
//import com.bumptech.glide.Glide
//import com.tencent.mmkv.MMKV
//import java.io.File
//
//@SuppressLint("StaticFieldLeak")
////lateinit var current: TextView
//
//private var con: Context? = null
//private var bean: ResultBean? = null
//
//private val handler: Handler = @SuppressLint("HandlerLeak")
//object : Handler() {
//    override fun handleMessage(msg: Message) {
//        super.handleMessage(msg)
//        if (msg.what == 1) {
//            if (!xxTools.get().isBackground(con!!)){
//                if (!MMKV.defaultMMKV().decodeBool("state", false)) {
//                    val updateAlert = updateAlert(con!!, bean!!)
//                    updateAlert.show()
//                }else{
//
//                }
//            }
//        }
//    }
//}
