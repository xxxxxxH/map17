package qiu.li.gao.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import qiu.li.gao.R


class ToobarView : LinearLayout {

    private lateinit var menu: ImageView

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context): View {
        val v = LayoutInflater.from(context).inflate(R.layout.zhuye_top, this, true)
        menu = v.findViewById<ImageView>(R.id.menu)
        return v
    }

    fun getMenuBtn(): ImageView {
        return menu
    }
}