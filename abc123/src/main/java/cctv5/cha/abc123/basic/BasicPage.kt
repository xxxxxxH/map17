package cctv5.cha.abc123.basic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cctv5.cha.abc123.http.getMainId
import cctv5.cha.abc123.utils.countDown
import cctv5.cha.abc123.utils.requestPermissions
import cctv5.cha.abc123.utils.xRegisterReceiver

abstract class BasicPage(id: Int) : AppCompatActivity(id) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
        xRegisterReceiver()
        requestPermissions {
            if (it) {
                getMainId()
                countDown {
                    closeLoading()
                    next()
                }
            } else {
                finish()
            }
        }
    }

    abstract fun next()

    abstract fun showLoading()

    abstract fun closeLoading()

}