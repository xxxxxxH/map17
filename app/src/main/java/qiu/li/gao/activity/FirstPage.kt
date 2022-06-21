package qiu.li.gao.activity

import android.content.Intent
import cctv5.cha.abc123.basic.BasicPage
import qiu.li.gao.R

class FirstPage : BasicPage(R.layout.layout_first) {
    override fun next() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showLoading() {

    }

    override fun closeLoading() {

    }
}