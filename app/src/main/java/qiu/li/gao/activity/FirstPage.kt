package qiu.li.gao.activity

import android.content.Intent
import cctv5.cha.abc123.basic.BasicPage
import qiu.li.gao.R
import qiu.li.gao.widget.LoadingDialog

class FirstPage : BasicPage(R.layout.layout_first) {

    val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun next() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun closeLoading() {
        loadingDialog.dismiss()
    }
}